package Controller;

import Model.AnalysisResult;
import Model.WordCountRows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class AnalysisController {

    @FXML private BarChart<String, Number> wordFreqBarChart;
    @FXML private BarChart<String, Number> charFreqBarChart;
    @FXML private BarChart<String, Number> sentenceLengthsHistogram;
    @FXML private Label fleschScore, tabNameLabel;
    @FXML private TableView<WordCountRows> tableView;


    public void loadAnalysis(AnalysisResult analysisResult, String tabName) {
        tabNameLabel.setText(tabName);
        Map<String, Integer> wordFreq = analysisResult.getWordFrequency();
        Map<Character, Integer> charFreq = analysisResult.getCharFrequency();
        List<Integer> sentenceLengths = analysisResult.getSentenceLengths();
        populateWordFrequencyChart(wordFreq);
        populateCharFrequencyChart(charFreq);
        populateSentenceLengthHistogram(sentenceLengths);
        populateWordFrequencyTable(wordFreq);
        populateFleschScore(analysisResult.getFleschReadingEase());
    }

    private void populateWordFrequencyChart(Map<String, Integer> wordFreq) {
        Stream<Map.Entry<String, Integer>> sortedStream = wordFreq.entrySet().stream().
                sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(15);
        XYChart.Series<String, Number> wordFreqSeries = new XYChart.Series<>();
        sortedStream.forEach(entry -> {
            wordFreqSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        });
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
        series.add(wordFreqSeries);
        wordFreqBarChart.getData().setAll(series);
    }

    private void populateCharFrequencyChart(Map<Character, Integer> charFreq) {
        Stream<Map.Entry<Character, Integer>> sortedStream = charFreq.entrySet().stream()
                .sorted(Map.Entry.comparingByKey());
        XYChart.Series<String, Number> charFreqSeries = new XYChart.Series<>();
        sortedStream.forEach(entry -> {
            charFreqSeries.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
        });
        ObservableList<XYChart.Series<String, Number>> observableList = FXCollections.observableArrayList();
        observableList.add(charFreqSeries);
        charFreqBarChart.getData().setAll(observableList);
    }

    private void populateSentenceLengthHistogram(List<Integer> sentenceLengths) {
        // all values for buckets range are inclusive
        int bucket1Count = 0; // 1 - 5
        int bucket2Count = 0; // 6 - 10
        int bucket3Count = 0; // 11 - 15
        int bucket4Count = 0; // 16 - 20
        int bucket5Count = 0; // 21+
        for (Integer sentenceLength : sentenceLengths){
            if (1 <= sentenceLength && sentenceLength <= 5){
                bucket1Count++;
            } else if (6 <= sentenceLength && sentenceLength <= 10){
                bucket2Count++;
            } else if (11 <= sentenceLength && sentenceLength <= 15){
                bucket3Count++;
            } else if (16 <= sentenceLength && sentenceLength <= 20){
                bucket4Count++;
            } else {
                bucket5Count++;
            }
        }
        XYChart.Series<String, Number> sentenceLengthsSeries = new XYChart.Series<>();
        sentenceLengthsSeries.getData().add(new XYChart.Data<>("1 - 5", bucket1Count));
        sentenceLengthsSeries.getData().add(new XYChart.Data<>("6 - 10", bucket2Count));
        sentenceLengthsSeries.getData().add(new XYChart.Data<>("11 - 15", bucket3Count));
        sentenceLengthsSeries.getData().add(new XYChart.Data<>("16 - 20", bucket4Count));
        sentenceLengthsSeries.getData().add(new XYChart.Data<>("21+", bucket5Count));
        ObservableList<XYChart.Series<String, Number>> observableList = FXCollections.observableArrayList();
        observableList.add(sentenceLengthsSeries);
        sentenceLengthsHistogram.getData().setAll(observableList);
    }

    private void populateWordFrequencyTable(Map<String, Integer> wordFreq) {
        ObservableList<WordCountRows> observableList = FXCollections.observableArrayList();
        Stream<Map.Entry<String, Integer>> sortedStream = wordFreq.entrySet().stream().
                sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(15);
        sortedStream.forEach(entry -> {
            WordCountRows currWordCountRow = new WordCountRows(entry.getKey(), entry.getValue());
            observableList.add(currWordCountRow);
        });
        tableView.setItems(observableList);
    }

    private void populateFleschScore(double score) {
        fleschScore.setText(String.valueOf(score));
        if (score >= 70){
            fleschScore.setStyle("-fx-text-fill: lightgreen;");
        } else if (score >= 50 && score < 70){
            fleschScore.setStyle("-fx-text-fill: orange;");
        } else{
            fleschScore.setStyle("-fx-text-fill: red;");
        }
    }

}
