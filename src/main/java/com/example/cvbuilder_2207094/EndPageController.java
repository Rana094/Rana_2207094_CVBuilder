package com.example.cvbuilder_2207094;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

public class EndPageController {

    @FXML
    private Button ExitBtn;

    @FXML
    private Label addressLbl;

    @FXML
    private Label boardLbl;

    @FXML
    private Label emailLbl;

    @FXML
    private Label examLbl;

    @FXML
    private Label institueLbl;

    @FXML
    private Label nameLbl;

    @FXML
    private Label phoneLbl;

    @FXML
    private Label projectLbl;

    @FXML
    private Label resultLbl;

    @FXML
    private Label sessionLbl;

    @FXML
    private Label skillsLbl;

    @FXML
    private Label workLbl;

    @FXML
    void ExitBtn(MouseEvent event) {

    }

    @FXML
    void submitCvNext(MouseEvent event) {

    }

    @FXML
    public void setName(String name) {
        nameLbl.setText(name);
    }
    public void setPhone(String phone) {
        phoneLbl.setText(phone);
    }
    public void setProject(String project) {
        projectLbl.setText(project);

    }
    public void setResult(String result) {
        resultLbl.setText(result);

    }
    public void setSession(String session) {
        sessionLbl.setText(session);
    }
//    public void setSkills(String skills) {
//        skillsLbl.se
//    }
    public void setWork(String work) {
        workLbl.setText(work);
    }
    public void setAddress(String address) {
        addressLbl.setText(address);
    }
    public void setEmail(String email) {
        emailLbl.setText(email);
    }
    public void setExam(String exam) {
        examLbl.setText(exam);
    }
    public void setInstitue(String institue) {
        institueLbl.setText(institue);
    }
    public void setSkills(String skills) {
        skillsLbl.setText(skills);
    }
    public void setBoardLbl(String board) {
        boardLbl.setText(board);
    }


}
