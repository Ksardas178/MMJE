package start.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import start.GameConst;
import start.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Character extends Pane {
    private int columns = 4;
    private int width;
    private int height;
    private int score = 0;
    private boolean alreadyMoving = false;
    public Direction direction;
    private double speed;
    private SpriteAnimation animation;

    private HashMap<String, Integer> abilities = new HashMap<>();

    public Character(ImageView imageView, int width, int height) {
        this.height = height;
        this.width = width;
        int offsetX = 0;
        int offsetY = 0;
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        int count = 4;
        animation = new SpriteAnimation(imageView, Duration.millis(GameConst.animDuration), count, columns, offsetX, offsetY, width, height);
        this.setTranslateX((GameConst.prefWindowSize.x - this.width) / 2);
        this.setTranslateY((GameConst.prefWindowSize.y - this.height) / 2);
        getChildren().addAll(imageView);

        abilities.put("concentration", 0);
        abilities.put("endurance", 0);
        abilities.put("strength", 0);
        abilities.put("healing", 0);
        abilities.put("mana", 0);
        abilities.put("experience", 0);
    }

    //Позиция верхнего левого угла картинки
    public Point getPlacement() {
        return new Point((int) Math.floor(this.getTranslateX()), (int) Math.floor(this.getTranslateY()));
    }

    //Позиция ног персонажа
    public Point getRealPlacement() {
        return new Point((int) Math.floor(this.getTranslateX()) + GameConst.heroPictureSize.x / 2,
                (int) Math.floor(this.getTranslateY()) + GameConst.heroPictureSize.y);
    }

    public void moveDir(Direction direction) {
        this.direction = direction;
        if (!alreadyMoving) {
            animation.setOffsetX(0);
        }
        animation.setOffsetY(height * direction.getLine());
        animation.play();
        move(direction.getOffset(), GameConst.heroSpeed);
        alreadyMoving = true;
    }

    public void upAbility(String abilName, int lvlUp){
        int currLvl = abilities.get(abilName);
        abilities.put(abilName, currLvl+lvlUp);
    };

    public String getAbility(String abilName){
        return abilities.get(abilName).toString();
    };

    public double getAbilityPercentage(String abilName){
        final int max = Collections.max(abilities.values());
        if (max!=0) {
            return abilities.get(abilName)/(double)max;
        } else return 0;
    };

    public void stopAnimation() {
        if (alreadyMoving) {
            animation.stop();//Останавливаем с целью начать рисовать новую анимацию с первого кадра
            animation.setOffsetX(width * columns);
            animation.play();
            speed = GameConst.heroSpeed;
        }
        alreadyMoving = false;
        if (animation.getCurrentRate() == 1) {
            move(direction.getOffset(), (int) Math.ceil(speed));
            if (speed > 0) speed -= GameConst.heroStopSpeed;
        }
    }

    private void move(Point offsetDirection, int speed) {
        Point offset = offsetDirection.multiply(speed).reverse();
        Main.forest.correctionMove(offset);
        Main.monster.correctionMove(offset);
        Main.fireball.correctionMove(offset);
    }

    public void isBonusEat() {
        ArrayList<Rectangle> bonusesToRemove = new ArrayList<>();
        Main.bonuses.forEach(rect -> {
            if (this.getBoundsInParent().intersects(rect.getBoundsInParent())) {
                bonusesToRemove.add(rect);
                score++;
                System.out.println(score);
            }
        });
        bonusesToRemove.forEach((rect -> {
            Main.bonuses.remove(rect);
            Main.inGameRoot.getChildren().remove(rect);
        }));
    }

    public void treeCollision(Point place) {
        Point offset = this.getRealPlacement().minus(place);
        move(offset.multiply(direction.getOffset().abs()), 1);
    }
}