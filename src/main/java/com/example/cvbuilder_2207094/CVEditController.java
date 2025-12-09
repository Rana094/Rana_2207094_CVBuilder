package com.example.cvbuilder_2207094;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CVEditController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;
    @FXML private TextArea educationArea;
    @FXML private TextArea skillsArea;
    @FXML private TextArea experienceArea;
    @FXML private TextArea projectsArea;

    private final DatabaseHelper db = new DatabaseHelper();

    public void loadRecord(String phone) {
        DatabaseHelper.CVRecord cv = db.getCV(phone);
        if (cv == null) {
            showAlert(Alert.AlertType.WARNING, "Not found", "No record for phone: " + phone);
            return;
        }
        phoneField.setText(cv.phone);
        nameField.setText(nullToEmpty(cv.name));
        emailField.setText(nullToEmpty(cv.email));
        addressArea.setText(nullToEmpty(cv.address));


        educationArea.clear();
        String line = String.join("|",
                nullToEmpty(cv.exam),
                nullToEmpty(cv.institute),
                nullToEmpty(cv.boardUniversity),
                nullToEmpty(cv.session),
                nullToEmpty(cv.result));
        educationArea.appendText(line + "\n");


        skillsArea.clear();
        skillsArea.appendText(cv.skills);


        experienceArea.clear();
        experienceArea.appendText(cv.workExperience);

        projectsArea.clear();
        projectsArea.appendText(cv.projects);

        phoneField.setEditable(false);

    }


    @FXML
    private void onUpdate(ActionEvent event) {
        try {
            DatabaseHelper.CVRecord cv = collectForm();

            db.updateCV(cv);
            showAlert(Alert.AlertType.INFORMATION, "Updated", "Record updated for phone: " + cv.phone);
        } catch (IllegalArgumentException iae) {
            showAlert(Alert.AlertType.WARNING, "Validation", iae.getMessage());
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        String phone = phoneField.getText();
        if (phone == null || phone.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing phone", "Phone is required to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete record for phone " + phone + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait();
        if (confirm.getResult() != ButtonType.YES) return;

        boolean ok = db.deleteCV(phone);
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Record deleted.");

            goBackToRecords(event);
        } else {
            showAlert(Alert.AlertType.WARNING, "Not found", "No record was deleted.");
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        goBackToRecords(event);
    }

    private void goBackToRecords(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("records_view.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) ((Node) ((javafx.scene.Node) event.getSource()).getScene().getWindow());
//            stage.setScene(new Scene(root));
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Navigation Error", e.getMessage());
//        }
    }

    // Build a CVRecord from UI fields (used by Update)
    private DatabaseHelper.CVRecord collectForm() {
        String phone = phoneField.getText();
        String name = nameField.getText();
        if (phone == null || phone.trim().isEmpty()) throw new IllegalArgumentException("Phone is required.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name is required.");

        DatabaseHelper.CVRecord cv = new DatabaseHelper.CVRecord();
        cv.phone = phone.trim();
        cv.name = name.trim();
        cv.email = safeTrim(emailField.getText());
        cv.address = safeTrim(addressArea.getText());
        cv.exam = safeTrim(educationArea.getText());
        cv.session = safeTrim(skillsArea.getText());
        cv.result = safeTrim(experienceArea.getText());
        cv.workExperience = safeTrim(experienceArea.getText());
        cv.projects = safeTrim(projectsArea.getText());
        cv.skills = safeTrim(skillsArea.getText());

        return cv;
    }

    // small helpers
    private String safeGet(String[] arr, int i) { return i < arr.length ? arr[i].trim() : ""; }
    private String safeTrim(String s) { return s == null ? "" : s.trim(); }
    private String nullToEmpty(String s) { return s == null ? "" : s; }

    private List<String> splitLines(String text) {
        List<String> out = new ArrayList<>();
        if (text == null || text.isEmpty()) return out;
        String[] lines = text.split("\\r?\\n");
        for (String L: lines) out.add(L.trim());
        return out;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
