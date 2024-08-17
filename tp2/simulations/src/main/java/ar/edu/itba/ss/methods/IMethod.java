package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.Particle;

import java.util.List;
import java.util.Map;

public interface IMethod {
    /**
     * Calculate the neighbors of each particle
     *
     * @param rc cutoff radius
     * @return a map with the particle and its neighbors
     */
    Map<Particle, List<Particle>> calculateNeighbors(Double rc);
}
