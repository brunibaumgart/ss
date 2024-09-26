package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.SystemParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VerletAlgorithm {
    public static void runIteration(SimulationState state, double deltaT) {
       final List<Particle> updatedParticles = new ArrayList<>();

       state.particles().forEach(p -> {
           final Optional<Particle> prevParticle = state.particles().parallelStream().filter(particle -> particle.id() == p.id() - 1).findFirst();
           final Optional<Particle> nextParticle = state.particles().parallelStream().filter(particle -> particle.id() == p.id() + 1).findFirst();

           final double newPosition = 2 * p.position()
                   - p.lastPosition()
                   + deltaT*deltaT * p.force(state.systemParameters(), prevParticle, nextParticle) / p.mass();

           final double newVelocity = (newPosition - p.lastPosition()) / (2 * deltaT);

           final Particle updatedParticle = new Particle(p.id(), newPosition, p.position(), newVelocity, p.velocity(), p.mass());

           updatedParticles.add(updatedParticle);
       });

       state.particles(updatedParticles);

       state.addTime(deltaT);
       state.addIteration();
    }
}
