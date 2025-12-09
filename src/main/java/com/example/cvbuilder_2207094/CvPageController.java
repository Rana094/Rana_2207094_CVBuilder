package com.example.cvbuilder_2207094;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.lang.foreign.SymbolLookup;
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

//    private com.yourcompany.cvbuilder.DatabaseHelper db;

    @FXML
    public void initialize() {
        //String dbPath = System.getProperty("user.dir") + "/myDatabase.db";
        db = new com.example.cvbuilder_2207094.DatabaseHelper();

//        try {
//            db.initDatabase();
//        } catch (SQLException e) {
//            showError("Database initialization failed: " + e.getMessage());
//            e.printStackTrace();
//        }
    }

//    @FXML
//    void submit(MouseEvent event) throws IOException {
//    }

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
//        if (isBlankTA.test(skilltxt)) { problems.add("Skills"); }
//        if (isBlankTA.test(worktxt)) { problems.add("Work"); }
//        if (isBlankTA.test(projecttxt)) { problems.add("Project"); }

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
            com.example.cvbuilder_2207094.DatabaseHelper.CVRecord cv = buildRecordFromForm();
            System.out.println(cv.name);
            DatabaseHelper.createNewTable();
            db.insertCV(cv);
            showInfo("CV saved successfully for phone: " + cv.phone);
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
        cv.boardUniversity = boardtxt.getText().trim();
        cv.institute = institutetxt.getText().trim();
        cv.projects = projecttxt.getText().trim();
        cv.skills = skilltxt.getText().trim();
        cv.result = resulttxt.getText().trim();
        cv.exam = examtxt.getText().trim();
        cv.institute = institutetxt.getText().trim();
        cv.workExperience = worktxt.getText().trim();
        cv.session = sessiontxt.getText().trim();

        return cv;
    }

//    private String safeGet(String[] arr, int i) {
//        return i < arr.length ? arr[i].trim() : "";
//    }
//
//    private List<String> splitLines(String text) {
//        List<String> out = new ArrayList<>();
//        if (text == null || text.isEmpty()) return out;
//        String[] lines = text.split("\\r?\\n");
//        for (String L: lines) out.add(L.trim());
//        return out;
//    }
//
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
//
//    private boolean confirm(String msg) {
//        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, javafx.scene.control.ButtonType.YES, javafx.scene.control.ButtonType.NO);
//        a.setHeaderText(null);
//        a.showAndWait();
//        return a.getResult() == javafx.scene.control.ButtonType.YES;
//    }
}
