package utils;

import models.IdentifiableParticle;
import models.Point;

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
     * @param N number of particles
     * @param L length of the box
     * @param r radius of the particles
     * @return list of particles
     */
    public static List<IdentifiableParticle> createParticles(final int N, final Double L, final Double r) {
        final List<IdentifiableParticle> particles = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < N; i++) {
            double x = random.nextDouble() * L;
            double y = random.nextDouble() * L;

            IdentifiableParticle ip = new IdentifiableParticle(i, new Point(x, y), r);
            while (particles.contains(ip)) {
                x = random.nextDouble() * L;
                y = random.nextDouble() * L;
                ip = new IdentifiableParticle(i, new Point(x, y), r);
            }

            particles.add(ip);
        }

        return particles;
    }
}
