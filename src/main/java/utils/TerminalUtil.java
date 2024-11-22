package utils;

/**
 * Util class for work with terminal.
 */
public class TerminalUtil {
	
	/**
	 * Clear terminal window.
	 */
	public static void clearTerminal() {
		try {
			final String os = System.getProperty("os.name");
			
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();	// Clear terminal in Windows
			} else {
				System.out.print("\033[H\033[2J");	// ESC-sequence to clear terminal window
				System.out.flush();
			}
		} catch (final Exception e) {
			//  Handle any exceptions
		}
	}
}
