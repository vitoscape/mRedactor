package services;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import static utils.TerminalUtil.clearTerminal;

public class EditDirectoryService {
	
	private final Scanner terminalInput = new Scanner(System.in);
	
	private static final HashMap<Integer, FieldKey> tags = new HashMap<>();	// HashMap to store keys for editable tags
	
	private final File files[];
	
	public EditDirectoryService(File[] files) {
		this.files = files;
		
		// Fill tags HashMap
		tags.put(1, FieldKey.GENRE);
		tags.put(2, FieldKey.ARTIST);
		tags.put(3, FieldKey.ALBUM);
		tags.put(4, FieldKey.ALBUM_ARTIST);
		tags.put(5, FieldKey.YEAR);
	}
	
	
	public void editDirectory() throws FieldDataInvalidException, CannotWriteException {
		
		AudioFile audioFile;
		Tag tag;
		
		
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
}
