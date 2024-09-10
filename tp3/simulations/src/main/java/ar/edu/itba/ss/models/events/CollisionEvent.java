package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class CollisionEvent implements Comparable<CollisionEvent> {
    private final EventType type;
    @Setter
    private double time;

    @Override
    public int compareTo(CollisionEvent o) {
        return Double.compare(time, o.time);
    }

    public abstract List<Particle> getParticles();

    public abstract boolean containsParticles(List<Particle> particles);

    public enum EventType {
        PARTICLES_COLLISION,
        WALL_COLLISION,
    }

}
