package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.events.CollisionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.PriorityQueue;

@Getter
@Accessors(fluent = true)
public class CircularState {
    private final Double radius;
    private final Vector center;
    @Setter
    private List<Particle> particles;
    @Setter
    private PriorityQueue<CollisionEvent> events;
    private int iteration = 0;
    private double timeElapsed = 0.0;
    @Setter
    private CollisionEvent lastEvent;


    public CircularState(final List<Particle> particles, final double L) {
        this.particles = particles;
        this.events = new PriorityQueue<>();
        this.radius = L/2;
        this.center = new Vector(radius, radius);
    }

    public void incrementIteration() {
        this.iteration++;
    }

    public void addTime(final double time) {
        this.timeElapsed += time;
    }
}
