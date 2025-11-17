package com.example.cvbuilder_2207094;

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
        Stage stage = (Stage) submitCv.getScene().getWindow();
        FXMLLoader  loader = new FXMLLoader(getClass().getResource("EndPage.fxml"));
        Scene scene =new Scene(loader.load());
        stage.setTitle("End Page");
        stage.setScene(scene);


    }

}
