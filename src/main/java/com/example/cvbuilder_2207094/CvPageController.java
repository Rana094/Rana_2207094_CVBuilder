package com.example.cvbuilder_2207094;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CvPageController {

    @FXML private TextArea address;
    @FXML private TextArea boardtxt;
    @FXML private TextField email;
    @FXML private TextArea examtxt;
    @FXML private TextArea institutetxt;
    @FXML private TextField name;
    @FXML private TextField phone;
    @FXML private TextArea projecttxt;
    @FXML private TextArea resulttxt;
    @FXML private TextArea sessiontxt;
    @FXML private TextArea skilltxt;
    @FXML private Button submitCv;
    @FXML private TextArea worktxt;

    private DatabaseHelper db;

    @FXML
    public void initialize() {
        db = new DatabaseHelper("cv_data.db");
        try {
            db.initDatabase();
        } catch (SQLException e) {
            showError("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void submit(MouseEvent event) throws IOException {
    }

    @FXML
    void submitCvNext(MouseEvent event) throws IOException {

        Node[] nodes = new Node[]{
                name, email, phone, address,
                examtxt, institutetxt, boardtxt, sessiontxt, resulttxt,
                skilltxt, worktxt, projecttxt
        };

        List<String> problems = new ArrayList<>();

        java.util.function.Predicate<TextField> isBlankTF = tf -> tf == null || tf.getText() == null || tf.getText().trim().isEmpty();
        java.util.function.Predicate<TextArea> isBlankTA = ta -> ta == null || ta.getText() == null || ta.getText().trim().isEmpty();

        if (isBlankTF.test(name)) { problems.add("Name"); }
        if (isBlankTF.test(email)) { problems.add("Email"); }
        if (isBlankTF.test(phone)) { problems.add("Phone"); }
        if (isBlankTA.test(address)) { problems.add("Address"); }
        if (isBlankTA.test(examtxt)) { problems.add("Exam"); }
        if (isBlankTA.test(institutetxt)) { problems.add("Institute"); }
        if (isBlankTA.test(boardtxt)) { problems.add("Board/University"); }
        if (isBlankTA.test(sessiontxt)) { problems.add("Session"); }
        if (isBlankTA.test(resulttxt)) { problems.add("Result"); }
        if (isBlankTA.test(skilltxt)) { problems.add("Skills"); }
        if (isBlankTA.test(worktxt)) { problems.add("Work"); }
        if (isBlankTA.test(projecttxt)) { problems.add("Project"); }

        if (!problems.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String p : problems) sb.append("• ").append(p).append("\n");
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText("Please fix the following before submitting:");
            alert.setContentText(sb.toString());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            return;
        }

        try {
            DatabaseHelper.CVRecord cv = buildRecordFromForm();
            try {
                db.insertCV(cv);
                showInfo("CV saved successfully for phone: " + cv.phone);
            } catch (SQLException insertEx) {
                String msg = insertEx.getMessage() == null ? "" : insertEx.getMessage().toLowerCase();
                if (msg.contains("constraint") || msg.contains("unique") || msg.contains("primary key")) {
                    boolean shouldUpdate = confirm("A record with this phone already exists. Do you want to update it?");
                    if (shouldUpdate) {
                        try {
                            db.updateCV(cv);
                            showInfo("CV updated for phone: " + cv.phone);
                        } catch (SQLException updateEx) {
                            showError("Update failed: " + updateEx.getMessage());
                            updateEx.printStackTrace();
                            return;
                        }
                    } else {
                    }
                } else {
                    showError("Save failed: " + insertEx.getMessage());
                    insertEx.printStackTrace();
                    return;
                }
            }
        } catch (Exception e) {
            showError("Error while saving CV: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        Stage stage = (Stage) submitCv.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EndPage.fxml"));

        Scene scene = new Scene(loader.load());

        EndPageController controller = loader.getController();
        controller.setName(name.getText());
        controller.setEmail(email.getText());
        controller.setPhone(phone.getText());
        controller.setAddress(address.getText());
        controller.setExam(examtxt.getText());
        controller.setInstitute(institutetxt.getText());
        controller.setProject(projecttxt.getText());
        controller.setResult(resulttxt.getText());
        controller.setWork(worktxt.getText());
        controller.setSkills(skilltxt.getText());
        controller.setSession(sessiontxt.getText());
        controller.setBoardLbl(boardtxt.getText());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("CV saved successfully!");
        alert.showAndWait();

        stage.setTitle("End Page");
        stage.setScene(scene);
    }

    private DatabaseHelper.CVRecord buildRecordFromForm() {
        DatabaseHelper.CVRecord cv = new DatabaseHelper.CVRecord();
        cv.phone = phone.getText().trim();
        cv.name = name.getText().trim();
        cv.email = email.getText().trim();
        cv.address = address.getText().trim();

        for (String line : splitLines(examtxt.getText())) {
            if (line.isBlank()) continue;
            DatabaseHelper.Education ed = new DatabaseHelper.Education();
            if (line.contains("|")) {
                String[] parts = line.split("\\|", -1);
                ed.exam = safeGet(parts,0);
                ed.institute = safeGet(parts,1);
                ed.boardUniversity = safeGet(parts,2);
                ed.session = safeGet(parts,3);
                ed.result = safeGet(parts,4);
            } else {
                ed.exam = line;
            }
            cv.education.add(ed);
        }

        if (!institutetxt.getText().isBlank() && cv.education.isEmpty()) {
            DatabaseHelper.Education ed = new DatabaseHelper.Education();
            ed.exam = examtxt.getText().trim();
            ed.institute = institutetxt.getText().trim();
            ed.boardUniversity = boardtxt.getText().trim();
            ed.session = sessiontxt.getText().trim();
            ed.result = resulttxt.getText().trim();
            cv.education.add(ed);
        }

        for (String s : splitLines(skilltxt.getText())) {
            if (!s.isBlank()) cv.skills.add(s);
        }

        for (String line : splitLines(worktxt.getText())) {
            if (line.isBlank()) continue;
            DatabaseHelper.Experience ex = new DatabaseHelper.Experience();
            if (line.contains("|")) {
                String[] p = line.split("\\|", -1);
                ex.role = safeGet(p,0);
                ex.organization = safeGet(p,1);
                ex.duration = safeGet(p,2);
                ex.description = safeGet(p,3);
            } else {
                ex.role = line;
            }
            cv.experience.add(ex);
        }

        for (String line : splitLines(projecttxt.getText())) {
            if (line.isBlank()) continue;
            DatabaseHelper.Project p = new DatabaseHelper.Project();
            if (line.contains("|")) {
                String[] a = line.split("\\|", -1);
                p.title = safeGet(a,0);
                p.description = safeGet(a,1);
                p.link = safeGet(a,2);
            } else {
                p.title = line;
            }
            cv.projects.add(p);
        }

        return cv;
    }

    private String safeGet(String[] arr, int i) {
        return i < arr.length ? arr[i].trim() : "";
    }

    private List<String> splitLines(String text) {
        List<String> out = new ArrayList<>();
        if (text == null || text.isEmpty()) return out;
        String[] lines = text.split("\\r?\\n");
        for (String L: lines) out.add(L.trim());
        return out;
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, javafx.scene.control.ButtonType.YES, javafx.scene.control.ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait();
        return a.getResult() == javafx.scene.control.ButtonType.YES;
    }
}
