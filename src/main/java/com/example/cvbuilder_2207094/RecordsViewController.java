//package com.example.cvbuilder_2207094;
//
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.stage.Stage;
//import javafx.scene.Node;
//import javafx.event.ActionEvent;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Map;
//
//public class RecordsViewController {
//
//    @FXML private TableView<RecordRow> recordsTable;
//    @FXML private TableColumn<RecordRow, String> phoneCol;
//    @FXML private TableColumn<RecordRow, String> nameCol;
//
//    private DatabaseHelper db;
//
//    public void onBackClick(ActionEvent actionEvent) {
//    }
//
//    public void onDeleteRecord(ActionEvent actionEvent) {
//    }
//
//    public static class RecordRow {
//        private final String phone;
//        private final String name;
//        public RecordRow(String phone, String name) { this.phone = phone; this.name = name; }
//        public String getPhone(){ return phone; }
//        public String getName(){ return name; }
//    }
//
//    @FXML
//    public void initialize() {
//        //String dbPath = System.getProperty("user.dir") + "/myDatabase.db";
//        db = new DatabaseHelper();
////        try { db.initDatabase(); } catch (SQLException e) { e.printStackTrace(); }
//
//        phoneCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
//        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
//        loadTable();
//    }
//
//
//    private void loadTable() {
//        try {
//            ObservableList<RecordRow> items = FXCollections.observableArrayList();
//            for (Map.Entry<String,String> e : db.listAllPhonesAndNames()) {
//                items.add(new RecordRow(e.getKey(), e.getValue()));
//            }
//            recordsTable.setItems(items);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "DB error", ex.getMessage());
//        }
//    }
//
//    @FXML
//    private void onLoadRecord(ActionEvent evt) {
//        RecordRow sel = recordsTable.getSelectionModel().getSelectedItem();
//        if (sel == null) { showAlert(Alert.AlertType.INFORMATION, "Select", "Please select a record first."); return; }
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_edit.fxml"));
//            Parent root = loader.load();
//            CVEditController controller = loader.getController();
//            controller.loadRecord(sel.getPhone());
//            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Load error", ex.getMessage());
//        }
//    }
//
////    @FXML
////    private void onDeleteRecord(ActionEvent evt) {
////        RecordRow sel = recordsTable.getSelectionModel().getSelectedItem();
////        if (sel == null) { showAlert(Alert.AlertType.INFORMATION, "Select", "Please select a record first."); return; }
////        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "Delete record for phone " + sel.getPhone() + "?", ButtonType.YES, ButtonType.NO);
////        c.setHeaderText(null);
////        c.showAndWait();
////        if (c.getResult() != ButtonType.YES) return;
////        boolean ok = db.deleteCV(sel.getPhone());
////        if (ok) {
////            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Record deleted.");
////            loadTable(); // refresh
////        } else {
////            showAlert(Alert.AlertType.WARNING, "Not found", "Record not found or not deleted.");
////        }
////    }
//
//    @FXML
//    private void onBack(ActionEvent evt) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("Welcome.fxml")); // or your home FXML
//            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Navigation", e.getMessage());
//        }
//    }
//
//    private void showAlert(Alert.AlertType t, String title, String message) {
//        Alert a = new Alert(t);
//        a.setTitle(title);
//        a.setHeaderText(null);
//        a.setContentText(message);
//        a.showAndWait();
//    }
//}





package com.example.cvbuilder_2207094;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RecordsViewController {

    @FXML private TableView<RecordRow> recordsTable;
    @FXML private TableColumn<RecordRow, String> phoneCol;
    @FXML private TableColumn<RecordRow, String> nameCol;

    private DatabaseHelper db;

    // Helper class for the TableView
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
        db = new DatabaseHelper();
        // Initialize columns
        phoneCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        loadTable();
    }

    // --- THE FIXED LOAD TABLE METHOD ---
    private void loadTable() {
        ObservableList<RecordRow> items = FXCollections.observableArrayList();

        // Call the new method from DatabaseHelper
        List<DatabaseHelper.CVRecord> records = db.getAllCVs();

        // Loop through the results and add them to the table
        for (DatabaseHelper.CVRecord rec : records) {
            items.add(new RecordRow(rec.phone, rec.name));
        }

        recordsTable.setItems(items);
    }

    @FXML
    private void onLoadRecord(ActionEvent evt) {
        RecordRow sel = recordsTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.INFORMATION, "Select", "Please select a record first.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cv_edit.fxml"));
            Parent root = loader.load();

            // Assuming your CVEditController has a loadRecord method
            // CVEditController controller = loader.getController();
            // controller.loadRecord(sel.getPhone());

            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load error", ex.getMessage());
        }
    }

    @FXML
    private void onDeleteRecord(ActionEvent evt) {
        RecordRow sel = recordsTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.INFORMATION, "Select", "Please select a record first.");
            return;
        }

        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "Delete record for phone " + sel.getPhone() + "?", ButtonType.YES, ButtonType.NO);
        c.setHeaderText(null);
        c.showAndWait();

        if (c.getResult() != ButtonType.YES) return;

        // Calling the static delete method
        boolean ok = DatabaseHelper.deleteCV(sel.getPhone());

        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Record deleted.");
            loadTable(); // Refresh the table to remove the deleted item
        } else {
            showAlert(Alert.AlertType.WARNING, "Error", "Record could not be deleted.");
        }
    }

    @FXML
    private void onBackClick(ActionEvent evt) {
        try {
            // Ensure "Welcome.fxml" exists in your resources
            Parent root = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType t, String title, String message) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
