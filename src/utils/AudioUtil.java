package utils;

/* If it doesn't work then uncomment 5 and 17 lines and delete 18 line */

//import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

public class AudioUtil {
	
	public static boolean isContainAudioFiles(File files[]) {
		boolean retVal = false;
		for (File file : files) {
			if (file.isFile()) {
				try {	// If not a single file is audio then retVal still false
					//AudioFile audioFile = AudioFileIO.read(file);
					AudioFileIO.read(file);
				} catch (Exception e) {
					continue;
				}
				retVal = true;
			}
		}
		return retVal;
	}
}
