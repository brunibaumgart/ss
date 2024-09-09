package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.*;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.models.events.ParticleCollisionEvent;
import ar.edu.itba.ss.models.events.WallCollisionEvent;
import ar.edu.itba.ss.operators.NoGravityOperator;
import ar.edu.itba.ss.utils.CollisionUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class MolecularDynamicsMethod {
    public static BoxState runIteration2(BoxState boxState) {
        final List<Particle> particles = boxState.particles();

        // If it's the first iteration, calculate tc for all particles
        if(boxState.iteration() == 0) {
            for (Particle particle : particles) {
                final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(particle, particles, boxState.L());
                if(!collisions.isEmpty())
                    boxState.events().add(collisions.peek());
            }
        }

        // Get the event with the smallest time (tc)
        final CollisionEvent currEvent = boxState.events().poll();
        if (currEvent == null)
            throw new IllegalStateException("No event for next iteration");
        final double minTc = currEvent.getTime();

        final List<Particle> particlesColliding = currEvent.getParticles();

        // Update positions
        particles.forEach(p -> {
            final Vector newPosition = p.position().add(p.speed().multiply(minTc));
            final Particle newParticle = new Particle(p.id(), p.radius(), newPosition, p.speed(), p.mass());
            particles.set(particles.indexOf(p), newParticle);
        });

        // Update speeds of the particles involved
        if (currEvent.getType() == CollisionEvent.EventType.PARTICLES_COLLISION) {
            final ParticleCollisionEvent event = (ParticleCollisionEvent) currEvent;
            final Particle p1 = event.p1();
            final Particle p2 = event.p2();

            final Pair<Vector, Vector> newSpeeds = NoGravityOperator.collide(p1, p2);

            final Particle newP1 = new Particle(p1.id(), p1.radius(), p1.position(), newSpeeds.first(), p1.mass());
            final Particle newP2 = new Particle(p2.id(), p2.radius(), p2.position(), newSpeeds.second(), p2.mass());

            particles.set(particles.indexOf(p1), newP1);
            particles.set(particles.indexOf(p2), newP2);
        } else {
            final WallCollisionEvent event = (WallCollisionEvent) currEvent;
            final Particle p = event.p();
            final Wall wall = event.wall();

            final Vector newSpeed = NoGravityOperator.collideWithWall(p, wall);
            final Particle newP = new Particle(p.id(), p.radius(), p.position(), newSpeed, p.mass());

            particles.set(particles.indexOf(p), newP);
        }

        // Update all events
        boxState.events().removeIf(e -> e.containsParticles(particlesColliding));
        boxState.events().forEach(e -> e.setTime(e.getTime() - minTc));

        // Add new events of the colliding particles
        particlesColliding.forEach(p -> {
            final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(p, particles, boxState.L());
            if(!collisions.isEmpty())
                boxState.events().add(collisions.peek());
        });

        // Increment iteration
        boxState.incrementIteration();

        return boxState;
    }

    public static BoxState runIteration(BoxState boxState) {
        final List<Particle> particles = boxState.particles();

        // 1. Si es la primera iteración, calculo tc para todas las particulas
        if (boxState.iteration() == 0){
            for (Particle particle : particles) {
                final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(particle, particles, boxState.L());
                if(!collisions.isEmpty())
                    boxState.events().add(collisions.peek());
            }
        }

        // 2. Obtengo el evento con menor tiempo (tc)
        final CollisionEvent nextEvent = boxState.events().poll();
        if (nextEvent == null)
            throw new IllegalStateException("No event for next iteration");

        final double minTc = nextEvent.getTime();
        final List<Particle> nextEventParticles = nextEvent.getParticles();

        // 3. Recalculo todas las posiciones
        final List<Particle> newParticles = ParticleUtils.updateParticlesPosition(particles, minTc);

        // 4. Recalculo velocidades de las particulas involucradas
        if (nextEvent.getType() == CollisionEvent.EventType.PARTICLES_COLLISION) {
            final ParticleCollisionEvent event = (ParticleCollisionEvent) nextEvent;
            final Particle p1 = event.p1();
            final Particle p2 = event.p2();

            final Pair<Vector, Vector> speeds = NoGravityOperator.collide(p1, p2);

            for (int i = 0; i < 2; i++) {
                final Particle particle = i == 0 ? p1 : p2;

                final Optional<Particle> newParticle = newParticles.stream().filter(p -> p.equals(particle)).findFirst();
                if(newParticle.isEmpty())
                    throw new IllegalStateException("Error updating particles");
                newParticle.ifPresent(newParticles::remove);

                final Particle updatedParticle = new Particle(
                        particle.id(),
                        particle.radius(),
                        particle.position(),
                        i == 0 ? speeds.first() : speeds.second(),
                        particle.mass()
                );

                newParticles.add(updatedParticle);
            }
        }
        else {
            final WallCollisionEvent event = (WallCollisionEvent) nextEvent;
            final Particle p1 = event.p();
            final Wall wall = event.wall();

            final Vector speed = NoGravityOperator.collideWithWall(p1, wall);

            final Optional<Particle> newParticle = newParticles.stream().filter(p -> p.equals(p1)).findFirst();
            if (newParticle.isEmpty())
                throw new IllegalStateException("Error updating particles");
            newParticle.ifPresent(newParticles::remove);

            final Particle updatedSpeedParticle = new Particle(p1.id(), p1.radius(), p1.position(), speed, p1.mass());
            newParticles.add(updatedSpeedParticle);
        }

        // 5. Updateo todos los eventos
        final PriorityQueue<CollisionEvent> newEvents= new PriorityQueue<>();

        for (CollisionEvent event : boxState.events()){
            if (!event.containsParticles(nextEventParticles)){
                event.setTime(event.getTime() - minTc);
                newEvents.add(event);
            }
        }

        for (Particle particle: nextEventParticles) {
            final Optional<Particle> updatedParticle = newParticles.stream().filter(p -> p.equals(particle)).findFirst();
            if(updatedParticle.isEmpty())
                throw new IllegalStateException("Error updating particles");

            final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(updatedParticle.get(), newParticles, boxState.L());
            newEvents.add(collisions.peek());
        }
        boxState.events(newEvents);
        boxState.particles(newParticles);

        // 5. Incremento la iteración
        boxState.incrementIteration();

        return boxState;
    }

}
