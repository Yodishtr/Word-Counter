package Controller;

import Model.AnalysisResult;
import Model.FileManager;
import Model.TextAnalyzer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
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
    private Stage currStage;
    private Map<Tab, AnalysisResult> tabAnalysisMap = new HashMap<>();

    public void initialize() {
        createNewTab();
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateStatsLabels();
            } else {
                wordCountData.setText(String.valueOf(0));
                statusLabel.setText(String.valueOf(0));
                paragraphCountData.setText(String.valueOf(0));
                sentenceCountData.setText(String.valueOf(0));
            }

        });
        tabPane.sceneProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue != null){
                newValue.windowProperty().addListener((obs, oldWindow, newWindow) ->{
                    if (newWindow != null){
                        currStage = (Stage) newWindow;
                    }
                });
            }
        });
        tabPane.getTabs().addListener((ListChangeListener<Tab>) c -> {
            if (c.getList().isEmpty()){
                Platform.runLater(() -> {
                    createNewTab();
                });

            }
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
        String defaultName = "Document " + tabCounter++;
        Tab currTab = new Tab(defaultName, stackPane);
        currTab.setUserData(defaultName);
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
                            tabName.setText(currName);
                            currTab.setUserData(currName);
                            currTab.setGraphic(tabName);
                        });
                    }
                }
            }
        });

        tabPane.getTabs().add(currTab);
        tabTextAreaMap.put(currTab, currTextArea);
        tabPane.getSelectionModel().select(currTab);
        currTab.setOnClosed(event -> {
            tabTextAreaMap.remove(currTab);
        });
    }

    public void updateStatsLabels(){
        Tab currTab = tabPane.getSelectionModel().getSelectedItem();
        if (currTab == null){
            return;
        }
        TextArea currTextArea = tabTextAreaMap.get(currTab);
        if (currTextArea == null){
            wordCountData.setText(String.valueOf(0));
            statusLabel.setText(String.valueOf(0));
            paragraphCountData.setText(String.valueOf(0));
            sentenceCountData.setText(String.valueOf(0));
            return;
        }
        String currText = currTextArea.getText();
        int wordCountVal = TextAnalyzer.countWords(currText);
        wordCountData.setText(String.valueOf(wordCountVal));
        statusLabel.setText(String.valueOf(wordCountVal));
        paragraphCountData.setText(String.valueOf(TextAnalyzer.paragraphCount(currText)));
        sentenceCountData.setText(String.valueOf(TextAnalyzer.sentenceCount(currText)));
    }

    @FXML
    private void handleNewTabAction(ActionEvent event){
        createNewTab();
    }

    @FXML
    private void handleOpenFileAction(ActionEvent event){
        if (currStage == null){
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(currStage);
        if (file != null){
            try{
                String fileContent = FileManager.loadTextFromFile(file);
                Tab currTab = tabPane.getSelectionModel().getSelectedItem();
                TextArea currTextArea = tabTextAreaMap.get(currTab);
                currTextArea.setText(fileContent);
            } catch (IOException ioException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occurred");
                alert.setHeaderText("There was an issue loading text from file");
                alert.setContentText(ioException.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleSaveTextAction(ActionEvent event){
        if (currStage == null){
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showSaveDialog(currStage);
        if (file != null){
            try {
                Tab currTab = tabPane.getSelectionModel().getSelectedItem();
                TextArea currTextArea = tabTextAreaMap.get(currTab);
                String textAreaContent = currTextArea.getText();
                FileManager.saveTextToFile(textAreaContent, file);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Text");
                alert.setHeaderText("Text Saved Successfully");
                alert.setContentText("Your work was saved successfully in a file");
                alert.showAndWait();
            } catch (IOException ioException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occurred");
                alert.setHeaderText("There was an issue saving text to file");
                alert.setContentText(ioException.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleSaveAnalysisReportAction(ActionEvent event){
        if (currStage == null){
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showSaveDialog(currStage);
        if (file != null){
            Tab currTab = tabPane.getSelectionModel().getSelectedItem();
            TextArea currTextArea = tabTextAreaMap.get(currTab);
            AnalysisResult analysisResult = TextAnalyzer.analyze(currTextArea.getText());
            String tabName = (String) currTab.getUserData();
            tabAnalysisMap.put(currTab, analysisResult);
            try {
                FileManager.saveAnalysisReport(analysisResult, tabName, file);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Analysis.");
                alert.setHeaderText("Analysis Report Saved Successfully.");
                alert.setContentText("The analysise of your work was saved successfully.");
            } catch (IOException ioException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occurred.");
                alert.setHeaderText("There was an issue saving analysis report.");
                alert.setContentText(ioException.getMessage());
            }
        }
    }

    @FXML
    private void handleViewAnalysisReportAction(ActionEvent event){
        if (currStage == null){
            return;
        }
        Tab currTab = tabPane.getSelectionModel().getSelectedItem();
        TextArea currTextArea = tabTextAreaMap.get(currTab);
        String currTabName = (String) currTab.getUserData();
        if (currTextArea.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error occurred");
            alert.setHeaderText("There was an issue viewing analysis report.");
            alert.setContentText("No text to analyse. Please enter something.");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/analysis.fxml"));
            try {
                Parent root = fxmlLoader.load();
                AnalysisController analysisController = fxmlLoader.getController();
                AnalysisResult analysisResult = TextAnalyzer.analyze(currTextArea.getText());
                analysisController.loadAnalysis(analysisResult, currTabName);
                Stage analysisStage = new Stage();
                analysisStage.setTitle("Analysis Report: " + currTabName);
                Scene analysisScene = new Scene(root, 800, 800);
                analysisStage.setScene(analysisScene);
                analysisStage.show();
            } catch (IOException ioException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occurred");
                alert.setHeaderText("There was an issue viewing analysis report.");
                alert.setContentText(ioException.getMessage());
                alert.showAndWait();
            }
        }
    }


}
