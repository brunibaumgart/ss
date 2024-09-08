package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.events.CollisionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.PriorityQueue;

@Getter
@Accessors(fluent = true)
public class BoxState {

    @Setter
    private List<Particle> particles;
    @Setter
    private PriorityQueue<CollisionEvent> events;
    private int iteration;
    private final Double L;

    public BoxState(final List<Particle> particles, final double L) {
        this.particles = particles;
        this.events = new PriorityQueue<>();
        this.iteration = 0;
        this.L = L;
    }

    public void incrementIteration(){
        this.iteration++;
    }

}
