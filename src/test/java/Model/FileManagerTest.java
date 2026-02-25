package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileManagerTest {

    @TempDir
    Path path;
    File tempFileRead;
    File tempFileWrite;

    @BeforeEach
    public void setUp(){
        tempFileRead = path.resolve("tempFileRead.txt").toFile();
        tempFileWrite = path.resolve("tempFileWrite.txt").toFile();
    }

    @Test
    public void saveTextToFileTest() throws IOException {
        String text = "Hello World";
        FileManager.saveTextToFile(text, tempFileWrite);
        String fileContent = Files.readString(tempFileWrite.toPath());
        assertEquals(text, fileContent);
    }

    @Test
    public void saveTextToFileUnicodeTest() throws IOException {
        String unicodeText = "é, 漢字, 🇿🇼";
        FileManager.saveTextToFile(unicodeText, tempFileWrite);
        String fileContent = Files.readString(tempFileWrite.toPath());
        assertEquals(unicodeText, fileContent);
    }

    @Test
    public void loadTextFromFileTest() throws IOException {
        Files.writeString(tempFileRead.toPath(), "A\nB");
        String text = FileManager.loadTextFromFile(tempFileRead);
        assertEquals("A\nB\n", text);
    }

    @Test
    public void loadtextFromFileMissingTest(){
        File missingFile = path.resolve("missingFile.txt").toFile();
        assertThrows(IOException.class, () -> {
            FileManager.loadTextFromFile(missingFile);
        });
    }

}
