package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell {
    private final int x, y;
    private final List<Particle> particles;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.particles = new ArrayList<>();
    }

    public Cell(int x, int y, List<Particle> particles) {
        this.x = x;
        this.y = y;
        this.particles = new ArrayList<>();
        this.particles.addAll(particles);
    }

    public void addParticle(Particle particle) {
        this.particles.add(particle);
    }

    public List<Particle> getParticles() {
        return Collections.unmodifiableList(this.particles);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Cell{x=%d, y=%d, particles=%s}", x, y, particles);
    }
}
