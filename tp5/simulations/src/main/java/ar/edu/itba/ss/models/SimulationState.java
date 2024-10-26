package ar.edu.itba.ss.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public class SimulationState {
    @Setter
    private List<Particle> particles;
    private double timeElapsed = 0.0;
    private int iteration = 0;
    private final double width;

    public SimulationState(final List<Particle> particles, final double width) {
        this.particles = particles;
        this.width = width;
    }

    public void addTime(final double deltaT) {
        timeElapsed += deltaT;
    }

    public void addIteration() {
        iteration++;
    }
}
