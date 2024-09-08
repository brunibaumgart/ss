package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class Event implements Comparable<Event> {
    @Setter
    double time;
    private final EventType type;

    @Override
    public int compareTo(Event o) {
        return Double.compare(time, o.time);
    }

    public enum EventType {
        PARTICLES_COLLISION,
        WALL_COLLISION,
    }

    public abstract List<Particle> getParticles();

    public abstract boolean containsParticles(List<Particle> particles);

}
