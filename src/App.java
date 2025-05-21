import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class App extends Application {

    private static final String FILE_TXT = "todos.txt";
    private ListView<String> listView;
    private TextField inputField;
    private Button addButton, updateButton, deleteButton;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage){

        listView = new ListView<>();
        inputField = new TextField();
        inputField.setPromptText("Masukkan list....");

        addButton = new Button("Tambahkan");
        updateButton = new Button("Perbarui");
        deleteButton = new Button("Hapus");

        addButton.setOnAction(e -> addItem());
        updateButton.setOnAction(e -> updateItem());
        deleteButton.setOnAction(e -> deleteItem());

        HBox inputBox = new HBox(10, inputField, addButton, updateButton, deleteButton);
        inputBox.setStyle("-fx-padding: 10;");
        VBox root = new VBox(10, listView, inputBox);
        root.setStyle("-fx-padding: 1-;");

        loadItems();

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("To-Do-List App");
        stage.setScene(scene);

        stage.setOnCloseRequest(this::onClose);

        stage.show();

    }

    private void addItem(){
        String item = inputField.getText().trim();
        if (!item.isEmpty()){
            listView.getItems().add(item);
            inputField.clear();
        }
    }

    private void updateItem(){
        int idx = listView.getSelectionModel().getSelectedIndex();
        String newItem = inputField.getText().trim();
        if (idx != -1 && !newItem.isEmpty()){
            listView.getItems().set(idx, newItem);
            inputField.clear();
        }
    }

    private void deleteItem(){
        int idx = listView.getSelectionModel().getSelectedIndex();
        if (idx != -1){
            listView.getItems().remove(idx);
        }
    }

    private void loadItems(){
        Path path = Paths.get(FILE_TXT);
        if (Files.exists(path)){
            try{
                List<String> lines = Files.readAllLines(path);
                listView.getItems().addAll(lines);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void onClose(WindowEvent event){
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_TXT))){
            for (String item : listView.getItems()){
                writer.write(item);
                writer.newLine();
            } 
        }catch (IOException e ){
                e.printStackTrace();
            }
    }    
}
