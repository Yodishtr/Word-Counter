package Model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
                    0, 0, null, null, null);
        }
        String textNormalized = text.toLowerCase().trim();


    }

    // Helpers
    private static int countSyllables(String text){}


}
