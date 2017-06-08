package ToDoItemList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("ToDo List");
        File f = new File("mycss.css");
        Scene mainWindowScene = new Scene(root,700,500);
        mainWindowScene.getStylesheets().clear();
        mainWindowScene.getStylesheets().add("/ToDoItemList/mycss.css");
        primaryStage.setScene(mainWindowScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
