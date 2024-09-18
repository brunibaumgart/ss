package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.events.CollisionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.PriorityQueue;

@Getter
@Accessors(fluent = true)
public class SimulationState {

    private final Double L;
    @Setter
    private List<Particle> particles;
    @Setter
    private PriorityQueue<CollisionEvent> events;
    private int iteration = 0;
    private double timeElapsed = 0.0;
    @Setter
    private CollisionEvent lastEvent;
    private final boolean isCircular;

    public SimulationState(final List<Particle> particles, final double L, final boolean isCircular) {
        this.particles = particles;
        this.events = new PriorityQueue<>();
        this.L = L;
        this.isCircular = isCircular;
    }

    public void incrementIteration() {
        this.iteration++;
    }

    public void addTime(final double time) {
        this.timeElapsed += time;
    }
}