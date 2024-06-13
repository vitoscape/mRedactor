import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
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

public class Main {
	public static void main(String args[]) throws TagException, CannotReadException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
		
		LogManager.getLogManager().reset();	// Disable log
		
		Scanner terminalInput = new Scanner(System.in);						// Input from terminal
		
		System.out.print("Type directory path: ");
		String directoryPath = terminalInput.nextLine();					// Input directory path
		
		//File directory = new File("C:\\Users\\vit20\\Documents\\test");	// Directory with files
		File directory = new File(directoryPath);							// Directory with files
		File files[] = directory.listFiles();								// Files array from directory
		
		ArrayList<String> initialGenres = new ArrayList<>();
		ArrayList<String> changedGenres = new ArrayList<>();
		
		// Filling initialGenres ArrayList
		assert files != null;	// If files contain null
		for (File file : files) {
			if (file.isFile()) {
				AudioFile audioFile = AudioFileIO.read(file);
				Tag tag = audioFile.getTag();
				//System.out.printf("File name:\n%s\n", file.getName());
				//System.out.printf("Genre: %s\n\n", tag.getFirst(FieldKey.GENRE));
				
				// Add unique genres to ArrayList of initial genres
				if (!initialGenres.contains(tag.getFirst(FieldKey.GENRE))) {
					initialGenres.add(tag.getFirst(FieldKey.GENRE));
				}
			}
		}
		
		System.out.printf("%s\n", initialGenres);

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
		
		System.out.printf("%s\n", changedGenres);
		
		
		
		
		////////////// CHANGE TAG EXAMPLE //////////////
//		AudioFile f = AudioFileIO.read(testFile);
//		Tag tag = f.getTag();
//
//		System.out.printf("%s\n", tag.getFirst(FieldKey.GENRE));
//
//		tag.setField(FieldKey.GENRE, "Punk-rock");		// Change tag
//		f.commit();										// Confirm changes
//
//		System.out.printf("%s\n", tag.getFirst(FieldKey.GENRE));
	}
}
