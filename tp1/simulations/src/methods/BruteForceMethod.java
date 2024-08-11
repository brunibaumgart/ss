package methods;

import models.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForceMethod implements IMethod {
    private final List<Particle> particles;

    public BruteForceMethod(List<Particle> particles) {
        this.particles = particles;
    }

    public Map<Particle, List<Particle>> calculateNeighbors(Double rc) {
        final Map<Particle, List<Particle>> neighbors = new HashMap<>();

        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                final Particle particle = particles.get(i);
                final Particle neighbourParticle = particles.get(j);

                if (particle.distanceTo(neighbourParticle) <= rc) {
                    neighbors.computeIfAbsent(particle, k -> new ArrayList<>()).add(neighbourParticle);
                    neighbors.computeIfAbsent(neighbourParticle, k -> new ArrayList<>()).add(particle);
                }
            }
        }

        return neighbors;
    }
}
