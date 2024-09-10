package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create particles in the space without overlapping
     *
     * @param particles list of particles. not modified
     * @param N         number of particles
     * @param L         length of the box
     * @param r         radius of the particles
     * @param speed     speed of particles
     * @param mass      mass of particles
     * @return a new list of particles containing the old ones and the new ones
     */
    public static List<Particle> createMovingParticles(
            final List<Particle> particles,
            final int N,
            final double L,
            final double r,
            final double speed,
            final double mass
    ) {
        final List<Particle> result = new ArrayList<>(particles);
        final Random random = new Random();

        for (int i = 0; i < N; i++) {
            final double angle = random.nextDouble() * 2 * Math.PI;
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            Particle p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, angle), mass);
            // check particles do not overlap
            while (collidesWithAny(p, result) || collidesWithWalls(p, L)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, angle), mass);
            }

            result.add(p);
        }

        return result;
    }

    public static List<Particle> updateParticlesPosition(List<Particle> particles, double minimumCollisionTime) {
        // TODO: check this
        return particles.stream()
                .map(particle -> updateParticlePosition(particle, minimumCollisionTime))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Particle updateParticlePosition(Particle particle, double minimumCollisionTime) {
        final Vector position = particle.position();
        final Vector speed = particle.speed();
        final Vector newPos = position.add(speed.multiply(minimumCollisionTime));

        return new Particle(particle.id(), particle.radius(), newPos, speed, particle.mass());
    }

    private static boolean collidesWithAny(final Particle particle, final List<Particle> particles) {
        return particles.stream().anyMatch(particle::collidesWith);
    }

    private static boolean collidesWithWalls(final Particle particle, final double L) {
        return particle.position().x() - particle.radius() <= 0 ||
                particle.position().x() + particle.radius() >= L ||
                particle.position().y() - particle.radius() <= 0 ||
                particle.position().y() + particle.radius() >= L;
    }
}
