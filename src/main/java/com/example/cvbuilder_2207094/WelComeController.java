package com.example.cvbuilder_2207094;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelComeController {

    @FXML
    private Button next;

    @FXML
    void GoNext(MouseEvent event) throws IOException {
        Stage stage = (Stage) next.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CvPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cv Creator Page");
        stage.setScene(scene);

    }

    @FXML
    void ShowRecords(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("records_view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

}
