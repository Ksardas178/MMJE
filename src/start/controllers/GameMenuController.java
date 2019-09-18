package start.controllers;

        import java.net.URL;
        import java.util.ResourceBundle;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import start.GameConst;
        import start.Main;

public class GameMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button upgradeMenuButton;

    @FXML
    private Button saveGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        backButton.setOnAction(event ->
                Main.sceneToChange = GameConst.gameFieldID
        );
        upgradeMenuButton.setOnAction(event -> {
            Main.sceneToChange = GameConst.upgradeMenuID;
        });
        mainMenuButton.setOnAction(event ->
                Main.sceneToChange = GameConst.mainMenuID
        );
    }
}
