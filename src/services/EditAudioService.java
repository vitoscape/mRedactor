package services;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import static utils.TerminalUtil.clearTerminal;

public class EditAudioService {
	
	private final Scanner terminalInput = new Scanner(System.in);
	
	private static final HashMap<Integer, FieldKey> tags = new HashMap<>();	// HashMap to store keys for editable tags
	
	private final File files[];
	
	private AudioFile audioFile;
	private Tag tag;
	
	// Fill tags HashMap
	static {
		tags.put(1, FieldKey.GENRE);
		tags.put(2, FieldKey.ARTIST);
		tags.put(3, FieldKey.ALBUM);
		tags.put(4, FieldKey.ALBUM_ARTIST);
		tags.put(5, FieldKey.YEAR);
	}
	
	
	public EditAudioService(File files[]) {
		this.files = files;
	}
	
	
	public void editAlbum() throws FieldDataInvalidException, CannotWriteException {
		
		// Fill tags to change
		System.out.print("Enter tags that you want to set up. Type 0 if you want to keep tag.\n");
		System.out.print("Artist: ");
		String artist = terminalInput.nextLine();
		System.out.print("Album: ");
		String album = terminalInput.nextLine();
		System.out.print("Genre: ");
		String genre = terminalInput.nextLine();
		System.out.print("Year: ");
		String year = terminalInput.nextLine();
		System.out.print("Number of tracks: ");
		String trackTotal = terminalInput.nextLine();
		
		// Change tags
		for (File file : files) {
			if (file.isFile()) {
				try {
					audioFile = AudioFileIO.read(file);
					tag = audioFile.getTag();
				} catch (CannotReadException | IOException | TagException | ReadOnlyFileException |
						 InvalidAudioFrameException e) {
					continue;						// If this file is not audio then continue the iteration
				}
				
				if (!artist.equals("0")) {
					tag.setField(FieldKey.ARTIST, artist);
					tag.setField(FieldKey.ALBUM_ARTIST, artist);
				} else {
					artist = tag.getFirst(FieldKey.ARTIST);			// If changing tag value doesn't need then read tag value from file to rename file
				}
				if (!album.equals("0")) {
					tag.setField(FieldKey.ALBUM, album);
				}
				if (!genre.equals("0")) {
					tag.setField(FieldKey.GENRE, genre);
				}
				if (!year.equals("0")) {
					tag.setField(FieldKey.YEAR, year);
				}
				if (!trackTotal.equals("0")) {
					tag.setField(FieldKey.TRACK_TOTAL, trackTotal);
				}
				tag.setField(FieldKey.COMMENT, "");	// Remove comment
				
				// Edit title (remove track number from title)
				String title = tag.getFirst(FieldKey.TITLE);
				int firstSpaceIndex = title.indexOf(" ");
				
				if (firstSpaceIndex > 0) {
					String subString = title.substring(0, firstSpaceIndex);
					try {
						if (Integer.parseInt(subString) == Integer.parseInt(tag.getFirst(FieldKey.TRACK))) {
							tag.setField(FieldKey.TITLE, title.substring(firstSpaceIndex + 1));
						}
					} catch (NumberFormatException _) {
					
					}
				}
				
				
				audioFile.commit();	// Apply change
				
				
				// Rename file
				String fileName = file.getName();
				int dotIndex = fileName.lastIndexOf('.');								// Get index of last  '.' char to get extension
				String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);	// Get extension
				Path dirPath = Paths.get(file.getPath()).getParent();
				String newName = artist + " - " + tag.getFirst(FieldKey.TITLE) + extension;	// Create name of file
				
				// If new name contains forbidden characters for files then delete these characters
				if (newName.matches(".*[<>\"/\\\\|?*:].*")) {
					newName = newName.replace("<", "");
					newName = newName.replace(">", "");
					newName = newName.replace("\"", "");
					newName = newName.replace("/", "");
					newName = newName.replace("\\", "");
					newName = newName.replace("|", "");
					newName = newName.replace("?", "");
					newName = newName.replace("*", "");
					newName = newName.replace(":", "");
				}
				
				String newPathName = dirPath + "\\" + newName;								// And finally create new full path name
				
				if (!file.renameTo(new File(newPathName))) {
					System.out.printf("Error when renaming file: %s\n", fileName);
				}
			}
		}
		
		System.out.print("Done!\n");
	}
	
	
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
		
		System.out.print("Done.\n");
	}
	
	
	public void removeMultiplyTags() {
		
		System.out.print("Removing multiplied tags separated by ';'...\n");
		for (File file : files) {
			if (file.isFile()) {
				try {													// If not audio file then continue
					audioFile = AudioFileIO.read(file);
					
					tag = audioFile.getTag();
					
					for (FieldKey fieldKey : FieldKey.values()) {
						if (tag.getFields(fieldKey).size() > 1) {
							String singleTag = tag.getFirst(fieldKey);	// Read single tag
							tag.deleteField(fieldKey);					// Delete tag
							tag.setField(fieldKey, singleTag);			// Set single tag in that field
							audioFile.commit();							// Apply change
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		
		System.out.print("Done!\n");
	}
	
}
