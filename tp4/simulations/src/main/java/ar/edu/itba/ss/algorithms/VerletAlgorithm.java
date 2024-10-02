package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VerletAlgorithm {
    public static void runIteration(SimulationState state, double deltaT) {
       final List<Particle> updatedParticles = new ArrayList<>();

       state.particles().forEach(p -> {
           final Optional<Particle> prevParticle = p.id() == 0
                   ? Optional.empty()
                   : Optional.ofNullable(state.particles().get(p.id() - 1));

           final Optional<Particle> nextParticle = p.id() == state.particles().size() - 1
                   ? Optional.empty()
                   : Optional.ofNullable(state.particles().get(p.id() + 1));

           final double newPosition;
           if(prevParticle.isEmpty() && nextParticle.isEmpty()) { // System 1
               throw new IllegalStateException();
           }
           else { // System 2
               if(p.id() == (state.particles().size() - 1)) {
                   newPosition = p.force(state.systemParameters(), nextParticle, prevParticle, state.timeElapsed());
               }
               else {
                   newPosition = 2 * p.position()
                           - p.lastPosition()
                           + deltaT*deltaT * p.force(state.systemParameters(), nextParticle, prevParticle, state.timeElapsed()) / p.mass();
               }
           }

           final double newVelocity = (newPosition - p.lastPosition()) / (2 * deltaT);

           final Particle updatedParticle = new Particle(p.id(), newPosition, p.position(), newVelocity, p.velocity(), p.mass());

           updatedParticles.add(updatedParticle);
       });

       state.particles(updatedParticles);

       state.addTime(deltaT);
       state.addIteration();
    }
}
