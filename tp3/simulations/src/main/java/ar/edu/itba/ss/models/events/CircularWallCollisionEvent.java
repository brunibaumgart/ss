package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CircularWallCollisionEvent extends CollisionEvent implements Comparable<CollisionEvent>{
    private final Particle p;

    public CircularWallCollisionEvent(double time, Particle p) {
        super(EventType.CIRCULAR_WALL_COLLISION, time);
        this.p = p;
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
