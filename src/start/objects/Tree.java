package start.objects;

import javafx.scene.layout.Pane;
import start.GameConst;
import start.Main;

public class Tree extends Pane {
    int type;
    int width;
    int height;
    private Point placement;
    private Point offset;

    public Tree() {
        this.height = GameConst.treePictureSize.y;
        this.width = GameConst.treePictureSize.x;
        this.offset = new Point(-width/2, -height/2);
        this.type = (int)Math.floor(Math.random()*GameConst.typesOfTrees);
        placement = GameConst.getRandomPlacement();
        setOffset();
    }

    //Добавление дерева на карту
    public void placeOnMap(){
        this.setTranslateX(placement.x);
        this.setTranslateY(placement.y);
    }

    //Сдвиг от (0,0) к текущему положения героя
    public void setOffset() {
        this.placement.inc(Main.player.getPlacement());
        this.placement.inc(offset);
    }

    //Перемещение и изменение типа дерева
    public void reGen() {
        Point newPlacement = GameConst.getRandomPlacement(Main.player.direction);
        newPlacement.inc(Main.player.getPlacement());
        newPlacement.inc(offset);
        placement = newPlacement;
        this.type = (int)Math.floor(Math.random()*GameConst.typesOfTrees);//Выбираем случайный тип дерева
        this.placeOnMap();
    }

    public void correctionMove(Point offset){
        this.placement.inc(offset);
    }

    public int getType(){
        return type;
    }

    //Позиция верхнего левого угла картинки
    public Point getPlacement() {
        return new Point((int) Math.floor(this.placement.x), (int) Math.floor(this.placement.y));
    }

    //Позиция основания ствола дерева
    public Point getRealPlacement() {
        return new Point((int) Math.floor(this.placement.x)+GameConst.treePictureSize.x/2,
                (int) Math.floor(this.placement.y)+GameConst.treePictureSize.y);
    }

    public void checkCollision(){
        if (this.getRealPlacement().distance(Main.player.getRealPlacement())<GameConst.collisionArea) {
            //Main.keys.clear();
            Main.player.treeCollision(this.getRealPlacement());
        }
    }
}
