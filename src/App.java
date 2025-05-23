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
    private ListView<TodoItem> listView;
    private TextField inputField;
    private Button addButton, updateButton, deleteButton, toggleButton;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        listView = new ListView<>();
        inputField = new TextField();
        inputField.setPromptText("Masukkan list...");

        addButton = new Button("Tambahkan");
        updateButton = new Button("Perbarui");
        deleteButton = new Button("Hapus");
        toggleButton = new Button("Ubah Status");

        addButton.setOnAction(e -> addItem());
        updateButton.setOnAction(e -> updateItem());
        deleteButton.setOnAction(e -> deleteItem());
        toggleButton.setOnAction(e -> toggleStatus());

        HBox inputBox = new HBox(10, inputField, addButton, updateButton, deleteButton, toggleButton);
        inputBox.setStyle("-fx-padding: 10;");
        VBox root = new VBox(10, listView, inputBox);
        root.setStyle("-fx-padding: 10;");

        loadItems();

        Scene scene = new Scene(root, 500, 350);
        stage.setTitle("To-Do List App");
        stage.setScene(scene);
        stage.setOnCloseRequest(this::onClose);
        stage.show();
    }

    private void addItem() {
        String item = inputField.getText().trim();
        if (!item.isEmpty()) {
            listView.getItems().add(new TodoItem(item, false));
            inputField.clear();
        }
    }

    private void updateItem() {
        int idx = listView.getSelectionModel().getSelectedIndex();
        String newItem = inputField.getText().trim();
        if (idx != -1 && !newItem.isEmpty()) {
            TodoItem selected = listView.getItems().get(idx);
            listView.getItems().set(idx, new TodoItem(newItem, selected.isDone()));
            inputField.clear();
        }
    }

    private void deleteItem() {
        int idx = listView.getSelectionModel().getSelectedIndex();
        if (idx != -1) {
            listView.getItems().remove(idx);
        }
    }

    private void toggleStatus() {
        int idx = listView.getSelectionModel().getSelectedIndex();
        if (idx != -1) {
            TodoItem selected = listView.getItems().get(idx);
            selected.toggleStatus();
            listView.refresh();
        }
    }

    private void loadItems() {
        Path path = Paths.get(FILE_TXT);
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);
                for (String line : lines) {
                    String[] parts = line.split("\\|", 2);
                    if (parts.length == 2) {
                        listView.getItems().add(new TodoItem(parts[1], Boolean.parseBoolean(parts[0])));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onClose(WindowEvent event) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_TXT))) {
            for (TodoItem item : listView.getItems()) {
                writer.write(item.isDone() + "|" + item.getDescription());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static class TodoItem {
        private String description;
        private boolean isDone;

        public TodoItem(String description, boolean isDone) {
            this.description = description;
            this.isDone = isDone;
        }

        public String getDescription() {
            return description;
        }

        public boolean isDone() {
            return isDone;
        }

        public void toggleStatus() {
            this.isDone = !this.isDone;
        }

        @Override
        public String toString() {
            return (isDone ? "[SELESAI] " : "[BELUM SELESAI] ") + description;
        }
    }
}
