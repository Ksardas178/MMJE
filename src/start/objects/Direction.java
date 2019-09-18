package start.objects;

public enum Direction {
    NORTH(new Point(0, -1), 0),
    EAST(new Point(1, 0), 1),
    SOUTH(new Point(0, 1), 2),
    WEST(new Point(-1, 0), 3);

    private Point offset;
    private int animLine;

    //private
    Direction(Point offset, int animLine) {
        this.offset = offset;
        this.animLine = animLine;
    }

    public Point getOffset() {
        return offset;
    }

    public int getLine() {
        return animLine;
    }

    public static Direction random() {
        int n = (int) (Math.random() * 4);
        if (n == 0) {
            return Direction.NORTH;
        } else if (n == 1) {
            return Direction.EAST;
        } else if (n == 2) {
            return Direction.SOUTH;
        } else {
            return Direction.WEST;
        }
    }
}
