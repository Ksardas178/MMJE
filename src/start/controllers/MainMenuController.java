package start.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import start.GameConst;
import start.Main;

public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button lodGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button exitButton;

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void loadGame(ActionEvent event) {

    }

    @FXML
    void newGame(ActionEvent event) {

    }

    @FXML
    void initialize() {
        newGameButton.setOnAction(event ->
                Main.sceneToChange = GameConst.newGameFieldID
        );
        exitButton.setOnAction(event ->
                exitButton.getScene().getWindow().hide()
        );
    }
}
