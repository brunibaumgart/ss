package ar.edu.itba.ss.models.forces;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.List;

public interface Force {
    Vector apply(Particle particle, List<Particle> otherParticles);
}
