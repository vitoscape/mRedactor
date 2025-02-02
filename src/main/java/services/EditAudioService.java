package services;

import model.Album;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.images.Artwork;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static utils.TerminalUtil.clearTerminal;
import static utils.ImageUtil.*;

public class EditAudioService {
	
	private final Scanner terminalInput = new Scanner(System.in);
	
	private static final HashMap<Integer, FieldKey> tags = new HashMap<>();
	
	private final File files[];
	
	private AudioFile audioFile;
	private Tag tag = new FlacTag();
	
	// Fill tags HashMap
	static {
		tags.put(1, FieldKey.GENRE);
		tags.put(2, FieldKey.ARTIST);
		tags.put(3, FieldKey.ALBUM);
		tags.put(4, FieldKey.YEAR);
	}
	
	
	public EditAudioService(File files[]) {
		this.files = files;
	}
	
	/**
	 * Fills album tag field values by terminal input: artist, album, genre, year and total track number.
	 * <p>
	 * Every field can be set to {@code 0} if that field does not need changing.
	 *
	 * @return The filled with tags {@code Album} object.
	 */
	@NotNull
	private Album fillAlbum() {
		
		Album album = new Album();
		
		System.out.print("Enter tags that you want to set up. Type 0 if you want to keep tag.\n");
		System.out.print("Artist: ");
		album.setArtist(terminalInput.nextLine());
		
		System.out.print("Album: ");
		album.setAlbum(terminalInput.nextLine());
		
		System.out.print("Genre: ");
		album.setGenre(terminalInput.nextLine());
		
		System.out.print("Year: ");
		album.setYear(terminalInput.nextLine());
		
		System.out.print("Number of tracks: ");
		album.setTrackTotal(terminalInput.nextLine());
		
		return album;
	}
	
	/**
	 * Apply tag changes to audio file.
	 *
	 * @param tag	audio file Tag object that will be changed
	 * @param album	album object with changed desired tag values
	 * @return String that contains artist tag value that will be used to rename the file
	 * @throws FieldDataInvalidException if {@code TRACK} field key that contains in the file has non-digit chars
	 */
	private String processTag(@NotNull Tag tag, @NotNull Album album) throws FieldDataInvalidException {
		
		String artistToRename;
		
		tag.deleteField("ALBUMARTIST");		// Use these three ways to delete fields because this field may vary
		tag.deleteField("ALBUM ARTIST");
		tag.deleteField(FieldKey.ALBUM_ARTIST);
		
		// Delete insignificant zeros in track number
		int trackNumber = 0;
		
		try {
			trackNumber = Integer.parseInt(tag.getFirst(FieldKey.TRACK));
			tag.setField(FieldKey.TRACK, String.valueOf(trackNumber));
		} catch (NumberFormatException _) {}
		
		
		if (!album.getArtist().equals("0")) {
			
			tag.setField(FieldKey.ARTIST, album.getArtist());
			tag.setField(FieldKey.ALBUM_ARTIST, album.getArtist());
			
			artistToRename = album.getArtist();
			
		} else {
			artistToRename = tag.getFirst(FieldKey.ARTIST);			// If changing tag value doesn't need then read tag value from file to rename file
			tag.setField(FieldKey.ALBUM_ARTIST, artistToRename);
		}
		
		if (!album.getAlbum().equals("0")) {
			tag.setField(FieldKey.ALBUM, album.getAlbum());
		}
		
		if (!album.getGenre().equals("0")) {
			tag.setField(FieldKey.GENRE, album.getGenre());
		}
		
		if (!album.getYear().equals("0")) {
			tag.setField(FieldKey.YEAR, album.getYear());
		}
		
		if (!album.getTrackTotal().equals("0")) {
			tag.setField(FieldKey.TRACK_TOTAL, album.getTrackTotal());
		}
		
		tag.setField(FieldKey.COMMENT, "");	// Remove comment
		tag.deleteField("DESCRIPTION");
		tag.deleteField("NOTES");
		tag.deleteField("LENGTH");
		
		return artistToRename;
	}
	
	/**
	 * Remove track number from the title.
	 * Some audiofile's tags contains track number in title field.
	 *
	 * @param tag the tag in the {@code TITLE} field of which the track number will be deleted
	 */
	private void deleteTrackNumberFromTitle(@NotNull Tag tag) {
		
		String title = tag.getFirst(FieldKey.TITLE);
		
		int firstSpaceIndex = title.indexOf(" ");		// Get index of first space to get first word of the title
		
		if (firstSpaceIndex > 0) {																		// If more than 1 words in title
			String subString = title.substring(0, firstSpaceIndex);										// Get first word from title
			try {
				if (Integer.parseInt(subString) == Integer.parseInt(tag.getFirst(FieldKey.TRACK))) {	// Compare first word as int and track number
					tag.setField(FieldKey.TITLE, title.substring(firstSpaceIndex + 1));		// Set new title without track number
				}
			} catch (NumberFormatException | FieldDataInvalidException _) {}
		}
	}
	
	/**
	 * Delete forbidden filename characters from {@code fileName}
	 *
	 * @param fileName the name of the file from which the forbidden characters will be deleted
	 */
	private void deleteForbiddenCharacters(String fileName) {
		
		// If new name contains forbidden characters for files then delete these characters
		if (fileName.matches(".*[<>\"/\\\\|?*:].*")) {
			fileName = fileName.replace("<", "");
			fileName = fileName.replace(">", "");
			fileName = fileName.replace("\"", "");
			fileName = fileName.replace("/", "");
			fileName = fileName.replace("\\", "");
			fileName = fileName.replace("|", "");
			fileName = fileName.replace("?", "");
			fileName = fileName.replace("*", "");
			fileName = fileName.replace(":", "");
		}
	}
	
	/**
	 * Create new full path name for audiofile. Required for renaming.
	 *
	 * @param file				the file that will be used to get filename
	 * @param artistToRename	artist is used to create filename following template {@code Artist - Title.extension}
	 * @return new full path name
	 */
	@NotNull
	private String makeNewPathName(@NotNull File file, String artistToRename) {
		
		String fileName = file.getName();
		
		int dotIndex = fileName.lastIndexOf('.');								// Get index of last  '.' char to get extension
		String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);	// Get extension
		
		Path dirPath = Paths.get(file.getPath()).getParent();
		String newName = artistToRename + " - " + tag.getFirst(FieldKey.TITLE) + extension;	// Create new file name
		
		return dirPath + "\\" + newName;								// And finally create new full path name
	}
	
	/**
	 * Rename the {@code file}.
	 *
	 * @param file				file that will be renamed
	 * @param artistToRename	artist is used to create filename following template {@code Artist - Title.extension},
	 *                          used in {@link #makeNewPathName(File file, String artistToRename)}  method
	 */
	private void renameFile(@NotNull File file, String artistToRename) {
		
		String fileName = file.getName();
		String newPathName = makeNewPathName(file, artistToRename);
		
		if (!file.renameTo(new File(newPathName))) {
			System.out.printf("Error while renaming file: %s\n", fileName);
		}
	}
	
	/**
	 * Edit full album with set field values.
	 *
	 * @throws FieldDataInvalidException	if {@code TRACK} field key that contains in the file has non-digit chars
	 * @throws CannotWriteException			if audiofile applying changes fails
	 */
	public void editAlbum() throws FieldDataInvalidException, CannotWriteException {
		
		Album album = fillAlbum();
		Artwork albumCover = null;
		
		try {
			albumCover = findAndCreateArtworkFromFiles(files);
		} catch (IOException _) {}
		
		// Change tags
		for (File file : files) {
			if (file.isFile()) {
				
				try {	// Check if the file is audio else skip that file
					audioFile = AudioFileIO.read(file);
					tag = audioFile.getTag();
				} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
					continue;
				}
				
				String artistToRename = processTag(tag, album);
				
				deleteTrackNumberFromTitle(tag);
				
				if (albumCover != null) {
					tag.setField(albumCover);
				}
				
				audioFile.commit();	// Apply change
				
				renameFile(file, artistToRename);
			}
		}
		
		System.out.print("Done!\n");
	}
	
	/**
	 * Edit one tag field of audiofiles in the directory.
	 *
	 * @throws FieldDataInvalidException	if {@code TRACK} field key that contains in the file has non-digit chars
	 * @throws CannotWriteException			if audiofile applying changes fails
	 */
	public void editDirectory() throws FieldDataInvalidException, CannotWriteException {
		
		// ArrayLists for initial tags and changed tags
		ArrayList<String> initialTags = new ArrayList<>();
		ArrayList<String> changedTags = new ArrayList<>();
		
		int tagIndex = -1;
		
		while (tagIndex < 0 || tagIndex > tags.size()) {
			clearTerminal();
			System.out.print("What tag you want to change?\n");
			for (int i = 1; i <= tags.size(); i++) {
				System.out.printf("%d - %s;\n",i, tags.get(i));
			}
			System.out.print("Tag: ");
			
			try {											// If tag index is not int then keep tagIndex = -1
				tagIndex = terminalInput.nextInt();
			} catch (InputMismatchException e) {
				terminalInput.next();
				tagIndex = -1;
			}
		}
		
		// The nextInt() method previously called doesn't read the following new-line character, so we need
		// to call nextLine() to avoid reading "" in next call of nextLine()
		terminalInput.nextLine();
		
		// Fill tags
		for (File file : files) {
			if (file.isFile()) {
				try {									// If not audio file then continue
					audioFile = AudioFileIO.read(file);
				} catch (Exception e) {
					continue;
				}
				tag = audioFile.getTag();
				
				// Add unique tags to ArrayList of initial tags
				if (!initialTags.contains(tag.getFirst(tags.get(tagIndex)))) {
					initialTags.add(tag.getFirst(tags.get(tagIndex)));
				}
			}
		}
		
		System.out.printf("Found %d unique tags of %s.\n", initialTags.size(), tags.get(tagIndex));
		System.out.print("Type new value for old value. Type 0 if you don't want to replace this tag.\n");
		
		// Change initial genres to another genres
		for (String initialTag : initialTags) {
			System.out.printf("[%d/%d] %s -> ",initialTags.indexOf(initialTag) + 1, initialTags.size(), initialTag);
			String changedTag = terminalInput.nextLine();
			
			if (changedTag.equals("0")) {		// If user don't want to change genre keep this genre
				changedTags.add(initialTag);
			} else {
				changedTags.add(changedTag);
			}
		}
		
		System.out.print("Editing...\n");
		for (File file : files) {
			if (file.isFile()) {
				try {									// If not audio file then continue
					audioFile = AudioFileIO.read(file);
				} catch (Exception e) {
					continue;
				}
				tag = audioFile.getTag();
				
				for (int i = 0; i < initialTags.size(); i++) {							// Go through tags
					if (tag.getFirst(tags.get(tagIndex)).equals(initialTags.get(i))) {	// If tag of the audio file is equal to initial genre
						tag.setField(tags.get(tagIndex), changedTags.get(i));			// Then change the genre of the audio file to the new genre
						audioFile.commit();												// Apply change
					}
				}
			}
		}
		
		System.out.print("Done!\n");
	}
	
	/**
	 * Fix bug when tag field multiplies.
	 */
	public void removeMultiplyTags() {
		
		System.out.print("Removing multiplied tags separated by ';'...\n");
		for (File file : files) {
			if (file.isFile()) {
				try {													// If not audio file then continue
					audioFile = AudioFileIO.read(file);
					tag = audioFile.getTag();
					
					//Get ALBUM_ARTIST field
					Iterator<TagField> it = tag.getFields();
					String albumArtist = null;
					
					while (it.hasNext()) {
						TagField field = (TagField) it.next();
						if (field.getId().equals("ALBUM ARTIST")) {
							albumArtist = field.toString();
						}
					}
					
					// TODO: continue with albumArtist
					
					
					for (FieldKey fieldKey : tags.values()) {
						if (!tag.getFields(fieldKey).isEmpty()) {
							String singleTag = tag.getFirst(fieldKey);		// Read single tag
							while (tag.getFields(fieldKey).isEmpty()) {
								tag.deleteField(fieldKey);                  // Delete tag
							}
							tag.setField(fieldKey, singleTag);				// Set single tag in that field
							audioFile.commit();								// Apply change
						}
					}
				} catch (Exception _) {}
			}
		}
		
		System.out.print("Done!\n");
	}
	
}
