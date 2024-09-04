package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create particles in the space without overlapping
     *
     * @param N     number of particles
     * @param L     length of the box
     * @param r     radius of the particles
     * @param speed speed of particles
     * @param mass  mass of particles
     * @return list of particles
     */
    public static List<Particle> createMovingParticles(final int N, final double L, final double r, final double speed, final double mass) {
        final List<Particle> particles = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < N; i++) {
            final double angle = random.nextDouble() * 2 * Math.PI;
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            Particle p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, angle), mass);
            // check particles do not overlap
            while (ParticleUtils.collidesWithAny(p, particles)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(i, r, new Vector(x, y), Vector.fromPolar(speed, angle), mass);
            }

            particles.add(p);
        }

        return particles;
    }

    private static boolean collidesWithAny(final Particle particle, final List<Particle> particles) {
        return particles.parallelStream().anyMatch(particle::collidesWith);
    }
}
