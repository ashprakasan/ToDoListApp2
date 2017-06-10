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
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Controller {
    private PseudoClass pastDeadline = PseudoClass.getPseudoClass("pastDeadline");
    private PseudoClass isApproaching = PseudoClass.getPseudoClass("isApproaching");
    @FXML
    ListView<ToDoItem> ToDoItemList;
    static ObservableList<ToDoItem> allItems = FXCollections.observableArrayList();

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
        ToDoItemList.setItems(allItems.sorted(Comparator.comparing(ToDoItem::getDeadline)));
        ToDoItemList.getSelectionModel().selectFirst();
        ToDoItemList.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                return new newCell();
            }
        });
        }
        class newCell extends ListCell<ToDoItem> {
            ContextMenu contextMenu;
            MenuItem deleteItem;
            MenuItem editItem;

            public newCell() {
                this.contextMenu = new ContextMenu();
                this.deleteItem = new MenuItem("Delete");
                this.editItem = new MenuItem("Edit");
                contextMenu.getItems().addAll(deleteItem, editItem);
                this.emptyProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue)
                        this.setContextMenu(contextMenu);
                    else
                        this.setContextMenu(null);
                });
                deleteItem.setOnAction(event -> {
                    allItems.remove(newCell.super.getItem());
                    ToDoItemList.getSelectionModel().selectFirst();
                });
                editItem.setOnAction(event->{
                    ToDoItem item = newCell.super.getItem();
                    ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                    showItemDialog(saveButton,item);
                });
            }

            @Override
            public void updateItem(ToDoItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    pseudoClassStateChanged(pastDeadline, false);
                } else {
                    setText(item.toString());
                    pseudoClassStateChanged(pastDeadline, item.isDeadlinePast());
                    pseudoClassStateChanged(isApproaching, item.isDeadlineApproching());
                }
            }
        }

    public void showItemDialog(ButtonType button, ToDoItem item){
        Dialog<ToDoItem> ItemDialog = new Dialog<>();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(new Label("Short Description"), 1, 1);
        grid.add(new Label("Details : "), 1, 2);
        grid.add(new Label("Deadline : "), 1, 3);
        TextField shortDescription = new TextField();
        TextField details = new TextField();
        DatePicker deadLine = new DatePicker();
        grid.add(shortDescription, 2, 1);
        grid.add(details, 2, 2);
        grid.add(deadLine, 2, 3);
        ItemDialog.getDialogPane().setContent(grid);
        ItemDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, button);
        Node dialogButton = ItemDialog.getDialogPane().lookupButton(button);
        dialogButton.setDisable(true);
        shortDescription.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Node dialogButton = ItemDialog.getDialogPane().lookupButton(button);
                dialogButton.setDisable(newValue.trim().isEmpty());
            }
        });
        if(button.getText().equals("Add")){
            ItemDialog.setTitle("New Item Dialog");
            ItemDialog.setHeaderText("Details of new Item to be added");
            Platform.runLater(()->{
                shortDescription.requestFocus();
            });
        }
        if(button.getText().equals("Save")){
            ItemDialog.setTitle("Edit Item Dialog");
            ItemDialog.setHeaderText("Edit the Item ");
            shortDescription.setText(item.getShortDescription());
            details.setText(item.getDetails());
            deadLine.setValue(item.getDeadline());

        }
        ItemDialog.setResultConverter(ButtonType -> {
            if (ButtonType == button) {
                if(ButtonType.getText().equals("Save")){
                    allItems.remove(item);
                }
                return new ToDoItem(shortDescription.getText(), details.getText(), deadLine.getValue());
            }
            return null;
        });
        Optional<ToDoItem> newItem = ItemDialog.showAndWait();
        newItem.ifPresent(Item -> allItems.add((ToDoItem) newItem.get()));
        ToDoItemList.getSelectionModel().select(newItem.get());
    }
    @FXML
    public void addItemDialog() {
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        showItemDialog(addButtonType, null);
    }

    static ArrayList<ToDoItem> getAllItems() {
        return new ArrayList<ToDoItem>(allItems);
    }

    static void setAllItems(ObservableList<ToDoItem> allFileItems) {
        allItems = allFileItems;
    }
}

