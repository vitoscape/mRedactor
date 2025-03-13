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
	 * @return	{@code true} if directory contains at least one audiofile and<p>
	 * 			{@code false} if directory does not contain audiofiles.
	 */
	public static boolean isContainingAudioFiles(File files[]) {
		
		boolean returnValue = false;
		
		for (File file : files) {
			if (canRead(file)) {
				returnValue = true;
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Check if the {@code file} is file and can it be read as audiofile.
	 *
	 * @param file	the file to check
	 * @return		{@code true} if the {@code file} if file, and it can be read as audiofile<p>
	 * 				{@code false} if the {@code file} isn't file, or it can't be read as audiofile
	 */
	private static boolean canRead(File file) {
		
		if (!file.isFile()) {
			return false;
		}
		
		try {
			AudioFileIO.read(file);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
