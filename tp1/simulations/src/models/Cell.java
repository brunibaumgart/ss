package models;

import models.particle.IdentifiableParticle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell {
    private final List<IdentifiableParticle> particles;

    public Cell() {
        this.particles = new ArrayList<>();
    }

    public Cell(List<IdentifiableParticle> particles) {
        this.particles = new ArrayList<>();
        this.particles.addAll(particles);
    }

    public void addParticle(IdentifiableParticle particle) {
        this.particles.add(particle);
    }

    public List<IdentifiableParticle> getParticles() {
        return Collections.unmodifiableList(this.particles);
    }
}
