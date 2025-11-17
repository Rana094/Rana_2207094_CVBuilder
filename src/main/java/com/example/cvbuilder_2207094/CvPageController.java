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

public class CvPageController {

    @FXML
    private TextArea address;

    @FXML
    private TextArea boardtxt;

    @FXML
    private TextField email;

    @FXML
    private TextArea examtxt;

    @FXML
    private TextArea institutetxt;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private TextArea projecttxt;

    @FXML
    private TextArea resulttxt;

    @FXML
    private TextArea sessiontxt;

    @FXML
    private TextArea skilltxt;

    @FXML
    private Button submitCv;

    @FXML
    private TextArea worktxt;

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


        if (isBlankTF.test(name)) {
            problems.add("Name");
        }
        if (isBlankTF.test(email)) {
            problems.add("Email");
        }
        if (isBlankTF.test(phone)) {
            problems.add("Phone");
        }
        if (isBlankTA.test(address)) {
            problems.add("Address");

        }
        if (isBlankTA.test(examtxt)) {
            problems.add("Exam");
        }
        if (isBlankTA.test(institutetxt)) {
            problems.add("Institute");
        }
        if (isBlankTA.test(boardtxt)) {
            problems.add("Board/University");
        }
        if (isBlankTA.test(sessiontxt)) {
            problems.add("Session");
        }
        if (isBlankTA.test(resulttxt)) {
            problems.add("Result");
        }
        if (isBlankTA.test(skilltxt)) {
            problems.add("Skills");
        }
        if (isBlankTA.test(worktxt)) {
            problems.add("Work");
        }
        if (isBlankTA.test(projecttxt)) {
            problems.add("Project");
        }


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

        Stage stage = (Stage) submitCv.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EndPage.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("End Page");
        stage.setScene(scene);
    }
}
