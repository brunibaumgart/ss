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
