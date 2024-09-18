package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.*;
import ar.edu.itba.ss.models.events.CircularWallCollisionEvent;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.models.events.ParticleCollisionEvent;
import ar.edu.itba.ss.models.events.WallCollisionEvent;
import ar.edu.itba.ss.operators.NoGravityOperator;
import ar.edu.itba.ss.utils.CollisionUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MolecularDynamicsMethod {

    public static void runIteration(SimulationState simulationState){
        final List<Particle> particles = simulationState.particles();

        // If it's the first iteration, calculate tc for all particles
        if (simulationState.iteration() == 0) {
            for (Particle particle : particles) {
                final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(particle, particles, simulationState.L(), simulationState.isCircular());
                if (!collisions.isEmpty())
                    simulationState.events().addAll(collisions);
            }
        }

        // Get the event with the smallest time (tc)
        final CollisionEvent currEvent = simulationState.events().poll();

        if (currEvent == null)
            throw new IllegalStateException("No event for next iteration");
        final double minTc = currEvent.getTime();

        final List<Particle> particlesColliding = currEvent.getParticles();

        // Update positions
        particles.forEach(p -> {
            final Vector newPosition = p.position().add(p.speed().multiply(minTc));
            final Particle newParticle = new Particle(p.id(), p.radius(), newPosition, p.speed(), p.mass(),p.previousSpeed());
            particles.set(particles.indexOf(p), newParticle);
        });

        // Update speeds of the particles involved
        if (currEvent.getType() == CollisionEvent.EventType.PARTICLES_COLLISION) {
            ParticleCollisionEvent event = (ParticleCollisionEvent) currEvent;
            // Ensure p1 is not the brownian particle
            if(event.p1().id() == ParticleUtils.BROWNIAN_ID)
                event = new ParticleCollisionEvent(event.getTime(), event.p2(), event.p1());

            // Get updated particles
            final int indexP1 = particles.indexOf(event.p1());
            final int indexP2 = particles.indexOf(event.p2());
            final Particle p1 = particles.get(indexP1);
            final Particle p2 = particles.get(indexP2);

            final Pair<Vector, Vector> newSpeeds = NoGravityOperator.collide(p1, p2);

            final Particle newP1 = new Particle(p1.id(), p1.radius(), p1.position(), newSpeeds.first(), p1.mass(),p1.speed());
            final Particle newP2 = new Particle(p2.id(), p2.radius(), p2.position(), newSpeeds.second(), p2.mass(),p2.speed());

            particles.set(indexP1, newP1);
            particles.set(indexP2, newP2);
        } else {
            if (simulationState.isCircular()){
                final CircularWallCollisionEvent event = (CircularWallCollisionEvent) currEvent;

                // Get updated particle
                final int indexP = particles.indexOf(event.p());
                final Particle p = particles.get(indexP);

                final Vector newSpeed = NoGravityOperator.collideWithCircularWall(p,
                        new Vector(simulationState.L()/2, simulationState.L()/2));
                final Particle newP = new Particle(p.id(), p.radius(), p.position(), newSpeed, p.mass(), p.speed());
                particles.set(indexP, newP);
            }
            else {
                final WallCollisionEvent event = (WallCollisionEvent) currEvent;

                // Get updated particle
                final int indexP = particles.indexOf(event.p());
                final Particle p = particles.get(indexP);

                final Vector newSpeed = NoGravityOperator.collideWithWall(p, event.wall());
                final Particle newP = new Particle(p.id(), p.radius(), p.position(), newSpeed, p.mass(),p.speed());
                particles.set(indexP, newP);
            }
        }

        // Update all events
        simulationState.events().removeIf(e -> e.containsParticles(particlesColliding));  // Remove stale events
        simulationState.events().forEach(e -> e.setTime(e.getTime() - minTc)); // Update time of remaining events

        // Get updated particles
        final List<Particle> updatedParticlesColliding = new ArrayList<>();
        for (Particle collidingParticle : particlesColliding) {
            int particle_index = particles.indexOf(collidingParticle);
            updatedParticlesColliding.add(particles.get(particle_index));
        }

        // Add new events of the colliding particles
        updatedParticlesColliding.forEach(p -> {
            final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(p, particles, simulationState.L(),  simulationState.isCircular());
            if (!collisions.isEmpty())
                simulationState.events().addAll(collisions);
        });

        // Increment iteration
        simulationState.incrementIteration();

        // Add time to the box state
        simulationState.addTime(minTc);
        simulationState.lastEvent(currEvent);
    }
}
