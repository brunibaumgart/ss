package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SystemParameters;

public class EulerAlgorithm {
    public static double calculatePosition(final SystemParameters systemParameters,
                                           final double deltaT,
                                           final Particle particle
    ) {
        return particle.position()
                + particle.velocity() * deltaT
                + deltaT*deltaT * particle.force(systemParameters) / (2 * particle.mass());
    }

    public static double calculateVelocity(final SystemParameters systemParameters,
                                           final double deltaT,
                                           final Particle particle
    ) {
        return particle.velocity() + deltaT * particle.force(systemParameters) / particle.mass();
    }
}
