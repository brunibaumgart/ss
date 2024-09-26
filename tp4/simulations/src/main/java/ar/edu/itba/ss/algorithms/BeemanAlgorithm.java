package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;

import java.util.ArrayList;
import java.util.List;

public class BeemanAlgorithm {
    public static void runIteration(SimulationState state, double deltaT) {
        final List<Particle> updatedParticles = new ArrayList<>();

        state.particles().forEach(p -> {
            final double newPosition = p.position()
                    + p.velocity() * deltaT
                    + ((2.0 / 3.0) * p.acceleration(state.systemParameters())
                    - (1.0 / 6.0) * p.lastAcceleration(state.systemParameters())) * deltaT * deltaT;
            final double predictedVelocity = p.velocity()
                    + ((3.0 / 2.0) * p.acceleration(state.systemParameters())
                    - (1.0 / 2.0) * p.lastAcceleration(state.systemParameters())) * deltaT;

            final double newAcceleration = p.force(state.systemParameters(), newPosition, predictedVelocity) / p.mass();
            final double newVelocity = p.velocity()
                    + ((1.0 / 3.0) * newAcceleration
                    + (5.0 / 6.0) * p.acceleration(state.systemParameters())
                    - (1.0 / 6.0) * p.lastAcceleration(state.systemParameters())) * deltaT;

            final Particle updatedParticle = new Particle(p.id(), newPosition, p.position(), newVelocity, p.velocity(), p.mass());
            updatedParticles.add(updatedParticle);
        });

        state.particles(updatedParticles);

        state.addTime(deltaT);
        state.addIteration();
    }
}
