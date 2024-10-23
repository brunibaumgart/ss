package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.forces.Force;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleUtils {
    private ParticleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Particle> createMovingParticles(
            final List<Particle> particles,
            final int Nj,
            final double height,
            final double width,
            final double mass,
            final Force force,
            final double radius,
            final double desiredVelocity,
            final Vector target
    ) {
        final Random seedGenerator = new Random();
        final long seed = seedGenerator.nextLong();
        final Random random = new Random(seed);
        System.out.println("Seed " + seed);

        final List<Particle> result = new ArrayList<>(particles);

        for (int i = 0; i < Nj; i++) {
            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;

            Vector position = new Vector(x, y);

            Particle p = Particle.builder()
                    .id(i)
                    .mass(mass)
                    .lastPosition(position)
                    .position(position)
                    .force(force)
                    .radius(radius)
                    .velocity(new Vector(0,0))
                    .desiredVelocity(desiredVelocity)
                    .target(target)
                    .build();

            // check particles do not overlap
            while (collidesWithAny(p, result) || collidesWithWalls(p, height, width)) {
                x = random.nextDouble() * width;
                y = random.nextDouble() * height;

                position = new Vector(x,y);
                p = Particle.builder()
                        .id(i)
                        .mass(mass)
                        .lastPosition(position)
                        .position(position)
                        .force(force)
                        .radius(radius)
                        .velocity(new Vector(0,0))
                        .desiredVelocity(desiredVelocity)
                        .target(target)
                        .build();
            }

            result.add(p);
        }

        return result;
    }
    private static boolean collidesWithAny(final Particle particle, final List<Particle> particles) {
        return particles.stream().anyMatch(particle::collidesWith);
    }

    private static boolean collidesWithWalls(final Particle particle, final double height, final double width) {
        return particle.position().x() - particle.radius() <= 0 ||
                particle.position().x() + particle.radius() >= width ||
                particle.position().y() - particle.radius() <= 0 ||
                particle.position().y() + particle.radius() >= height;
    }
}
