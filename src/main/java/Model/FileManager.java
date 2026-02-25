package Model;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class FileManager {

    private FileManager(){}

    public static void saveTextToFile(String text, File file) throws IOException {
        FileWriter currFileWriter = new FileWriter(file, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(currFileWriter);
        bufferedWriter.write(text);
        bufferedWriter.close();
    }

    public static void saveAnalysisReport(AnalysisResult analysisResult, String tabName, File file) throws IOException{
        FileWriter currFileWriter = new FileWriter(file, StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(currFileWriter);
        bufferedWriter.write(tabName);
        bufferedWriter.newLine();
        bufferedWriter.write("Word Count: " + String.valueOf(analysisResult.getWordCount()));
        bufferedWriter.newLine();
        bufferedWriter.write("Character Count Without Spaces: " + analysisResult.getCharCountWithoutSpaces());
        bufferedWriter.newLine();
        bufferedWriter.write("Paragraph Count: " + analysisResult.getParagraphCount());
        bufferedWriter.newLine();
        bufferedWriter.write("Sentence Count: " + analysisResult.getSentenceCount());
        bufferedWriter.newLine();
        bufferedWriter.write("Unique Word Count: " + analysisResult.getUniqueWordCount());
        bufferedWriter.newLine();
        bufferedWriter.write("Average Word Length: " + analysisResult.getAverageWordLength());
        bufferedWriter.newLine();
        bufferedWriter.write("Average Sentence Length: " + analysisResult.getAverageSentenceLength());
        bufferedWriter.newLine();
        bufferedWriter.write("Approximate Reading Time in seconds: " + analysisResult.getReadingTimeSeconds());
        bufferedWriter.newLine();
        bufferedWriter.write("Flesch Reading Score: " + analysisResult.getFleschReadingEase());
        bufferedWriter.newLine();
        bufferedWriter.write("Word Frequency: ");
        bufferedWriter.write(analysisResult.getWordFrequency().toString());
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    public static String loadTextFromFile(File file) throws IOException{
        StringBuffer buffer = new StringBuffer();
        FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String currLine;
        while ((currLine = bufferedReader.readLine()) != null){
            buffer.append(currLine);
            buffer.append("\n");
        }
        bufferedReader.close();
        return buffer.toString();
    }
}
