package utils;

import java.io.File;
import java.util.Scanner;

/** Util class for work with terminal. */
public class TerminalUtil {
	
	/** Clear terminal window. */
	public static void clearTerminal() {
		try {
			final String os = System.getProperty("os.name");
			
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();	// Clear terminal in Windows
			} else {
				System.out.print("\033[H\033[2J");	// ESC-sequence to clear terminal window
				System.out.flush();
			}
		} catch (final Exception _) {}
	}
	
	/**
	 * Input directory path from terminal.
	 *
	 * @param terminalInput {@code Scanner} object to input path
	 * @return array of {@code File}s from directory that has been input
	 */
	public static File[] inputDirectory(Scanner terminalInput) {
		
		String directoryPath = null;
		File directory = null;
		
		while (directoryPath == null || directoryPath.isEmpty() || !directory.isDirectory()) {
			clearTerminal();
			System.out.print("Type path of directory with audio files: ");
			directoryPath = terminalInput.nextLine();
			directory = new File(directoryPath);
		}
		
		return directory.listFiles();
	}
}
