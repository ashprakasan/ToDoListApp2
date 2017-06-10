package ToDoItemList.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by tusharmathur on 09/06/17.
 */
public class ToDoData {
    private static ObservableList<ToDoItem> allItems = FXCollections.observableArrayList();
    private static String filename = "allItems.ser";

//    private ToDoData(){
//        allItems = FXCollections.observableArrayList();
//        filename = "allItems.ser";
//    }

    public static ObservableList<ToDoItem> getAllItems() {
        return allItems;
    }

    public static void loadData(){
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream oin = new ObjectInputStream(fin);
            ArrayList<ToDoItem> allFileItems = (ArrayList<ToDoItem>)oin.readObject();
            allItems= FXCollections.observableArrayList(allFileItems);
            System.out.println("Successfully read from disk.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void saveData(){
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutput out = new ObjectOutputStream(fos);
            out.writeObject(new ArrayList<ToDoItem>(getAllItems()));
            System.out.println("Successfully written to disk.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
