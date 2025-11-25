package com.example.cvbuilder_2207094;

import com.example.cvbuilder_2207094.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class RecordsViewController {
    public void onLoadRecord(ActionEvent actionEvent) {
    }

    public void onDeleteRecord(ActionEvent actionEvent) {
    }

    public void onBackClick(ActionEvent actionEvent) {
    }

    @FXML private TableView<RecordRow> recordsTable;
    @FXML private TableColumn<RecordRow, String> phoneCol;
    @FXML private TableColumn<RecordRow, String> nameCol;

    private final DatabaseHelper db = new DatabaseHelper("myDatabase.db");

    public static class RecordRow {
        private final String phone;
        private final String name;

        public RecordRow(String phone, String name) {
            this.phone = phone;
            this.name = name;
        }

        public String getPhone() { return phone; }
        public String getName() { return name; }
    }

    @FXML
    public void initialize() {
        phoneCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        loadTable();
    }

    private void loadTable() {
        try {
            var list = db.listAllPhonesAndNames();
            ObservableList<RecordRow> data = FXCollections.observableArrayList();

            for (Map.Entry<String, String> entry : list) {
                data.add(new RecordRow(entry.getKey(), entry.getValue()));
            }

            recordsTable.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoadRecord() throws IOException {
        RecordRow selected = recordsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a record.");
            return;
        }


        FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_edit.fxml"));
        Parent root = loader.load();


        CVEditController controller = loader.getController();
        controller.loadRecord(selected.getPhone());

        Stage stage = (Stage) recordsTable.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void onDeleteRecord() {
        try {
            RecordRow selected = recordsTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Please select a record.");
                return;
            }

            boolean ok = db.deleteCV(selected.getPhone());
            if (ok) {
                showAlert("Record deleted.");
                loadTable();
            } else {
                showAlert("Error deleting record.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home_page.fxml"));
        Stage stage = (Stage) recordsTable.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.show();
    }
}
