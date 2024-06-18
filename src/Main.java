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
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.LogManager;

import static java.lang.System.exit;

public class Main {
	
	private static void clearTerminal() {
		// Clear terminal window
		System.out.print("\033[H\033[2J");
		System.out.flush();
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
		
		// Fill tags to change
		System.out.print("Artist: ");
		String artist = terminalInput.nextLine();
		System.out.print("Album: ");
		String album = terminalInput.nextLine();
		System.out.print("Genre: ");
		String genre = terminalInput.nextLine();
		System.out.print("Year: ");
		String year = terminalInput.nextLine();
		
		
		assert files != null;    	// If files contain null
		for (File file : files) {
			if (file.isFile()) {
				AudioFile audioFile;
				Tag tag;
				
				try {
					audioFile = AudioFileIO.read(file);
					tag = audioFile.getTag();
				} catch (CannotReadException e) {
					continue;						// If this file is not audio then continue the iteration
				}
				
				tag.setField(FieldKey.ARTIST, artist);
				tag.setField(FieldKey.ALBUM_ARTIST, artist);
				tag.setField(FieldKey.ALBUM, album);
				tag.setField(FieldKey.GENRE, genre);
				tag.setField(FieldKey.YEAR, year);
				tag.setField(FieldKey.COMMENT, "");	// Remove comment
				
				audioFile.commit();	// Apply change
			}
		}
		
		System.out.print("\nDone!\n");
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
		
		//File directory = new File(" /* PATH TO YOUR DIRECTORY */ ");		// Directory with files
		//File directory = new File(directoryPath);							// Directory with files
		File files[] = directory.listFiles();								// Files array from directory
		
		// Genres
		ArrayList<String> initialGenres = new ArrayList<>();
		ArrayList<String> changedGenres = new ArrayList<>();
		
		// Artists
		ArrayList<String> initialArtists = new ArrayList<>();
		ArrayList<String> changedArtists = new ArrayList<>();
		
		// Filling initial ArrayLists
		assert files != null;    	// If files contain null
		for (File file : files) {
			if (file.isFile()) {
				
				AudioFile audioFile = null;
				try {
					audioFile = AudioFileIO.read(file);
				} catch (CannotReadException e) {										// Sometimes throws this exception with directory with audio files
					//System.out.printf("%s\n", e.getMessage());						// TODO: Program should exit when there is not a single audio file in the directory.
					System.out.print("No audio files in this directory.\n");			// Now program exit when at least one file in the directory is not an audio file
					System.out.print("Program exit.\n");
					exit(1);
				}
				
				Tag tag = audioFile.getTag();
				
				// Add unique genres to ArrayList of initial genres
				if (!initialGenres.contains(tag.getFirst(FieldKey.GENRE))) {
					initialGenres.add(tag.getFirst(FieldKey.GENRE));
				}
				
				// Add unique artists to ArrayList of initial artists
				if (!initialArtists.contains(tag.getFirst(FieldKey.ARTIST))) {
					initialArtists.add(tag.getFirst(FieldKey.ARTIST));
				}
			}
		}
		
		System.out.printf("Initial genres: %s\n", initialGenres);
		System.out.printf("Initial artists: %s\n", initialArtists);
		
		// Change initial genres to another genres
		for (String initialGenre : initialGenres) {
			System.out.printf("What genre do you want to replace %s with? Type 0 if you don't want to replace genre.\n", initialGenre);
			String changedGenre = terminalInput.nextLine();
			
			if (Objects.equals(changedGenre, "0")) {	// If user don't want to change genre keep this genre
				changedGenres.add(initialGenre);
			} else {
				changedGenres.add(changedGenre);
			}
		}
		
		// Change initial artists to another artists
		for (String initialArtist : initialArtists) {
			System.out.printf("What artist do you want to replace %s with? Type 0 if you don't want to replace artist.\n", initialArtist);
			String changedArtist = terminalInput.nextLine();
			
			if (Objects.equals(changedArtist, "0")) {	// If user don't want to change artist keep this artist
				changedArtists.add(initialArtist);
			} else {
				changedArtists.add(changedArtist);
			}
		}
		
		System.out.printf("Changed genres: %s\n", changedGenres);
		System.out.printf("Changed artists: %s\n", changedArtists);
		
		for (File file : files) {
			if (file.isFile()) {
				AudioFile audioFile = AudioFileIO.read(file);
				Tag tag = audioFile.getTag();
				
				for (int i = 0; i < initialGenres.size(); i++) {								// Go through genres
					if (Objects.equals(tag.getFirst(FieldKey.GENRE), initialGenres.get(i))) {	// If the genre of the audio file is equal to initial genre
						tag.setField(FieldKey.GENRE, changedGenres.get(i));						// Then change the genre of the audio file to the new genre
						audioFile.commit();														// Apply change
					}
				}
				
				for (int i = 0; i < initialArtists.size(); i++) {								// Go through artists
					if (Objects.equals(tag.getFirst(FieldKey.ARTIST), initialArtists.get(i))) {	// If artist of the audio file is equal to initial artist
						tag.setField(FieldKey.ARTIST, changedArtists.get(i));					// Then change artist of the audio file to the new artist
						audioFile.commit();														// Apply change
					}
				}
				
				// Print changed genres
				//System.out.printf("File name:\n%s\n", file.getName());
				//System.out.printf("Genre: %s\n\n", tag.getFirst(FieldKey.GENRE));
			}
		}
	}
	
	
	
	public static void main(String args[]) throws TagException, CannotReadException, InvalidAudioFrameException,
			ReadOnlyFileException, IOException, CannotWriteException {
		
		Scanner terminalInput = new Scanner(System.in);	// Input from terminal
		
		LogManager.getLogManager().reset();				// Disable log
		
		
		// Select redacting mode
		String inputMode = null;
		
		while (inputMode == null || (!inputMode.equals("0") && !inputMode.equals("1"))) {
			System.out.print("\033[H\033[2J");			// Clear terminal window
			System.out.flush();
			
			System.out.print("What mode do you want to use?\n0 — edit album;\n1 — edit various audio files in directory.\n");
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
