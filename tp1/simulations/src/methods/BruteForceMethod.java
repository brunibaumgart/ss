package methods;

import models.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForceMethod implements IMethod {
    private final List<Particle> particles;
    private final Double L;
    private final boolean isPeriodic;

    public BruteForceMethod(Double L, boolean isPeriodic, List<Particle> particles) {
        this.particles = particles;
        this.L = L;
        this.isPeriodic = isPeriodic;
    }

    public Map<Particle, List<Particle>> calculateNeighbors(Double rc) {
        final Map<Particle, List<Particle>> neighbors = new HashMap<>();

        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                final Particle particle = particles.get(i);
                final Particle neighbourCandidate = particles.get(j);

                if (isNeighbour(particle, neighbourCandidate, rc)) {
                    neighbors.computeIfAbsent(particle, k -> new ArrayList<>()).add(neighbourCandidate);
                    neighbors.computeIfAbsent(neighbourCandidate, k -> new ArrayList<>()).add(particle);
                }
            }
        }

        return neighbors;
    }

    private boolean isNeighbour(Particle particle, Particle neighbourParticle, Double rc) {
        if (isPeriodic)
            return particle.periodicDistanceTo(neighbourParticle, this.L) <= rc;

        return particle.distanceTo(neighbourParticle) <= rc;
    }
}
