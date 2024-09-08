package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.utils.CollisionUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MolecularDynamicsMethod {

    public static BoxState runIteration(BoxState boxState) {
        final List<Particle> particles = boxState.particles();

        // 1. Si es la primera iteración, calculo los tiempos minimos de todas las partículas
        if (boxState.iteration() == 0){
            for (Particle particle : particles) {
                final PriorityQueue<CollisionEvent> collisions = CollisionUtils.calculateAllCollisions(particle, particles, boxState.L());
                boxState.events().add(collisions.peek());
            }
        }

        // 2. Obtengo el evento con menor tiempo (tc)
        final CollisionEvent nextEvent = boxState.events().poll();
        if (nextEvent == null){
            throw new IllegalStateException("No event for next iteration");
        }
        final double minimumCollisionTime = nextEvent.getTime();

        // 3. Recalculo todas las posiciones segun mi tc
        List<Particle> newParticles = ParticleUtils.updateParticlesPosition(particles, minimumCollisionTime);
        boxState.particles(newParticles);

        // 4. Recalculo todos los eventos segun mi tc
        List<Particle> nextEventParticles = nextEvent.getParticles();
        final PriorityQueue<CollisionEvent> newEvents= new PriorityQueue<>();

        for (CollisionEvent event: boxState.events()){
            if (!event.containsParticles(nextEventParticles)){
                event.setTime(event.getTime()-minimumCollisionTime);
                newEvents.add(event);
            }
        }

        for (Particle particle: nextEventParticles){
            final PriorityQueue<CollisionEvent> particleCollisions = CollisionUtils.calculateAllCollisions(particle, particles, boxState.L());
            newEvents.add(particleCollisions.peek());
        }

        boxState.events(newEvents);

        // 5. Incremento la iteración
        boxState.incrementIteration();

        return boxState;
    }

    private static List<Particle> calculateCollisionParticles(List<Particle> particles, Particle particle) {
        final List<Particle> collisionParticles = new ArrayList<>();
        final double angle = particle.position().angle();
        final double x = particle.position().x();
        final double y = particle.position().y();

        // TODO: check border cases (angle = 90, for example)
        if (angle < 90) {
            for (Particle potentialParticle : particles) {
                if (potentialParticle.position().x() > x && potentialParticle.position().y() > y) {
                    collisionParticles.add(potentialParticle);
                }
            }
        } else if (angle < 180) {
            for (Particle potentialParticle : particles) {
                if (potentialParticle.position().x() < x && potentialParticle.position().y() > y) {
                    collisionParticles.add(potentialParticle);
                }
            }
        } else if (angle < 270) {
            for (Particle potentialParticle : particles) {
                if (potentialParticle.position().x() < x && potentialParticle.position().y() < y) {
                    collisionParticles.add(potentialParticle);
                }
            }
        } else {
            for (Particle potentialParticle : particles) {
                if (potentialParticle.position().x() > x && potentialParticle.position().y() < y) {
                    collisionParticles.add(potentialParticle);
                }
            }
        }
        return collisionParticles;
    }

    private static double calculateCollisionTime(Particle particle, Particle otherParticle) {
        final double sigma = particle.radius() + otherParticle.radius();
        final Vector deltaR = otherParticle.position().subtract(particle.position());
        final Vector deltaV = otherParticle.speed().subtract(particle.speed());

        final double deltaRDotDeltaR = deltaR.dot(deltaR);
        final double deltaVDotDeltaV = deltaV.dot(deltaV);
        final double deltaVDotDeltaR = deltaV.dot(deltaR);

        if (deltaVDotDeltaR >= 0) {
            return Double.POSITIVE_INFINITY; // TODO: check this
        }

        final double d = Math.pow(deltaVDotDeltaR, 2) - (deltaVDotDeltaV) * (deltaRDotDeltaR - Math.pow(sigma, 2));

        if (d < 0) {
            return Double.POSITIVE_INFINITY; // TODO: check this
        }

        return -((deltaVDotDeltaR + Math.sqrt(d)) / deltaVDotDeltaV);
    }
}
