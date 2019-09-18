package start.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import start.GameConst;
import start.Main;

public class Fireball extends Pane {
    private ImageView imageView;
    private int width;
    private int height;
    private Direction direction = Main.player.direction;
    private Point placement;
    private boolean fire = false;
    private Point offset;
    private SpriteAnimation animation;

    public Fireball(ImageView imageView, int width, int height) {
        this.height = height;
        this.width = width;
        this.imageView = imageView;
        int offsetX = 0;
        int offsetY = 0;
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        int count = 4;
        int columns = 5;
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

    public void shoot() {
        if (!fire) {
            fire = true;
            direction = Main.player.direction;
            Point newPlacement = new Point(GameConst.prefWindowSize.x / 2, GameConst.prefWindowSize.y / 2);
            newPlacement.inc(offset.reverse());
            placement = newPlacement;
            this.placeOnMap();
        }
    }

    public Point getRealPlacement() {
        return new Point((int) Math.floor(this.getTranslateX()) + GameConst.fireballPictureSize.x / 2,
                (int) Math.floor(this.getTranslateY()) + GameConst.fireballPictureSize.y);
    }

    public void flying() {
        if (fire) {
            animation.setOffsetY(height * direction.getLine());
            animation.play();
            move(direction.getOffset().multiply(GameConst.fireballSpeed));
            checkShoot();
        }
    }

    public void move(Point offset) {
        this.placement.inc(offset);
        this.setTranslateX(placement.x);
        this.setTranslateY(placement.y);
    }

    public void correctionMove(Point offset) {
        if (fire) {
            this.move(offset);
        }
    }

    public void draw() {
        flying();
    }

    public void remove() {
        fire = false;
        placement = GameConst.hiddenPosition;
        placeOnMap();
    }

    public void checkShoot() {
        if (this.placement.distance(Main.player.getPlacement()) > GameConst.distToCorner) {
            remove();
        }
        if (this.placement.distance(Main.monster.getBodyPlacement()) < GameConst.fireballRange) {
            Main.monster.remove();
            Main.player.upAbility("experience", 1);
            this.remove();
        }
    }

    public void treeCollision(Point place) {

    }
}