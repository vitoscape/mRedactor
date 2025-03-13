package utils;

import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageUtil {
	
	private static final List<String> IMAGE_EXTENSIONS =
			Arrays.asList("jpg", "jpeg", "png");
	
	/**
	 * Check if the {@code file} is image.
	 *
	 * @param file	file to check
	 * @return		{@code true} if the file is image and {@code false} if it is not image
	 */
	private static boolean isImage(File file) {
		
		String name = file.getName().toLowerCase();
		int dotIndex = name.lastIndexOf(".");
		
		if (dotIndex == -1) {
			return false;
		}
		
		String extension = name.substring(dotIndex + 1);
		return IMAGE_EXTENSIONS.contains(extension);
	}
	
	/**
	 * Find image in {@code files} and create {@code Artwork} from this image.
	 *
	 * @param files			files to search for {@code Artwork} in
	 * @return				{@code Artwork} from image in {@code files}
	 * @throws IOException	if there is no image in {@code files}
	 */
	public static Artwork findAndCreateArtworkFromFiles(File[] files) throws IOException {
		
		for (File file : files) {
			if (file.isFile()) {
				try {
					if (isImage(file)) {
						return ArtworkFactory.createArtworkFromFile(file);
					}
				} catch (IOException _) {}
			}
		}
		
		throw new IOException("No images in files");
	}
}
