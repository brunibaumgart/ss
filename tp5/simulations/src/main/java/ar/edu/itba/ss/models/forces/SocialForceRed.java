package ar.edu.itba.ss.models.forces;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class SocialForceRed implements Force {
    private final double tau;
    private final double Ap;
    private final double Bp;
    private final double kn;
    private final static int MAX_CLOSEST_PARTICLES = 3;

    @Override
    public Vector apply(final Particle particle, final List<Particle> otherParticles) {
        return granularForce(particle, otherParticles).add(avoidanceForce(particle, otherParticles));
    }

    private Vector granularForce(final Particle i, final List<Particle> particles) {
        Vector result = Vector.fromPolar(0, 0);

        for (Particle j : particles) {
            if (j.equals(i)) continue;

            // Calculate the distance between the particles
            final Vector rij = i.position().subtract(j.position());
            final double epsilon = rij.magnitude() - (i.radius() + j.radius());
            final Vector eij = rij.normalize(); // Normalized vector from i to j

            if (epsilon > 0) continue;

            result = result.add(eij.multiply(-1 * kn * epsilon));
        }

        return result;
    }

    private Vector avoidanceForce(final Particle i, final List<Particle> particles) {
        final List<Particle> closestParticles = getClosestParticles(i, particles);
        if (closestParticles.isEmpty()) return drivingForce(i);

        final Vector eit = i.target().subtract(i.position()); // goalDirection

        Vector sumNjc = Vector.fromPolar(0, 0);
        for (Particle j : closestParticles) {
            if (j.equals(i)) continue;

            final Vector rij = j.position().subtract(i.position()); // obstacleDirection
            final Vector eij = rij.normalize();

            final double cos = eit.dot(rij) / (eit.magnitude() * rij.magnitude());
            final Vector njc = eij.multiply(Ap * Math.exp(-rij.magnitude() / Bp) * cos);

            sumNjc = sumNjc.subtract(njc);
        }

        // TODO: check walls

        final Vector ea = sumNjc.add(eit.normalize()).normalize();

        return ea.multiply(i.desiredVelocity()).subtract(i.velocity()).multiply(i.mass() / tau);
    }

    private Vector drivingForce(final Particle i) {
        return i.desiredVelocityVector().subtract(i.velocity()).multiply(i.mass() / tau);
    }

    private List<Particle> getClosestParticles(final Particle i, final List<Particle> particles) {
        final Comparator<Particle> comparator = Comparator.comparingDouble(p -> i.position().distanceTo(p.position()));
        final Set<Particle> closestParticles = new TreeSet<>(comparator);
        for (Particle j : particles) {
            if (j.equals(i)) continue;

            if (!i.hasVisionOf(j)) continue;
            closestParticles.add(j);
        }

        return closestParticles.stream().limit(MAX_CLOSEST_PARTICLES).collect(Collectors.toList());
    }
}
