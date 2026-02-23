package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextAnalyzerTest {

    @Test
    void nullTextTest(){
        String text = null;
        AnalysisResult result = TextAnalyzer.analyze(text);

        assertEquals(0, result.getWordCount());
        assertEquals(0, result.getCharCountWithoutSpaces());
        assertEquals(0, result.getParagraphCount());
        assertEquals(0, result.getSentenceCount());
        assertEquals(0, result.getUniqueWordCount());

        assertEquals(0.0, result.getAverageWordLength());
        assertEquals(0.0, result.getAverageSentenceLength());
        assertEquals(0.0, result.getReadingTimeSeconds());
        assertEquals(0.0, result.getFleschReadingEase());

        assertEquals(null, result.getWordFrequency());
        Assertions.assertNull(result.getCharFrequency());
        Assertions.assertNull(result.getSentenceLengths());
    }

    @Test
    void blankTextTest(){
        String emptyText = " \n\t";
        AnalysisResult result = TextAnalyzer.analyze(emptyText);

        assertEquals(0, result.getWordCount());
        assertEquals(0, result.getCharCountWithoutSpaces());
        assertEquals(0, result.getParagraphCount());
        assertEquals(0, result.getSentenceCount());
        assertEquals(0, result.getUniqueWordCount());

        assertEquals(0.0, result.getAverageWordLength());
        assertEquals(0.0, result.getAverageSentenceLength());
        assertEquals(0.0, result.getReadingTimeSeconds());
        assertEquals(0.0, result.getFleschReadingEase());

        assertEquals(null, result.getWordFrequency());
        Assertions.assertNull(result.getCharFrequency());
        Assertions.assertNull(result.getSentenceLengths());
    }

    @Test
    void helloWorldTest(){
        String helloWorldText = "Hello World.";
        AnalysisResult result = TextAnalyzer.analyze(helloWorldText);

        assertEquals(2, result.getWordCount(), "word count");
        assertEquals(10, result.getCharCountWithoutSpaces(), "char count with spaces");
        assertEquals(1, result.getParagraphCount(), "paragraph count");
        assertEquals(1, result.getSentenceCount(), "sentence count");
        assertEquals(2, result.getUniqueWordCount(), "unique word count");

        assertEquals(5.0, result.getAverageWordLength());
        assertEquals(2.0, result.getAverageSentenceLength());
        assertEquals(0.6, result.getReadingTimeSeconds());

        // test flesch score range
        double minValue = 0.0;
        double maxValue = 130.0;
        boolean withinRange = (result.getFleschReadingEase() >= minValue && result.getFleschReadingEase() <= maxValue);
        Assertions.assertTrue(withinRange);

        Map<String, Integer> wordFrequencyMap = result.getWordFrequency();
        assertEquals(2, wordFrequencyMap.size());
        assertEquals(1, wordFrequencyMap.get("hello"));
        assertEquals(1, wordFrequencyMap.get("world"));

        Map<Character, Integer> characterFrequencyMap = result.getCharFrequency();
        assertEquals(7, characterFrequencyMap.size());
        assertEquals(1, characterFrequencyMap.get('h'));
        assertEquals(1, characterFrequencyMap.get('e'));
        assertEquals(3, characterFrequencyMap.get('l'));
        assertEquals(2, characterFrequencyMap.get('o'));
        assertEquals(1, characterFrequencyMap.get('w'));
        assertEquals(1, characterFrequencyMap.get('r'));
        assertEquals(1, characterFrequencyMap.get('d'));

        Assertions.assertFalse(characterFrequencyMap.containsKey(' '));
        Assertions.assertFalse(characterFrequencyMap.containsKey('.'));

        List<Integer> sentenceLengthsList = result.getSentenceLengths();
        assertEquals(1, sentenceLengthsList.size());
        assertEquals(2, sentenceLengthsList.get(0));
    }

    @Test
    void criticalLogicTest(){
        String criticalText = "the the cat";
        AnalysisResult result = TextAnalyzer.analyze(criticalText);

        assertEquals(3, result.getWordCount());
        assertEquals(9, result.getCharCountWithoutSpaces());
        assertEquals(1, result.getParagraphCount());
        assertEquals(1, result.getSentenceCount());
        assertEquals(1, result.getUniqueWordCount());

        assertEquals(3.0, result.getAverageWordLength());
        assertEquals(3.0, result.getAverageSentenceLength());
        assertEquals(0.8999999999999999, result.getReadingTimeSeconds());

        double minValue = 0.0;
        double maxValue = 130.0;
        boolean withinRange = (result.getFleschReadingEase() >= minValue && result.getFleschReadingEase() <= maxValue);
        Assertions.assertTrue(withinRange);

        Map<String, Integer> wordFrequencyMap = result.getWordFrequency();
        assertEquals(1, wordFrequencyMap.size());
        assertEquals(1, wordFrequencyMap.get("cat"));
        Assertions.assertFalse(wordFrequencyMap.containsKey(" "));
        Assertions.assertFalse(wordFrequencyMap.containsKey("the"));

        Map<Character, Integer> characterFrequencyMap = result.getCharFrequency();
        assertEquals(3, characterFrequencyMap.size());
        assertEquals(1, characterFrequencyMap.get('c'));
        assertEquals(1, characterFrequencyMap.get('a'));
        assertEquals(1, characterFrequencyMap.get('t'));

        List<Integer> sentenceLengthsList = result.getSentenceLengths();
        assertEquals(1, sentenceLengthsList.size());
        assertEquals(3, sentenceLengthsList.get(0));

    }


}
