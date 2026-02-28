package Model;

public class WordCountRows {
    private String words;
    private int count;

    public WordCountRows(String words, int count) {
        this.words = words;
        this.count = count;
    }


    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
