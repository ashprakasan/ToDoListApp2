package ToDoItemList;

import ToDoItemList.DataModel.ToDoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class Controller {
    PseudoClass pastDeadline = PseudoClass.getPseudoClass("pastDeadline");
    PseudoClass isApproaching = PseudoClass.getPseudoClass("isApproaching");
    @FXML
    ListView<ToDoItem> ToDoItemList;
    ObservableList<ToDoItem> allItems = FXCollections.observableArrayList();

    @FXML
    TextFlow DetailsArea;

    public void initialize() {
        ToDoItemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                Text text1 = new Text(" " + newValue.getDetails() + "\n before : ");
                Text text2 = new Text(" " + newValue.getDeadline().format(formatter));
                text2.setStyle("-fx-font-weight:bold;-fx-fill:blue");
                DetailsArea.getChildren().clear();
                DetailsArea.getChildren().addAll(text1, text2);
            }
        });
        ToDoItemList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        allItems.add(new ToDoItem("Wish Birthday", "Send a birthday card to John",
                LocalDate.of(2017, Month.AUGUST, 25)));
        allItems.add(new ToDoItem("Order stuff", "Order required groceries on bigbasket",
                LocalDate.of(2017, Month.JULY, 25)));
        allItems.add(new ToDoItem("Call Electrician ", "Call electrician from UrbanClap for exhaust fan repair",
                LocalDate.of(2017, Month.JUNE, 15)));
        allItems.add(new ToDoItem("Create app", "Finish making the TodoList App",
                LocalDate.of(2017, Month.OCTOBER, 5)));
        allItems.add(new ToDoItem("Take vacation", "Plan and book ticket to Kerala",
                LocalDate.of(2017, Month.MARCH, 5)));
        allItems.add(new ToDoItem("Finish 3 lectures", "Complete three lectures with code submissions.",
                LocalDate.of(2017, Month.JUNE, 9)));
        ToDoItemList.setItems(allItems.sorted(Comparator.comparing(ToDoItem::getDeadline)));
        ToDoItemList.getSelectionModel().selectFirst();

        ToDoItemList.setCellFactory( lv-> new ListCell<ToDoItem>(){
           @Override
           public void updateItem(ToDoItem item, boolean empty){
                super.updateItem(item,empty);
                if(empty){
                    setText(null);
                    pseudoClassStateChanged(pastDeadline,false);
                }
                else{
                    setText(item.toString());
                    pseudoClassStateChanged(pastDeadline,item!=null&&item.isDeadlinePast());
                    pseudoClassStateChanged(isApproaching,item!=null&&item.isDeadlineApproching());
                    }
                }
        });

    }
    @FXML
    public void addItemDialog(){
        Dialog<ToDoItem> addItemDialog = new Dialog<>();
        GridPane grid= new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));
        grid.add(new Label("Enter Short Description"),1,1);
        grid.add(new Label("Enter Details : "),1,2);
        grid.add(new Label("Select Deadline : "),1,3);
        TextField shortDescription = new TextField();
        TextField details= new TextField();
        DatePicker deadLine = new DatePicker();
        grid.add(shortDescription,2,1);
        grid.add(details,2,2);
        grid.add(deadLine,2,3);
        addItemDialog.getDialogPane().setContent(grid);
        addItemDialog.setTitle("New Item Dialog");
        addItemDialog.setHeaderText("New To Do List Item to add");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        addItemDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL,addButtonType);
        Node addButton = addItemDialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        shortDescription.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,String newValue){
                Node addButton = addItemDialog.getDialogPane().lookupButton(addButtonType);
                addButton.setDisable(newValue.trim().isEmpty());
            }
        });
        Platform.runLater(() -> {
                shortDescription.requestFocus();
        });
        addItemDialog.setResultConverter(ButtonType->{
            if(ButtonType==addButtonType){
                return new ToDoItem(shortDescription.getText(),details.getText(),deadLine.getValue());
            }
            return null;
        });
        Optional<ToDoItem> newItem = addItemDialog.showAndWait();
        newItem.ifPresent(Item->allItems.add((ToDoItem)newItem.get()));
    }
}

