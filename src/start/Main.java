package start;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import start.objects.*;
import start.objects.Character;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private Stage primaryStage;
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public static ArrayList<Rectangle> bonuses = new ArrayList<>();
    private int counter = 0;
    public static int sceneToChange = GameConst.mainMenuID;
    private int currentScene = GameConst.mainMenuID;
    public static Trees forest;

    public static Pane inGameRoot = new Pane();
    private static Canvas canvas = new Canvas(GameConst.prefWindowSize.x, GameConst.prefWindowSize.y);
    static GraphicsContext gc = canvas.getGraphicsContext2D();

    public static Character player;
    public static Monster monster;
    public static Fireball fireball;

    private boolean farEnoughToDelete(Point place) {
        return place.distance(player.getPlacement()) > GameConst.deleteArea;
    }

    private Rectangle generateBonus() {
        Point placement = GameConst.getRandomPlacement(player.direction);
        Rectangle rect = new Rectangle(20, 20, Color.GREEN);
        rect.setX(placement.x + player.getTranslateX());
        rect.setY(placement.y + player.getTranslateY());
        return rect;
    }

    private void firstBonusesGen(int amount) {
        bonuses.clear();
        for (int i = amount; i < GameConst.bonusesAmount; i++) {
            Rectangle rect = generateBonus();
            bonuses.add(i, rect);
            inGameRoot.getChildren().addAll(rect);
        }
    }

    private void renewBonuses(int cycle) {
        int cyclePerBonus = GameConst.updateInterval / GameConst.bonusesAmount;
        int i = cycle % GameConst.updateInterval / cyclePerBonus;
        if (i < bonuses.size() && cycle % cyclePerBonus == 0) {
            Rectangle rect = bonuses.get(i);
            Point bonusPlacement = new Point(rect.getX(), rect.getY());
            if (farEnoughToDelete(bonusPlacement)) {
                bonuses.remove(rect);
                inGameRoot.getChildren().remove(rect);
                rect = generateBonus();
                bonuses.add(i, rect);
                inGameRoot.getChildren().addAll(rect);
            }
            firstBonusesGen(bonuses.size());//Возвращаем "съеденные" объекты
        }
    }

    private void update() {
        if (isPressed(KeyCode.UP)) {
            player.moveDir(Direction.NORTH);
        } else if (isPressed(KeyCode.DOWN)) {
            player.moveDir(Direction.SOUTH);
        } else if (isPressed(KeyCode.LEFT)) {
            player.moveDir(Direction.WEST);
        } else if (isPressed(KeyCode.RIGHT)) {
            player.moveDir(Direction.EAST);
        } else if (isPressed(KeyCode.ESCAPE)) {
            if (currentScene == GameConst.gameFieldID) sceneToChange = GameConst.gameMenuID;
        } else if (isPressed(KeyCode.F)) {
            fireball.shoot();
        } else {
            player.stopAnimation();
        }
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    private void changeScene(String fxml) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(fxml));
            primaryStage.getScene().setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeScene(Pane pane) {
        primaryStage.getScene().setRoot(pane);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setMinHeight(GameConst.minWindowSize.y);
        primaryStage.setMinWidth(GameConst.minWindowSize.x);

        primaryStage.setHeight(GameConst.prefWindowSize.y);
        primaryStage.setWidth(GameConst.prefWindowSize.x);

        //Preparing new game
        Image image = new Image(getClass().getResourceAsStream("assets/hero2.png"));
        ImageView imageView = new ImageView(image);
        player = new Character(imageView, GameConst.heroPictureSize.x, GameConst.heroPictureSize.y);
        inGameRoot.getChildren().addAll(player);
        player.direction = Direction.NORTH;

        image = new Image(getClass().getResourceAsStream("assets/monster.png"));
        imageView = new ImageView(image);
        monster = new Monster(imageView, GameConst.monsterPictureSize.x, GameConst.monsterPictureSize.y);
        inGameRoot.getChildren().addAll(monster);

        image = new Image(getClass().getResourceAsStream("assets/fireball.png"));
        imageView = new ImageView(image);
        fireball = new Fireball(imageView, GameConst.fireballPictureSize.x, GameConst.fireballPictureSize.y);
        inGameRoot.getChildren().addAll(fireball);

        inGameRoot.getChildren().add(canvas);
        inGameRoot.setPrefSize(GameConst.prefWindowSize.x, GameConst.prefWindowSize.y);

        firstBonusesGen(0);
        forest = new Trees();
        Scene gameField = new Scene(inGameRoot);
        gameField.setRoot(inGameRoot);

        gameField.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameField.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        Timer monsterSpawnTimer = new Timer((int) (GameConst.monsterSpawnTime*1000), e -> monster.spawn());
        Timer monsterMoveTimer = new Timer((int) (GameConst.monsterMoveTime*1000), e -> monster.changeRoute());

        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0,0, GameConst.prefWindowSize.x, GameConst.prefWindowSize.y);
                forest.draw();
                monster.draw();
                fireball.draw();
                update();
                renewBonuses(counter);
                forest.renew(counter + 1);
                forest.collision(counter + 2);
                counter++;
            }
        };

        /*AnimationTimer pauseTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                counter++;
            }
        };*/

        AnimationTimer sceneTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (currentScene != sceneToChange) {
                    switch (sceneToChange) {
                        case (GameConst.mainMenuID):
                            gameTimer.stop();
                            monsterSpawnTimer.stop();
                            changeScene("fxml/mainMenu.fxml");
                            break;
                        case (GameConst.gameFieldID):
                            gameTimer.start();
                            monsterSpawnTimer.start();
                            changeScene(inGameRoot);
                            break;
                        case (GameConst.gameMenuID):
                            gameTimer.stop();
                            monsterSpawnTimer.stop();
                            changeScene("fxml/gameMenu.fxml");
                            break;
                        case (GameConst.upgradeMenuID):
                            gameTimer.stop();
                            monsterSpawnTimer.stop();
                            changeScene("fxml/upgradeMenu.fxml");
                            break;
                        case (GameConst.endgameMenuID):
                            gameTimer.stop();
                            monsterSpawnTimer.stop();
                            changeScene("fxml/endgameMenu.fxml");
                            break;
                        case (GameConst.newGameFieldID):
                            gameTimer.start();
                            monsterSpawnTimer.start();
                            firstBonusesGen(0);
                            forest = new Trees();
                            forest.draw();
                            changeScene(inGameRoot);
                            monster.remove();
                            fireball.remove();
                            player.direction = Direction.NORTH;

                            sceneToChange = GameConst.gameFieldID;
                            break;
                    }
                    currentScene = sceneToChange;
                }
            }
        };

        sceneTimer.start();
        monsterMoveTimer.start();

        primaryStage.setTitle(GameConst.windowTitle);
        primaryStage.setScene(gameField);
        changeScene("fxml/mainMenu.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
