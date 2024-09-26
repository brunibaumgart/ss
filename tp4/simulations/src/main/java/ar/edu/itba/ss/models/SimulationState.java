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
    private final SystemParameters systemParameters;

    public SimulationState(final List<Particle> particle, final SystemParameters systemParameters) {
        this.particles = particle;
        this.systemParameters = systemParameters;
    }

    public void addTime(final double time) {
        timeElapsed += time;
    }

    public void addIteration() {
        iteration++;
    }
}
