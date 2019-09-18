package start;

import javafx.scene.image.Image;
import start.objects.Point;
import start.objects.Tree;

import java.util.ArrayList;

public class Trees {
    private ArrayList<Tree> trees = new ArrayList<>();
    private static ArrayList<Tree> closeTrees = new ArrayList<>();
    private static ArrayList<Image> treeImgs = new ArrayList<>();

    Trees() {
        //Генерация новых деревьев (круг)
        for (int i = 0; i < GameConst.treeAmount; i++) {
            Tree newTree = new Tree();
            trees.add(i, newTree);
            newTree.placeOnMap();
        }
        for (int i = 0; i < GameConst.typesOfTrees; i++) {
            String imgPath = "assets/tree_" + i + ".png";
            treeImgs.add(i, new Image(getClass().getResourceAsStream(imgPath)));
        }
    }

    //Обновление деревьев (по ходу движения игрока)
    public void renew(int cycle) {
        int i = cycle % GameConst.updateInterval / GameConst.cyclePerTree;

        //Обновление (обработка части массива деревьев на каждой итерации, регулируем константами)
        if (i < trees.size()) {
            Tree tree = trees.get(i);
            if (GameConst.farEnoughToDelete(tree.getPlacement())) {
                trees.remove(i);
                tree.reGen();
                trees.add(i, tree);
                tree.placeOnMap();
            }
        }
    }

    public void correctionMove(Point offset) {
        trees.forEach(tree -> tree.correctionMove(offset));
    }

    public void draw() {
        trees.forEach(tree -> {
            final double dst = tree.getRealPlacement().distance(Main.player.getRealPlacement());
            if (dst < GameConst.fogDistance) {
                Point placement = tree.getPlacement();
                int idx = tree.getType();
                final double opacity = Math.pow((1 - dst / GameConst.fogDistance),
                        GameConst.fogRate) * GameConst.nearFieldVisionRate;
                Main.gc.setGlobalAlpha(opacity);
                Main.gc.drawImage(treeImgs.get(idx), placement.x, placement.y,
                        GameConst.treePictureSize.x, GameConst.treePictureSize.y);
            }
        });
    }

    //Обработка столкновений
    public void collision(int cycle) {
        int cyclesPerTrees = GameConst.closeRange / GameConst.heroSpeed;
        int treesPerCycle = GameConst.treeAmount / cyclesPerTrees + 1;
        int currentCycleNumber = cycle % cyclesPerTrees;

        //Обновление closeTrees (обработка части массива деревьев на каждой итерации, регулируем константами)
        for (int i = currentCycleNumber * treesPerCycle; i <= (currentCycleNumber + 1) * treesPerCycle && i < trees.size(); i++) {
            Tree tree = trees.get(i);
            if (GameConst.collisionRisk(tree.getRealPlacement())) {
                closeTrees.add(tree);
            } else closeTrees.remove(tree);
        }

        //if (cycle%GameConst.collisionArea%2 == 0) {
        closeTrees.forEach(Tree::checkCollision);//Проверка ближайших деревьев на коллизии
        //}
    }
}
