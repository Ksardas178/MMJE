package start;

import start.objects.Direction;
import start.objects.Point;

import java.util.ArrayList;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public final class GameConst {
    public final static String windowTitle = "Master Magician";
    public final static int heroSpeed = 3;
    public final static double heroStopSpeed = 0.25; // (<1) Больше значение - быстрее остановка
    public final static int animDuration = 900;
    public final static int treeAmount = 200;
    public final static int updateInterval = treeAmount + 100; // (treeAmount + X, X>=0)
    public final static int bonusesAmount = 10;
    public final static int genArea = 20;
    public final static Point prefWindowSize = new Point(1920, 1080);
    public final static Point minWindowSize = new Point(400, 300);

    //Монстры
    public final static double chanceOfRandomTrueSight = 0.2;//Шанс того, что монстр изначально выберет верное направление
    public final static int catchDistance = 20;
    public final static int removeDistance = 1050;
    public final static int monsterRunSpeed = 2;
    public final static int monsterPatrolSpeed = 1;
    public final static double monsterSpawnTime = 10;
    public final static double monsterMoveTime = 2;
    public final static int monsterSight = 300;

    //Файерболы
    public final static int fireballSpeed = 4;
    public final static int fireballReload = 5;//Предпросчитывать?
    public final static int fireballRange = 30;

    //ID сцен
    public final static int mainMenuID = 0;
    public final static int gameFieldID = 1;
    public final static int gameMenuID = 2;
    public final static int upgradeMenuID = 3;
    public final static int newGameFieldID = 4;
    public final static int endgameMenuID = 5;

    //Не трогать без нужды
    public final static Point hiddenPosition = new Point(-1500, -1500);
    public final static int typesOfTrees = 12;
    public final static Point treePictureSize = new Point(100, 200);
    public final static Point fireballPictureSize = new Point(51, 51);
    public final static Point heroPictureSize = new Point(36, 52);
    public final static Point monsterPictureSize = new Point(53, 74);

    //Коллизии
    public static int collisionArea = 30;
    public final static int closeRange = heroSpeed*10;//Предпросчет "зоны столкновений"

    //Туман
    public final static double fogRate = 5;//Уровень тумана
    public final static int fogPadding = 200;//Отступ зоны невидимости от края экрана (сужение зоны)
    public final static double nearFieldVisionRate = 1.4;

    //Предопределяемые константы

    //Количество циклов, затрачиваемое на обработку одного дерева (обновление)
    public final static int cyclePerTree = GameConst.updateInterval / GameConst.treeAmount;
    public final static int distToCorner = (int) ceil
            (sqrt(prefWindowSize.x * prefWindowSize.x + prefWindowSize.y * prefWindowSize.y));
    public final static int fogDistance = (int) ceil
            (sqrt(prefWindowSize.minOfCoord() * prefWindowSize.minOfCoord() * 2)) - fogPadding;
    public final static int deleteArea = distToCorner + genArea;

    public static Point getRandomPlacement(){
        int radius = (int) Math.floor(Math.random() * genArea) + distToCorner;
        int angle = (int) Math.floor(Math.random() * 360);
        int x = (int) Math.floor(Math.cos(Math.toRadians(angle)) * radius);
        int y = (int) Math.floor(Math.sin(Math.toRadians(angle)) * radius);
        return new Point(x, y);
    }

    //Возвращает случайные координаты по ходу движения (без поправки на положение игрока)
    public static Point getRandomPlacement(Direction currentDirection){
        int x;
        int y;
        if (currentDirection == Direction.NORTH){
            x = (int)Math.floor(Math.random() * prefWindowSize.x);
            y = (int)Math.floor(Math.random() * genArea)-genArea;
        } else if (currentDirection == Direction.SOUTH){
            x = (int)Math.floor(Math.random() * prefWindowSize.x);
            y = (int)Math.floor(Math.random() * genArea)+prefWindowSize.y;
        } else if (currentDirection == Direction.WEST){
            y = (int)Math.floor(Math.random() * prefWindowSize.y);
            x = (int)Math.floor(Math.random() * genArea)-genArea;
        }else if (currentDirection == Direction.EAST){
            y = (int)Math.floor(Math.random() * prefWindowSize.y);
            x = (int)Math.floor(Math.random() * genArea)+prefWindowSize.x;
        }else return getRandomPlacement();
        x -= prefWindowSize.x/2;
        y -= prefWindowSize.y/2;
        return new Point(x, y);
    }

    private static double distanceToCenter(Point point){
        return point.distance(new Point(prefWindowSize.x, prefWindowSize.y));
    }

    public static boolean farEnoughToDelete(Point place){
        return place.distance(Main.player.getPlacement()) > GameConst.deleteArea;//Переписать без игрока?
    }

    public static boolean collisionRisk(Point place){
        return place.distance(Main.player.getRealPlacement()) < GameConst.closeRange;//Переписать без игрока?
    }

    public static double[][] fogAllocation(double fogRate){
        double[][]result = new double[prefWindowSize.x][prefWindowSize.y];
        for (int i = 0; i<=prefWindowSize.x; i++) {
            for (int j = 0; i<=prefWindowSize.y; i++) {
                double currentDistance = distanceToCenter(new Point(i,j));
                result[i][j] = Math.pow(currentDistance/distToCorner, fogRate);
            }
        }
        return result;
    }
}
