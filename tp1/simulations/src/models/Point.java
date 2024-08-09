package models;

public class Point {
    private final Double x, y;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double distanceTo(Point other) {
        // TODO: Chequear también el radio
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point point = (Point) obj;
        return x.equals(point.x) && y.equals(point.y);
    }

    @Override
    public String toString() {
        return String.format("Point{x=%.2f, y=%.2f}", x, y);
    }
}
