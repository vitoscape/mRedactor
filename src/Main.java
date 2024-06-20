import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.LogManager;

import static java.lang.System.exit;

public class Main {
	
	private static void clearTerminal() {
		// Clear terminal window
		try {
			final String os = System.getProperty("os.name");
			
			if (os.contains("Windows"))
			{
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();	// Works in Windows
			}
			else
			{
				new ProcessBuilder("clear").inheritIO().start().waitFor();			// TODO: Check in other systems
			}
		}
		catch (final Exception e) {
			//  Handle any exceptions.
		}
	}
	
	
	private static void clearLine() {
		System.out.printf("\033[%dA",1);	// Move cursor up by 1 line
		System.out.print("\033[2K");		// Erase the line
	}
	
	
	private static void editAlbum() throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
		
		Scanner terminalInput = new Scanner(System.in);						// Input from terminal
		
		// Input directory path
		String directoryPath = null;
		File directory = null;
		
		while (directoryPath == null || directoryPath.isEmpty() || !directory.isDirectory()) {
			clearTerminal();
			
			System.out.print("Type directory path: ");
			directoryPath = terminalInput.nextLine();                    	// Input directory path
			
			directory = new File(directoryPath);
		}
		
		File files[] = directory.listFiles();								// Files array from directory
		AudioFile audioFile;
		Tag tag;
		
		
		// Check if the directory contains files
		boolean isDirContAudio = false;
		assert files != null;    		// If files contain null
		
		for (File file : files) {
			if (file.isFile()) {
				try {									// If not a single file is audio then isDirContAudio still false
					audioFile = AudioFileIO.read(file);
				} catch (CannotReadException e) {
					continue;
				}
				isDirContAudio = true;
			}
		}
		
		if (!isDirContAudio) {	// If no audio files then return
			System.out.print("Directory does not contain audio files.\n");
			return;
		}
		
		
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
				} catch (CannotReadException e) {
					continue;						// If this file is not audio then continue the iteration
				}
				
				if (!artist.equals("0")) {
					tag.setField(FieldKey.ARTIST, artist);
					tag.setField(FieldKey.ALBUM_ARTIST, artist);
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
			}
		}
		
		System.out.print("Done!\n");
	}
	
	
	
	private static void editDirectory() throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
		
		System.out.print("Editing audio files in directory...\n");
		
		LogManager.getLogManager().reset();	// Disable log
		
		Scanner terminalInput = new Scanner(System.in);						// Input from terminal
		String directoryPath = null;
		File directory = null;
		
		while (directoryPath == null || directoryPath.isEmpty() || !directory.isDirectory()) {
			System.out.print("\033[H\033[2J");								// Clear terminal window
			System.out.flush();
			
			System.out.print("Type directory path: ");
			directoryPath = terminalInput.nextLine();                    	// Input directory path
			
			directory = new File(directoryPath);
		}
		
		File files[] = directory.listFiles();								// Files array from directory
		AudioFile audioFile;
		Tag tag;
		
		// ArrayLists for initial tags and changed tags
		ArrayList<String> initialTags = new ArrayList<>();
		ArrayList<String> changedTags = new ArrayList<>();
		
		String tagToChange = null;
		
		System.out.println();	// Print empty line for correct clearing line in while loop
		
		while (tagToChange == null || (
				!tagToChange.equals("0") &&
				!tagToChange.equals("1") &&
				!tagToChange.equals("2") &&
				!tagToChange.equals("3") &&
				!tagToChange.equals("4"))) {
			
			clearLine();
			
			System.out.print("What tag you want to change?\n");
			System.out.print("1 - genre;\n");
			System.out.print("2 - artist;\n");
			System.out.print("3 - album;\n");
			System.out.print("4 - year;\n");
			System.out.print("0 - exit;\n");
			System.out.print("Tag: ");
			
			tagToChange = terminalInput.next();
		}
		
		// Filling initial ArrayLists
		boolean isDirContAudio = false;
		assert files != null;    		// If files contain null
		
//		for (File file : files) {
//			if (file.isFile()) {
//
//				try {									// If not a single file is audio then isDirContAudio still false
//					audioFile = AudioFileIO.read(file);
//				} catch (CannotReadException e) {
//					continue;
//				}
//				isDirContAudio = true;
//
//				// Add unique genres to ArrayList of initial genres
//				if (!initialGenres.contains(tag.getFirst(FieldKey.GENRE))) {
//					initialGenres.add(tag.getFirst(FieldKey.GENRE));
//				}
//
//				// Add unique artists to ArrayList of initial artists
//				if (!initialArtists.contains(tag.getFirst(FieldKey.ARTIST))) {
//					initialArtists.add(tag.getFirst(FieldKey.ARTIST));
//				}
//			}
//		}
//
//		System.out.printf("Initial genres: %s\n", initialGenres);
//		System.out.printf("Initial artists: %s\n", initialArtists);
//
//		// Change initial genres to another genres
//		for (String initialGenre : initialGenres) {
//			System.out.printf("What genre do you want to replace %s with? Type 0 if you don't want to replace genre.\n", initialGenre);
//			String changedGenre = terminalInput.nextLine();
//
//			if (Objects.equals(changedGenre, "0")) {	// If user don't want to change genre keep this genre
//				changedGenres.add(initialGenre);
//			} else {
//				changedGenres.add(changedGenre);
//			}
//		}
//
//		// Change initial artists to another artists
//		for (String initialArtist : initialArtists) {
//			System.out.printf("What artist do you want to replace %s with? Type 0 if you don't want to replace artist.\n", initialArtist);
//			String changedArtist = terminalInput.nextLine();
//
//			if (Objects.equals(changedArtist, "0")) {	// If user don't want to change artist keep this artist
//				changedArtists.add(initialArtist);
//			} else {
//				changedArtists.add(changedArtist);
//			}
//		}
//
//		System.out.printf("Changed genres: %s\n", changedGenres);
//		System.out.printf("Changed artists: %s\n", changedArtists);
//
//		for (File file : files) {
//			if (file.isFile()) {
//				AudioFile audioFile = AudioFileIO.read(file);
//				Tag tag = audioFile.getTag();
//
//				for (int i = 0; i < initialGenres.size(); i++) {								// Go through genres
//					if (Objects.equals(tag.getFirst(FieldKey.GENRE), initialGenres.get(i))) {	// If the genre of the audio file is equal to initial genre
//						tag.setField(FieldKey.GENRE, changedGenres.get(i));						// Then change the genre of the audio file to the new genre
//						audioFile.commit();														// Apply change
//					}
//				}
//
//				for (int i = 0; i < initialArtists.size(); i++) {								// Go through artists
//					if (Objects.equals(tag.getFirst(FieldKey.ARTIST), initialArtists.get(i))) {	// If artist of the audio file is equal to initial artist
//						tag.setField(FieldKey.ARTIST, changedArtists.get(i));					// Then change artist of the audio file to the new artist
//						audioFile.commit();														// Apply change
//					}
//				}
//
//				// Print changed genres
//				//System.out.printf("File name:\n%s\n", file.getName());
//				//System.out.printf("Genre: %s\n\n", tag.getFirst(FieldKey.GENRE));
//			}
//		}
	}
	
	
	
	public static void main(String args[]) throws TagException, CannotReadException, InvalidAudioFrameException,
			ReadOnlyFileException, IOException, CannotWriteException {
		
		Scanner terminalInput = new Scanner(System.in);	// Input from terminal
		
		LogManager.getLogManager().reset();				// Disable log
		
		
		// Select redacting mode
		String inputMode = null;
		
		while (inputMode == null || (!inputMode.equals("0") && !inputMode.equals("1"))) {
			clearTerminal();
			
			System.out.print("What mode do you want to use?\n0 - edit album;\n1 - edit various audio files in directory.\nMode: ");
			inputMode = terminalInput.next();			// Input mode
		}
		
		if (inputMode.equals("0")) {
			editAlbum();
		} else if (inputMode.equals("1")) {
			editDirectory();
		} else {
			System.out.print("Wrong mode code.\nProgram exit.\n");
			exit(2);
		}
		
	}
}
