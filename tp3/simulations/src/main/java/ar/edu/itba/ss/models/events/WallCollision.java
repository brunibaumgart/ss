package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class WallCollision extends Event {
    private final Particle p;
    private final Wall wall;

    public WallCollision(double time, Particle p, Wall wall) {
        super(time, EventType.WALL_COLLISION);
        this.p = p;
        this.wall = wall;
    }
}
