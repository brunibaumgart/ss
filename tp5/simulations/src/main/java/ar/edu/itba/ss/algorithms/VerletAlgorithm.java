package ar.edu.itba.ss.algorithms;


import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;

public class VerletAlgorithm {
    public static void runIteration(SimulationState state, double deltaT) {
        final List<Particle> updatedParticles = new ArrayList<>();

        state.particles().forEach(p -> {
            final Vector newPosition = p.position().multiply(2)
                    .subtract(p.lastPosition())
                    .add(p.force().apply(p, state.particles()).multiply(deltaT * deltaT));

            final Vector newVelocity =  newPosition.subtract(p.lastPosition()).divide(2 * deltaT);

            final Particle.ParticleBuilder updatedParticleBuilder = Particle.builder()
                    .id(p.id())
                    .mass(p.mass())
                    .position(newPosition)
                    .lastPosition(p.position())
                    .force(p.force())
                    .radius(p.radius())
                    .velocity(newVelocity)
                    .desiredVelocity(p.desiredVelocity());

            final Particle updatedParticle;
            if (p.id() == -1 ){
                updatedParticle = updatedParticleBuilder.target(new Vector(state.width(), p.position().y())).build();
            }
            else {
                @SuppressWarnings("OptionalGetWithoutIsPresent")
                final Particle red = state.particles().stream().filter(aux->aux.id() == -1).findFirst().get();
                updatedParticle = updatedParticleBuilder.target(red.position()).build();
            }

            updatedParticles.add(updatedParticle);
        });

        state.particles(updatedParticles);

        state.addTime(deltaT);
        state.addIteration();
    }
}
