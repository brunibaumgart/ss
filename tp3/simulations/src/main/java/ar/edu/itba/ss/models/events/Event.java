package ar.edu.itba.ss.models.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event implements Comparable<Event> {
    private final double time;
    private final EventType type;

    @Override
    public int compareTo(Event o) {
        return Double.compare(time, o.time);
    }

    public enum EventType {
        PARTICLES_COLLISION,
        WALL_COLLISION,
    }
}
