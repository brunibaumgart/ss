package utils;

import models.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create particles in the space without overlapping
     * @param N number of particles
     * @param L length of the box
     * @param r radius of the particles
     *
     * @return list of particles
     */
    public static List<Particle> createParticles(final int N, final Double L, final Double r) {
        final List<Particle> particles = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < N; i++) {
            double x =  random.nextDouble() * L;
            double y =  random.nextDouble() * L;

            Particle p = new Particle(x, y, r);
            while (particles.contains(p)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                p = new Particle(x, y, r);
            }
            particles.add(p);
        }

        return particles;
    }
}
