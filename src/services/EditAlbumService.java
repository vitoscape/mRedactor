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
import java.util.Scanner;


public class EditAlbumService {
	
	private final Scanner terminalInput = new Scanner(System.in);
	
	private final File[] files;
	
	public EditAlbumService(File files[]) {
		this.files = files;
	}
	
	public void editAlbum() throws FieldDataInvalidException, CannotWriteException {
		AudioFile audioFile;
		Tag tag;
		
		
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
}
