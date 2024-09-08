package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.events.Event;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.PriorityQueue;

@Getter
@Accessors(fluent = true)
public class BoxState {

    @Setter
    List<Particle> particles;
    @Setter
    PriorityQueue<Event> events;
    int iteration;
    final Double L;

    public BoxState(List<Particle> particles, double L) {
        this.particles = particles;
        this.events = new PriorityQueue<>();
        this.iteration = 0;
        this.L = L;
    }

    public void incrementIteration(){
        this.iteration++;
    }

}
