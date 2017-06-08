package ToDoItemList;

import ToDoItemList.DataModel.ToDoItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Main extends Application {
    String filename = "allItems.ser";
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
    @Override
    public void stop(){
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutput out = new ObjectOutputStream(fos);
            System.out.println(Controller.getAllItems().getClass());
            out.writeObject(Controller.getAllItems());
            System.out.println("Successfully written to disk.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void init(){
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream oin = new ObjectInputStream(fin);
            ArrayList<ToDoItem> allFileItems = (ArrayList<ToDoItem>)oin.readObject();
            Controller.setAllItems(FXCollections.observableArrayList(allFileItems));
            System.out.println("Successfully read from disk.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
