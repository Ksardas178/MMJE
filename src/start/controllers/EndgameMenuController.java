package start.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import start.GameConst;
import start.Main;

public class EndgameMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private TextField scoreField;

    @FXML
    void initialize() {
        exitButton.setOnAction(event -> {
            exitButton.getScene().getWindow().hide();
        });
        mainMenuButton.setOnAction(event -> {
            Main.sceneToChange = GameConst.mainMenuID;
        });
        scoreField.appendText(Main.player.getAbility("experience"));
    }
}
