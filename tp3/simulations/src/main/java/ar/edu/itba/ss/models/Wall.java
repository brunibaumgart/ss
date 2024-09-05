package ar.edu.itba.ss.models;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Wall {
    private final WallType type;
    private Vector position;

    public Wall(WallType type, double L) {
        this.type = type;
        switch (type) {
            case TOP -> this.position = new Vector(0, L);
            case BOTTOM, LEFT -> this.position = new Vector(0, 0);
            case RIGHT -> this.position = new Vector(L, 0);
        }
    }

    public enum WallType {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }
}
