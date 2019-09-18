package start.objects;

public final class Point {
    public int x;
    public int y;

    public Point(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public Point(double newX, double newY) {
        x = (int) Math.floor(newX);
        y = (int) Math.floor(newY);
    }

    //Сумма координат
    public Point plus(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    //Разность координат
    public Point minus(Point other) {
        return new Point(x - other.x, y - other.y);
    }

    //Расстояние до точки от (0,0)
    public int distance() {
        return (int) Math.ceil(Math.sqrt(this.x * this.x + this.y * this.y));
    }

    //Расстояние между произвольными точками
    public int distance(Point other) {
        Point fromNull = this.minus(other);
        return fromNull.distance();
    }

    public Point abs(){
        return new Point(Math.abs(x), Math.abs(y));
    }

    public Point multiply(int multiplier) {
        return new Point(this.x * multiplier, this.y * multiplier);
    }

    public Point multiply(Point multiplier) {
        return new Point(this.x * multiplier.x, this.y * multiplier.y);
    }

    public int minOfCoord() {
        return Math.min(Math.abs(x), Math.abs(y));
    }

    public int maxOfCoord() {
        return Math.max(Math.abs(x), Math.abs(y));
    }

    public Point reverse() {
        return new Point(-this.x, -this.y);
    }

    public Point cutToLongest() {
        int newX = (Math.abs(this.x) > Math.abs(this.y) ? (int) (Math.signum(this.x)) : 0);
        int newY = (Math.abs(this.y) > Math.abs(this.x) ? (int) (Math.signum(this.y)) : 0);
        return new Point(newX, newY);
    }

    public Direction getDirection(Point other) {
        Point offset = other.minus(this).cutToLongest();
        if (offset.equals(Direction.NORTH.getOffset())) return Direction.NORTH;
        if (offset.equals(Direction.EAST.getOffset())) return Direction.EAST;
        if (offset.equals(Direction.WEST.getOffset())) return Direction.WEST;
        else return Direction.SOUTH;
    }

    //Изменяет координаты текущей точки на заданную величину
    public void inc(Point other) {
        x += other.x;
        y += other.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Point) {
            Point other = (Point) obj;
            return (this.x == other.x && this.y == other.y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = this.x;
        result += 29 * this.y;
        return result;
    }

    @Override
    public String toString() {
        return ("(" + x + ", " + y + ')');
    }
}
