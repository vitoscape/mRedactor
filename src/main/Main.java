package main;

import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.TagException;
import services.EditAudioService;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.LogManager;

import static java.lang.System.exit;
import static utils.AudioUtil.isContainAudioFiles;
import static utils.TerminalUtil.clearTerminal;

public class Main {
	
//	private static void editAlbum(File files[]) throws TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
//
//		Scanner terminalInput = new Scanner(System.in);						// Input from terminal
//
//		AudioFile audioFile;
//		Tag tag;
//
//
//		// Fill tags to change
//		System.out.print("Enter tags that you want to set up. Type 0 if you want to keep tag.\n");
//		System.out.print("Artist: ");
//		String artist = terminalInput.nextLine();
//		System.out.print("Album: ");
//		String album = terminalInput.nextLine();
//		System.out.print("Genre: ");
//		String genre = terminalInput.nextLine();
//		System.out.print("Year: ");
//		String year = terminalInput.nextLine();
//		System.out.print("Number of tracks: ");
//		String trackTotal = terminalInput.nextLine();
//
//		// Change tags
//		for (File file : files) {
//			if (file.isFile()) {
//				try {
//					audioFile = AudioFileIO.read(file);
//					tag = audioFile.getTag();
//				} catch (CannotReadException e) {
//					continue;						// If this file is not audio then continue the iteration
//				}
//
//				if (!artist.equals("0")) {
//					tag.setField(FieldKey.ARTIST, artist);
//					tag.setField(FieldKey.ALBUM_ARTIST, artist);
//				} else {
//					artist = tag.getFirst(FieldKey.ARTIST);			// If changing tag value doesn't need then read tag value from file to rename file
//				}
//				if (!album.equals("0")) {
//					tag.setField(FieldKey.ALBUM, album);
//				}
//				if (!genre.equals("0")) {
//					tag.setField(FieldKey.GENRE, genre);
//				}
//				if (!year.equals("0")) {
//					tag.setField(FieldKey.YEAR, year);
//				}
//				if (!trackTotal.equals("0")) {
//					tag.setField(FieldKey.TRACK_TOTAL, trackTotal);
//				}
//				tag.setField(FieldKey.COMMENT, "");	// Remove comment
//
//				audioFile.commit();	// Apply change
//
//
//				// Rename file
//				String fileName = file.getName();
//				int dotIndex = fileName.lastIndexOf('.');								// Get index of last  '.' char to get extension
//				String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);	// Get extension
//				Path dirPath = Paths.get(file.getPath()).getParent();
//				String newName = artist + " - " + tag.getFirst(FieldKey.TITLE) + extension;	// Create name of file
//
//				// If new name contains forbidden characters for files then delete these characters
//				if (newName.matches(".*[<>\"/\\\\|?*:].*")) {
//					newName = newName.replace("<", "");
//					newName = newName.replace(">", "");
//					newName = newName.replace("\"", "");
//					newName = newName.replace("/", "");
//					newName = newName.replace("\\", "");
//					newName = newName.replace("|", "");
//					newName = newName.replace("?", "");
//					newName = newName.replace("*", "");
//					newName = newName.replace(":", "");
//				}
//
//				String newPathName = dirPath + "\\" + newName;								// And finally create new full path name
//
//				if (!file.renameTo(new File(newPathName))) {
//					System.out.printf("Error when renaming file: %s\n", fileName);
//				}
//			}
//		}
//
//		System.out.print("Done!\n");
//	}
	
	
//	private static void editDirectory(File files[]) throws TagException, CannotWriteException {
//
//		Scanner terminalInput = new Scanner(System.in);						// Input from terminal
//
//		AudioFile audioFile;
//		Tag tag;
//
//
//		// ArrayLists for initial tags and changed tags
//		ArrayList<String> initialTags = new ArrayList<>();
//		ArrayList<String> changedTags = new ArrayList<>();
//
//		int tagIndex = -1;
//
//		while (tagIndex < 0 || tagIndex > tags.size()) {
//			clearTerminal();
//			System.out.print("What tag you want to change?\n");
//			for (int i = 1; i <= tags.size(); i++) {
//				System.out.printf("%d - %s;\n",i, tags.get(i));
//			}
//			System.out.print("Tag: ");
//
//			try {											// If tag index is not int then keep tagIndex = -1
//				tagIndex = terminalInput.nextInt();
//			} catch (InputMismatchException e) {
//				terminalInput.next();
//				tagIndex = -1;
//			}
//		}
//
//		// The nextInt() method previously called doesn't read the following new-line character, so we need
//		// to call nextLine() to avoid reading "" in next call of nextLine()
//		terminalInput.nextLine();
//
//		// Fill tags
//		for (File file : files) {
//			if (file.isFile()) {
//				try {									// If not audio file then continue
//					audioFile = AudioFileIO.read(file);
//				} catch (Exception e) {
//					continue;
//				}
//				tag = audioFile.getTag();
//
//				// Add unique tags to ArrayList of initial tags
//				if (!initialTags.contains(tag.getFirst(tags.get(tagIndex)))) {
//					initialTags.add(tag.getFirst(tags.get(tagIndex)));
//				}
//			}
//		}
//
//		System.out.printf("Found %d unique tags of %s.\n", initialTags.size(), tags.get(tagIndex));
//		System.out.print("Type new value for old value. Type 0 if you don't want to replace this tag.\n");
//
//		// Change initial genres to another genres
//		for (String initialTag : initialTags) {
//			System.out.printf("[%d/%d] %s -> ",initialTags.indexOf(initialTag) + 1, initialTags.size(), initialTag);
//			String changedTag = terminalInput.nextLine();
//
//			if (changedTag.equals("0")) {		// If user don't want to change genre keep this genre
//				changedTags.add(initialTag);
//			} else {
//				changedTags.add(changedTag);
//			}
//		}
//
//		System.out.print("Editing...\n");
//		for (File file : files) {
//			if (file.isFile()) {
//				try {									// If not audio file then continue
//					audioFile = AudioFileIO.read(file);
//				} catch (Exception e) {
//					continue;
//				}
//				tag = audioFile.getTag();
//
//				for (int i = 0; i < initialTags.size(); i++) {							// Go through tags
//					if (tag.getFirst(tags.get(tagIndex)).equals(initialTags.get(i))) {	// If tag of the audio file is equal to initial genre
//						tag.setField(tags.get(tagIndex), changedTags.get(i));			// Then change the genre of the audio file to the new genre
//						audioFile.commit();												// Apply change
//					}
//				}
//			}
//		}
//
//		System.out.print("Done.\n");
//	}
	

//	private static void removeMultiplyTags(File files[]) {
//
//		AudioFile audioFile;
//		Tag tag;
//
//		System.out.print("Removing multiplied tags separated by ';'...\n");
//		for (File file : files) {
//			if (file.isFile()) {
//				try {									// If not audio file then continue
//					audioFile = AudioFileIO.read(file);
//
//					tag = audioFile.getTag();
//
//					for (FieldKey fieldKey : FieldKey.values()) {
//						if (tag.getFields(fieldKey).size() > 1) {
//							String singleTag = tag.getFirst(fieldKey);	// Read single tag
//							tag.deleteField(fieldKey);					// Delete tag
//							tag.setField(fieldKey, singleTag);			// Set single tag in that field
//							audioFile.commit();							// Apply change
//						}
//					}
//				} catch (Exception e) {
//					continue;
//				}
//			}
//		}
//
//		System.out.print("Done!\n");
//	}
	
	
	public static void main(String args[]) throws TagException, CannotWriteException {
		
		Scanner terminalInput = new Scanner(System.in);	// Input from terminal
		
		LogManager.getLogManager().reset();				// Disable log
		
		// Input directory path
		String directoryPath = null;
		File directory = null;
		
		while (directoryPath == null || directoryPath.isEmpty() || !directory.isDirectory()) {
			clearTerminal();
			System.out.print("Type path of directory with audio files: ");
			directoryPath = terminalInput.nextLine();                    	// Input directory path
			directory = new File(directoryPath);
		}
		
		File files[] = directory.listFiles();	// Files array from directory
		assert files != null;    				// If files contain null
		
		// Check if the directory contains audio files
		System.out.print("Checking files in directory...\n");
		if (!isContainAudioFiles(files)) {
			System.out.print("Directory does not contain audio files.\n");
			return;
		}
		
		
		// Select redacting mode
		int inputMode = -1;
		
		while (inputMode < 0 || inputMode > 2) {
			clearTerminal();
			
			System.out.printf("Type path of directory with audio files: %s\n", directoryPath);
			System.out.print("What mode do you want to use?\n");
			System.out.print("0 - edit album;\n1 - edit various audio files in directory;\n");
			System.out.print("2 - remove multiplied tags separated by ';'.\nMode: ");
			try {
				inputMode = terminalInput.nextInt();	// Input mode
			} catch (InputMismatchException e) {		// If mode is not int then keep inputMode = -1
				terminalInput.next();
				inputMode = -1;
			}
		}
		
		EditAudioService editAudioService = new EditAudioService(files);
		
		if (inputMode == 0) {
			editAudioService.editAlbum();
		} else if (inputMode == 1) {
			editAudioService.editDirectory();
		} else if (inputMode == 2) {
			editAudioService.removeMultiplyTags();
		} else {														// Keep this if new mode will be added
			System.out.print("Wrong mode code.\nProgram exit.\n");
			exit(2);
		}
		
	}
}
