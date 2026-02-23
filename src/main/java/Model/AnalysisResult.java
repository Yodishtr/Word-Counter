package Model;

import java.util.List;
import java.util.Map;

public class AnalysisResult {

    // Basic Counts
    private final int wordCount;
    private final int charCountWithoutSpaces;
    private final int paragraphCount;
    private final int sentenceCount;
    private final int uniqueWordCount;

    // Derived Stats
    private final double averageWordLength;
    private final double averageSentenceLength; // average number of words per sentence
    private final double readingTimeSeconds; // compute from word count / reading speed (200 wpm)
    private final double fleschReadingEase; // readability score computed in the analyzer

    // charts data
    private final Map<String, Integer> wordFrequency; //maps each word to how many times it appears
    private final Map<Character, Integer> charFrequency; //maps each letter to its a-z count
    private final List<Integer> sentenceLengths; // a list where each element is the word count of one sentence


    public AnalysisResult(int wordCount, int charCountWithoutSpaces, int paragraphCount,
                          int sentenceCount, int uniqueWordCount, double averageWordLength, double averageSentenceLength,
                          double readingTimeSeconds, double fleschReadingEase,
                          Map<String, Integer> wordFrequency, Map<Character, Integer> charFrequency,
                          List<Integer> sentenceLengths) {
        this.wordCount = wordCount;
        this.charCountWithoutSpaces = charCountWithoutSpaces;
        //this.charCountWithoutSpaces = charCountWithoutSpaces;
        this.paragraphCount = paragraphCount;
        this.sentenceCount = sentenceCount;
        this.uniqueWordCount = uniqueWordCount;
        this.averageWordLength = averageWordLength;
        this.averageSentenceLength = averageSentenceLength;
        this.readingTimeSeconds = readingTimeSeconds;
        this.fleschReadingEase = fleschReadingEase;
        this.wordFrequency = wordFrequency;
        this.charFrequency = charFrequency;
        this.sentenceLengths = sentenceLengths;
    }

    // Getters
    public int getWordCount(){
        return wordCount;
    }

    public int getCharCountWithoutSpaces(){
        return charCountWithoutSpaces;
    }


    public int getParagraphCount(){
        return paragraphCount;
    }

    public int getSentenceCount(){
        return sentenceCount;
    }

    public int getUniqueWordCount(){
        return uniqueWordCount;
    }

    public double getAverageWordLength(){
        return averageWordLength;
    }

    public double getAverageSentenceLength(){
        return averageSentenceLength;
    }

    public double getReadingTimeSeconds(){
        return readingTimeSeconds;
    }

    public double getFleschReadingEase(){
        return fleschReadingEase;
    }

    public Map<String, Integer> getWordFrequency(){
        return wordFrequency;
    }

    public Map<Character, Integer> getCharFrequency(){
        return charFrequency;
    }

    public List<Integer> getSentenceLengths(){
        return sentenceLengths;
    }

}
