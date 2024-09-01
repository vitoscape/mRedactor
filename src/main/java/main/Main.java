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
