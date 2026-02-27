package Controller;

import Model.AnalysisResult;
import Model.TextAnalyzer;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class MainController {

    @FXML private BorderPane mainBorderPane;
    @FXML private Button newTabButtonMenu, openFileButtonMenu, saveTextButtonMenu, saveAnalysisReportButtonMenu,
            undoButtonMenu, redoButtonMenu, viewButtonMenu, viewFullAnalysisStatsButton;
    @FXML private TabPane tabPane;
    @FXML private Label wordCountData, sentenceCountData, paragraphCountData, statusLabel;

    private int tabCounter = 1;
    private Map<Tab, TextArea> tabTextAreaMap = new HashMap<>();
    private Map<Tab, AnalysisResult> tabAnalysisMap = new HashMap<>();

    public void initialize() {
        createNewTab();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateStatsLabels();
        });
    }

    public void createNewTab(){
        TextArea currTextArea = new TextArea();
        currTextArea.setWrapText(true);
        currTextArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
        pauseTransition.setOnFinished(event -> {
            String currText = currTextArea.getText();
            int wordCountVal = TextAnalyzer.countWords(currText);
            wordCountData.setText(String.valueOf(wordCountVal));
            statusLabel.setText(String.valueOf(wordCountVal));
            paragraphCountData.setText(String.valueOf(TextAnalyzer.paragraphCount(currText)));
            sentenceCountData.setText(String.valueOf(TextAnalyzer.sentenceCount(currText)));
        });
        currTextArea.textProperty().addListener((observable, oldValue, newValue) ->{
            pauseTransition.playFromStart();
        });
        StackPane stackPane = new StackPane(currTextArea);
        Tab currTab = new Tab("Document " + tabCounter++, stackPane);
        Label tabName = new Label(currTab.getText());
        currTab.setText(null);
        currTab.setGraphic(tabName);
        tabName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if (mouseEvent.getClickCount() == 2){
                        TextField textField = new TextField(tabName.getText());
                        currTab.setGraphic(textField);
                        textField.requestFocus();
                        textField.selectAll();
                        textField.setOnAction(event -> {
                            String currName = textField.getText().trim();
                            if (currName.isEmpty()){
                                currName = "Untitled";
                            }
                            Label updatedName = new Label(currName);
                            currTab.setGraphic(updatedName);
                        });
                    }
                }
            }
        });
        tabPane.getTabs().add(currTab);
        tabPane.getSelectionModel().select(currTab);
        tabTextAreaMap.put(currTab, currTextArea);
        currTab.setOnClosed(event -> tabTextAreaMap.remove(currTab));
    }

    public void updateStatsLabels(){
        Tab currTab = tabPane.getSelectionModel().getSelectedItem();
        TextArea currTextArea = tabTextAreaMap.get(currTab);
        String currText = currTextArea.getText();
        int wordCountVal = TextAnalyzer.countWords(currText);
        wordCountData.setText(String.valueOf(wordCountVal));
        statusLabel.setText(String.valueOf(wordCountVal));
        paragraphCountData.setText(String.valueOf(TextAnalyzer.paragraphCount(currText)));
        sentenceCountData.setText(String.valueOf(TextAnalyzer.sentenceCount(currText)));
    }

}
