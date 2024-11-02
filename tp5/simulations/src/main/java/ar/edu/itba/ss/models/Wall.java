package ar.edu.itba.ss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class Wall {
    public Side side;
    private final Vector from;
    private final Vector to;

    public Vector closestPoint(final Vector point) {
        switch(side) {
            case LEFT:
                return new Vector(Math.min(from.x(), to.x()), point.y());
            case UP:
                return new Vector(point.x(), Math.max(from.y(), to.y()));
            case DOWN:
                return new Vector(point.x(), Math.min(from.y(), to.y()));
            default:
                throw new IllegalArgumentException("Invalid side");
        }
    }

    public enum Side {
        LEFT, UP, DOWN
    }
}
