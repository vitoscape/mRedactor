package utils;

import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Audio util class that contains static util methods.
 */
public class AudioUtil {
	
	/**
	 * Check if directory ({@code File} array) contains audiofiles.
	 *
	 * @param files directory as {@code File} array
	 * @return	{@code true} if directory contains at least one audiofile and
	 * 			<p>
	 * 			{@code false} if directory does not contain audiofiles.
	 */
	public static boolean isContainAudioFiles(File files[]) {
		
		boolean returnValue = false;
		
		for (File file : files) {
			if (file.isFile()) {
				try {
					AudioFileIO.read(file);
				} catch (Exception e) {
					continue;
				}
				returnValue = true;
			}
		}
		return returnValue;
	}
}
