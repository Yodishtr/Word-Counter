import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        try{
            Parent root = loader.load();
            Scene mainScene = new Scene(root, 1000, 700);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Word Counter");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            URL imageUrl = getClass().getResource("/images/images.jpeg");
            if (imageUrl != null) {
                primaryStage.getIcons().add(new Image(String.valueOf(imageUrl)));
            }
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
