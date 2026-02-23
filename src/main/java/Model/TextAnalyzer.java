package Model;

import java.util.*;

public final class TextAnalyzer {

    // Stop words in English
    private static final Set<String> stopWords = new HashSet<>(Arrays.asList(
            "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves",
            "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them",
            "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am",
            "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did",
            "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by",
            "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above",
            "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further",
            "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few",
            "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too",
            "very", "s", "t", "can", "will", "just", "don", "should", "now"
    ));

    private TextAnalyzer(){}

    public static AnalysisResult analyze(String text){
        if (text == null || text.length() == 0){
            return new AnalysisResult(0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, null, null, null);
        }
        int paragraphCount = 0;
        String textNormalized = text.toLowerCase().trim();
        textNormalized = textNormalized.replace("\r\n", "\n");
        textNormalized = textNormalized.replace("\r", "\n");
        ArrayList<String> allParagraphsArray = new ArrayList<>(Arrays.asList(textNormalized.split("\\n\\s*\\n")));
        ArrayList<String> textParagraphsArray = new ArrayList<>();
        for (String paragraph : allParagraphsArray){
            paragraph = paragraph.trim();
            if (!paragraph.isEmpty()){
                paragraphCount++;
                textParagraphsArray.add(paragraph);
            }
        }

        ArrayList<String> sentencesArray = new ArrayList<>();
        for (String txtparagraph : textParagraphsArray){
            String[] sentences = txtparagraph.split("[.!?]+\\s+");
            String[] trimmedSentences = new String[sentences.length];
            for (int i = 0; i < sentences.length; i++){
                String currSentence = sentences[i].trim();
                if (!currSentence.isEmpty()){
                    trimmedSentences[i] = currSentence;
                }
            }
            Collections.addAll(sentencesArray, trimmedSentences);
        }

        int characterCountWithSpaces = 0;
        ArrayList<String> wordList = new ArrayList<>();
        for (String sentence : sentencesArray){
            String[] words = sentence.split("\\s+");
            for (String currWord : words){
                currWord = currWord.trim();
                if (!currWord.isEmpty() && currWord.matches(".*[a-zA-Z0-9]+.*")){
                    currWord = currWord.replaceAll("^[^a-zA-Z0-9]+", "");
                    currWord = currWord.replaceAll("[^a-zA-Z0-9]+$", "");
                    characterCountWithSpaces += currWord.length();
                    wordList.add(currWord);
                }
            }
        }
        int wordCount = wordList.size();
        double averageWordLength = 0.0;
        int totalWordLength = 0;
        for (String word : wordList){
            totalWordLength += word.length();
        }
        if (wordList.size() != 0){
            averageWordLength = (double)totalWordLength / wordList.size();
        }
        HashSet<String> uniqueWords = new HashSet<>();
        for (String uniqueWord : wordList){
            if (!stopWords.contains(uniqueWord)){
                uniqueWords.add(uniqueWord);
            }
        }
        int uniqueWordCount = uniqueWords.size();
        HashMap<String, Integer> wordFreqMap = new HashMap<>();
        for (String currWord : wordList){
            if (!stopWords.contains(currWord) && wordFreqMap.containsKey(currWord)){
                wordFreqMap.put(currWord, wordFreqMap.get(currWord) + 1);
            } else if (!stopWords.contains(currWord) && !wordFreqMap.containsKey(currWord)){
                wordFreqMap.put(currWord, 1);
            }
        }

        HashMap<Character, Integer> characterFreqMap = new HashMap<>();
        for (String currWord : wordList) {
            if (!stopWords.contains(currWord)) {
                for (int i = 0; i < currWord.length(); i++) {
                    Character c = currWord.charAt(i);
                    if (characterFreqMap.containsKey(c)) {
                        characterFreqMap.put(c, characterFreqMap.get(c) + 1);
                    } else {
                        characterFreqMap.put(c, 1);
                    }
                }
            }
        }
        ArrayList<Integer> sentenceLengths = new ArrayList<>();
        for (String sentence : sentencesArray){
            String[] wordsInSentence = sentence.split("\\s+");
            sentenceLengths.add(wordsInSentence.length);
        }

        int totalSentenceLengths = sentenceLengths.stream().mapToInt(Integer::intValue).sum();
        int numberOfSentences = sentenceLengths.size();
        double averageSentenceLength = (double)totalSentenceLengths / numberOfSentences;
        double readingTime = wordCount / (200.0 * 60);
        double fleschReadingEase = 206.835 - (1.015 * averageSentenceLength) - (84.6 * countSyllables(wordList));

        return new AnalysisResult(wordCount, characterCountWithSpaces, paragraphCount, numberOfSentences,
                uniqueWordCount, averageWordLength, averageSentenceLength, readingTime, fleschReadingEase, wordFreqMap,
                characterFreqMap, sentenceLengths);

    }

    // Helpers
    private static double countSyllables(ArrayList<String> text){
        Set<String> vowels = new HashSet<>(Arrays.asList("a", "e", "i", "o", "u", "y"));
        int totalSyllables = 0;
        for (String word : text){
            char[] charArray = word.toCharArray();
            int syllableCount = 0;
            boolean previousVowel = false;
            for (int i = 0; i < charArray.length; i++){
                char currChar = charArray[i];
                if (vowels.contains(Character.toString(currChar))){
                    if (!previousVowel){
                        previousVowel = true;
                        syllableCount++;
                    }
                } else {
                    previousVowel = false;
                }
            }

            if (word.endsWith("e")){
                boolean hasConsonantLeEnding = word.endsWith("le") && word.length() >= 3 &&
                        !vowels.contains(Character.toString(word.charAt(word.length() - 3)));
                if (!hasConsonantLeEnding && syllableCount > 1){
                    syllableCount--;
                }
            }
            if (syllableCount < 1){
                syllableCount = 1;
            }

            totalSyllables += syllableCount;
        }
        double averageSyllables = (double) totalSyllables / text.size();
        return averageSyllables;

    }


}
