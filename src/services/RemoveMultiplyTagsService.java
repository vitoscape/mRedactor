package services;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class RemoveMultiplyTagsService {
	
	private final File files[];
	
	public RemoveMultiplyTagsService(File files[]) {
		this.files = files;
	}
	
	public void removeMultiplyTags() {
		AudioFile audioFile;
		Tag tag;
		
		System.out.print("Removing multiplied tags separated by ';'...\n");
		for (File file : files) {
			if (file.isFile()) {
				try {													// If not audio file then continue
					audioFile = AudioFileIO.read(file);
					
					tag = audioFile.getTag();
					
					for (FieldKey fieldKey : FieldKey.values()) {
						if (tag.getFields(fieldKey).size() > 1) {
							String singleTag = tag.getFirst(fieldKey);	// Read single tag
							tag.deleteField(fieldKey);					// Delete tag
							tag.setField(fieldKey, singleTag);			// Set single tag in that field
							audioFile.commit();							// Apply change
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		
		System.out.print("Done!\n");
	}
}
