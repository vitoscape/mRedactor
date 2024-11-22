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
import static utils.TerminalUtil.inputDirectory;

public class Main {
	
	public static void main(String args[]) throws TagException, CannotWriteException {
		
		Scanner terminalInput = new Scanner(System.in);	// Input from terminal
		
		LogManager.getLogManager().reset();				// Disable log
		
		// Input directory path
		File files[] = inputDirectory(terminalInput);
		
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
