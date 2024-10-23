package ar.edu.itba.ss.models.forces;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class SocialForce implements Force {
    private final double tau;
    private final double A;
    private final double B;
    private final double kn;

    @Override
    public Vector apply(final Particle particle, final List<Particle> otherParticles) {
        return granularForce(particle, otherParticles).add(socialForce(particle, otherParticles)).add(drivingForce(particle));
    }

    private Vector granularForce(final Particle i, final List<Particle> particles) {
        Vector result = Vector.fromPolar(0, 0);

        for(Particle j : particles) {
            if(j.equals(i)) continue;

            // Calculate the distance between the particles
            final Vector rij = i.position().subtract(j.position());
            final double epsilon = rij.magnitude() - (i.radius() + j.radius());
            final Vector eij = rij.normalize(); // Normalized vector from i to j

            if(epsilon > 0) continue;

            result = result.add(eij.multiply(-1 * kn * epsilon)); // TODO: Check if this is correct (D29 Teorica 6)
        }

        return result;
    }

    private Vector socialForce(final Particle i, final List<Particle> particles) {
        Vector result = Vector.fromPolar(0, 0);

        for(Particle j : particles) {
            if(j.equals(i)) continue;

            // Calculate the distance between the particles
            final Vector rij = i.position().subtract(j.position());
            final double epsilon = rij.magnitude() - (i.radius() + j.radius());
            final Vector eij = rij.normalize(); // Normalized vector from i to j

            result = result.add(eij.multiply(A * Math.exp(-epsilon / B)));
        }

        return result;
    }

    private Vector drivingForce(final Particle i) {
        return i.desiredVelocityVector().subtract(i.velocity()).multiply(i.mass() / tau); // TODO: Check if this is correct (D31 Teorica 6)
    }
}
