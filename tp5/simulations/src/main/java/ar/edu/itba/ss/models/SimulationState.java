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
    private final double height;
    private final List<Wall> walls;

    public SimulationState(final List<Particle> particles, final double width, final double height, final List<Wall> walls) {
        this.particles = particles;
        this.width = width;
        this.height = height;
        this.walls = walls;
    }

    public void addTime(final double deltaT) {
        timeElapsed += deltaT;
    }

    public void addIteration() {
        iteration++;
    }
}
