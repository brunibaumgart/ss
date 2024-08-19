package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<MovingParticle> createMovingParticles(final int N, final Double L, final Double r, final Double speed) {
        final List<MovingParticle> particles = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < N; i++) {
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            Particle p = new Particle(i, r, new Vector(x, y));
            // check particles do not overlap
            while (collidesWithAny(p, particles)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(i, r, new Vector(x, y));
            }

            particles.add(new MovingParticle(i, r, new Vector(x, y), Vector.fromPolar(speed, random.nextDouble() * 2 * Math.PI)));
        }

        return particles;
    }

    /**
     * Create particles in the space without overlapping
     *
     * @param N number of particles
     * @param L length of the box
     * @param r radius of the particles
     * @return list of particles
     */
    public static List<Particle> createParticles(final int N, final Double L, final Double r) {
        final List<Particle> particles = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < N; i++) {
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            Particle p = new Particle(i, r, new Vector(x, y));
            while (collidesWithAny(p, particles)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(i, r, new Vector(x, y));
            }

            particles.add(p);
        }

        return particles;
    }

    public static List<Particle> createParticles(final int N, final Double L, List<Double> radius) {
        if (radius.size() != N)
            throw new IllegalArgumentException("The number of particles must be the same as the number of radius");

        final List<Particle> particles = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < radius.size(); i++) {
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            Particle p = new Particle(i, radius.get(i), new Vector(x, y));
            while (collidesWithAny(p, particles)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(i, radius.get(i), new Vector(x, y));
            }

            particles.add(p);
        }

        return particles;
    }

    private static boolean collidesWithAny(final Particle particle, final List<? extends Particle> particles) {
        return particles.stream().anyMatch(particle::collidesWith);
    }
}
