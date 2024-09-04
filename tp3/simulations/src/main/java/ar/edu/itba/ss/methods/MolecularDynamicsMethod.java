package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;

public class MolecularDynamicsMethod {


    public static List<Particle> runIteration(List<Particle> particles){
        double minorCollisionTime = 0;
        List<Particle> newParticles = new ArrayList<>();

        // 2. Se calcula el tiempo hasta el primer choque
        for (Particle particle: particles){

            // 2.a Calculo partículas vecinas (con las que podría chocar)
            final List<Particle> collisionParticles = calculateCollisionParticles(particles, particle);

            // 2.b Calculo tc para la particula seleccionada respecto a las paredes

            // 2.c Calculo tc para la partícula seleccionada respecto a otras partículas
            for (Particle collisionParticle: collisionParticles){
                final double collisionTime = calculateCollisionTime(particle, collisionParticle);
                if (collisionTime < minorCollisionTime){
                    minorCollisionTime = collisionTime;
                }
            }

        }
        // 3. Se evolucionan todas las partículas según sus ecuaciones de movimiento hasta tc

        return newParticles;
    }

    private static List<Particle> calculateCollisionParticles(List<Particle> particles, Particle particle){
        List<Particle> collisionParticles = new ArrayList<>();
        final double angle = particle.position().angle();
        final double x = particle.position().x();
        final double y = particle.position().y();

        // TODO: check border cases (angle = 90, for example)
        if (angle < 90){
            for (Particle potentialParticle: particles){
                if (potentialParticle.position().x() > x && potentialParticle.position().y() > y){
                    collisionParticles.add(potentialParticle);
                }
            }
        }
        else if (angle < 180){
            for (Particle potentialParticle: particles){
                if (potentialParticle.position().x() < x && potentialParticle.position().y() > y){
                    collisionParticles.add(potentialParticle);
                }
            }
        }
        else if (angle < 270){
            for (Particle potentialParticle: particles){
                if (potentialParticle.position().x() < x && potentialParticle.position().y() < y){
                    collisionParticles.add(potentialParticle);
                }
            }
        }
        else {
            for (Particle potentialParticle: particles){
                if (potentialParticle.position().x() > x && potentialParticle.position().y() < y){
                    collisionParticles.add(potentialParticle);
                }
            }
        }
        return collisionParticles;
    }

    private static double calculateCollisionTime(Particle particle, Particle otherParticle){
        final double particleX = particle.position().x();
        final double particleY = particle.position().y();
        final double otherParticleX = otherParticle.position().x();
        final double otherParticleY = otherParticle.position().y();

        final double speed = particle.speed().magnitude();

        final double sigma = particle.radius() + otherParticle.radius();
        final Vector deltaR = new Vector(otherParticleX - particleX, otherParticleY - particleY);
        final Vector deltaV = new Vector(speed*(otherParticleX - particleX), speed*(otherParticleY - particleY));

        final double deltaRDotDeltaR = deltaR.dot(deltaR);
        final double deltaVDotDeltaV = deltaV.dot(deltaV);
        final double deltaVDotDeltaR = deltaV.dot(deltaR);

        if (deltaVDotDeltaR >= 0){
            return Double.POSITIVE_INFINITY; // TODO: check this
        }

        final double d = Math.pow(deltaVDotDeltaR, 2) - (deltaVDotDeltaV)*(deltaRDotDeltaR - Math.pow(sigma,2));

        if (d < 0){
            return Double.POSITIVE_INFINITY; // TODO: check this
        }

        return -((deltaVDotDeltaR + Math.sqrt(d))/deltaVDotDeltaV);
    }
}
