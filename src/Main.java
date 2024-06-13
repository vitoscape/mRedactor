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
import java.util.Scanner;
import java.util.logging.LogManager;

public class Main {
	public static void main(String args[]) throws TagException, CannotWriteException, CannotReadException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
		LogManager.getLogManager().reset();	// Disable log
		
		Scanner terminalInput = new Scanner(System.in);		// Input from terminal
		
		System.out.printf("Type directory path: ");
		String directoryPath = terminalInput.nextLine();	// Input directory path
		

		//File directory = new File("C:\\Users\\vit20\\Documents\\test");	// Directory with files
		File directory = new File(directoryPath);							// Directory with files
		File files[] = directory.listFiles();								// Files array from directory
		
		for (File file : files) {
			if (file.isFile()) {
				System.out.printf("File name:\n%s\n", file.getName());
				
				AudioFile audioFile = AudioFileIO.read(file);
				Tag tag = audioFile.getTag();
				
				System.out.printf("Genre: %s\n\n", tag.getFirst(FieldKey.GENRE));
			}
		}



//		AudioFile f = AudioFileIO.read(testFile);		// Чтение аудиофайла
//		Tag tag = f.getTag();							// Получение тегов
//
//		System.out.printf("%s\n", tag.getFirst(FieldKey.GENRE));
//
//		tag.setField(FieldKey.GENRE, "Punk-rock");	// Изменение тега
//		f.commit();										// Подтверждение изменения
//
//		System.out.printf("%s\n", tag.getFirst(FieldKey.GENRE));
	}
}
