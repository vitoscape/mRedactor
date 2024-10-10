package utils;

import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

public class AudioUtil {
	
	public static boolean isContainAudioFiles(File files[]) {
		boolean retVal = false;
		for (File file : files) {
			if (file.isFile()) {
				try {											// If not a single file is audio then retVal still false
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
