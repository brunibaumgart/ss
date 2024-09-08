package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class WallCollisionEvent extends CollisionEvent implements Comparable<CollisionEvent> {
    private final Particle p;
    private final Wall wall;

    public WallCollisionEvent(double time, Particle p, Wall wall) {
        super(time, EventType.WALL_COLLISION);
        this.p = p;
        this.wall = wall;
    }

    @Override
    public List<Particle> getParticles() {
        final List<Particle> particles = new ArrayList<>();
        particles.add(p);
        return particles;
    }

    @Override
    public boolean containsParticles(List<Particle> particles) {
        return particles.contains(p);
    }
}
