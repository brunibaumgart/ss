package models;

public record Point(Double x, Double y) {

    public Double distanceTo(Point other) {
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
