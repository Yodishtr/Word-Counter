package Model;

import java.io.File;
import java.io.IOException;

public final class FileManager {

    private FileManager(){}

    public static void saveTextToFile(String text, File file) throws IOException {}

    public static void saveAnalysisReport(AnalysisResult analysisResult, String tabName, File file) throws IOException{}

    public static void loadTextFromFile(File file) throws IOException{}
}
