package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class MolecularDynamicsMethod {


    public static List<Particle> runIteration(List<Particle> particles){
        double tc = 0;
        List<Particle> newParticles = new ArrayList<>();

        // 2. Se calcula el tiempo hasta el primer choque
        for (Particle particle: particles){
            // 2.a Calculo partículas vecinas (con las que podría chocar)
            final List<Particle> collisionParticles = calculateCollisionParticles(particles, particle);
            // 2.b Calculo tc para cada una de ellas

        }
        // 3. Se evolucionan todas las partículas según sus ecuaciones de movimiento hasta tc

        return newParticles;
    }

    private static List<Particle> calculateCollisionParticles(List<Particle> particles, Particle particle){
        List<Particle> collisionParticles = new ArrayList<>();
        final double angle = particle.position().angle();
        final double x = particle.position().x();
        final double y = particle.position().y();

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
}
