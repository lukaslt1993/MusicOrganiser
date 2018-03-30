package musicorganiser;

import java.io.File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class MusicOrganiser {

    static final String WORKING_DIR = "F:/openbox recover/mp3";

    static boolean isMatchingExtension(File f, String... extensions) {
        String fileName = f.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        String extension = fileName.substring(lastDotIndex + 1);
        for (String s : extensions) {
            if (s.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    static String sanitise(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    static String generateDirName(File f) throws Exception {
        AudioFile af = AudioFileIO.read(f);
        Tag tag = af.getTag();
        if (tag != null) {
            String album = sanitise(tag.getFirst(FieldKey.ALBUM));
            if (!album.isEmpty()) {
                return WORKING_DIR + '/' + album;
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        for (File f : new File(WORKING_DIR).listFiles()) {
            if (f.isFile()) {
                if (isMatchingExtension(f, "flac", "m4a", "aac", "mp3")) {
                    String generatedDir = generateDirName(f);
                    if (!generatedDir.isEmpty()) {
                        new File(generatedDir).mkdirs();
                        f.renameTo(new File(generatedDir + '/' + f.getName()));
                    }
                }
            }
        }
    }
}
