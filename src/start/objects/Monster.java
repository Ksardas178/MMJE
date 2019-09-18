package start.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import start.GameConst;
import start.Main;

public class Monster extends Pane {
    private ImageView imageView;
    private int width;
    private int height;
    private Direction direction = Direction.random();
    private Point placement;
    private boolean alive = false;
    private boolean hunting = false;
    private Point offset;
    private SpriteAnimation animation;

    public Monster(ImageView imageView, int width, int height) {
        this.height = height;
        this.width = width;
        this.imageView = imageView;
        int offsetX = 0;
        int offsetY = 0;
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        int count = 4;
        int columns = 4;
        animation = new SpriteAnimation(imageView, Duration.millis(GameConst.animDuration), count, columns, offsetX, offsetY, width, height);
        this.offset = new Point(width / 2, height / 2);
        remove();
        getChildren().addAll(imageView);
    }

    //Позиция верхнего левого угла картинки
    public Point getPlacement() {
        return new Point((int) Math.floor(this.getTranslateX()), (int) Math.floor(this.getTranslateY()));
    }

    //Добавление монстра на карту
    public void placeOnMap() {
        this.imageView.setViewport(new Rectangle2D(0, 0, width, height));
        this.setTranslateX(placement.x);
        this.setTranslateY(placement.y);
    }

    public void spawn() {
        if (!alive) {
            hunting = false;
            alive = true;
            this.changeRoute();
            Point newPlacement = GameConst.getRandomPlacement(Main.player.direction);
            newPlacement.inc(Main.player.getPlacement());
            newPlacement.inc(offset);
            placement = newPlacement;
            this.placeOnMap();
        }
    }

    //Позиция ног монстра
    public Point getRealPlacement() {
        return new Point((int) Math.floor(this.getTranslateX()) + GameConst.monsterPictureSize.x / 2,
                (int) Math.floor(this.getTranslateY()) + GameConst.monsterPictureSize.y);
    }

    public Point getBodyPlacement() {
        return new Point((int) Math.floor(this.getTranslateX()) + GameConst.monsterPictureSize.x / 2,
                (int) Math.floor(this.getTranslateY()) + GameConst.monsterPictureSize.y/2);
    }

    public void patrolling() {
        if (alive) {
            catchHero();
            animation.setOffsetY(height * direction.getLine());
            animation.play();
            if (!hunting) {
                move(direction.getOffset().multiply(GameConst.monsterPatrolSpeed));
                if (this.getRealPlacement().distance(Main.player.getRealPlacement()) < GameConst.monsterSight)
                    hunting = true;
            } else {
                direction = this.getRealPlacement().getDirection(Main.player.getRealPlacement());
                move(direction.getOffset().multiply(GameConst.monsterRunSpeed));

            }
        }
    }

    public void changeRoute(){
        if (!hunting) {
            if (Math.random()<=GameConst.chanceOfRandomTrueSight) this.direction = this.getRealPlacement().getDirection(Main.player.getRealPlacement());
            else this.direction = Direction.random();
        }
    }

    public void draw() {
        patrolling();
    }

    public void move(Point offset) {
            this.placement.inc(offset);
            this.setTranslateX(placement.x);
            this.setTranslateY(placement.y);
    }

    public void correctionMove(Point offset) {
        if (alive) {
            this.move(offset);
        }
    }

    public void remove(){
        alive = false;
        hunting = false;
        placement = GameConst.hiddenPosition;
        placeOnMap();
    }

    public void catchHero() {
        final int dst = this.getRealPlacement().distance(Main.player.getRealPlacement());
        if (dst>GameConst.removeDistance) {
            remove();
        }
        if (dst<GameConst.catchDistance) {
            Main.sceneToChange = GameConst.endgameMenuID;
            remove();
        }
    }

    public void treeCollision(Point place) {
    }
}