package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Particle> createParticles(final int amount, final double mass, final double position) {
        final List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            final Particle particle = new Particle(
                    i,
                    position,
                    position,
                    0,
                    0,
                    mass
            );

            particles.add(particle);
        }

        return particles;
    }
}
