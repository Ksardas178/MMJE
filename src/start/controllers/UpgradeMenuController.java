package start.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import start.GameConst;
import start.Main;

public class UpgradeMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProgressBar concentrationBar;

    @FXML
    private TextField concentrationText;

    @FXML
    private ProgressBar enduranceBar;

    @FXML
    private TextField enduranceText;

    @FXML
    private ProgressBar strengthBar;

    @FXML
    private TextField strengthText;

    @FXML
    private ProgressBar healingBar;

    @FXML
    private TextField healingText;

    @FXML
    private ProgressBar manaBar;

    @FXML
    private TextField manaText;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        backButton.setOnAction(event -> {
            Main.sceneToChange = GameConst.gameMenuID;
        });
        concentrationBar.setProgress(Main.player.getAbilityPercentage("concentration")/10.0);
        concentrationText.appendText("lvl "+Main.player.getAbility("concentration"));
        enduranceBar.setProgress(Main.player.getAbilityPercentage("endurance")/10.0);
        enduranceText.appendText("lvl "+Main.player.getAbility("endurance"));
        strengthBar.setProgress(Main.player.getAbilityPercentage("strength")/10.0);
        strengthText.appendText("lvl "+Main.player.getAbility("strength"));
        healingBar.setProgress(Main.player.getAbilityPercentage("healing")/10.0);
        healingText.appendText("lvl "+Main.player.getAbility("healing"));
        manaBar.setProgress(Main.player.getAbilityPercentage("mana")/10.0);
        manaText.appendText("lvl "+Main.player.getAbility("mana"));
    }
}
