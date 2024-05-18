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

public class Main {
	public static void main(String args[]) throws TagException, CannotWriteException, CannotReadException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
		
		File folder = new File("C:\\Users\\vit20\\Documents\\test");	// Директория с файлами
		File files[] = folder.listFiles();										// Массив с файлами из директории
		
		for (File file : files) {
			if (file.isFile()) {
				System.out.printf("File name:\n%s\n", file.getName());
				
				AudioFile audioFile = AudioFileIO.read(file);			// Чтение аудиофайла
				Tag tag = audioFile.getTag();							// Получение тега
				
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
