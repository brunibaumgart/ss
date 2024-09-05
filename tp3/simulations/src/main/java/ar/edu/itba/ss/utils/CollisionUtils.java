package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Quadrant;
import ar.edu.itba.ss.models.Vector;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class CollisionUtils {
    private CollisionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Queue<Double> calculateCollisionTimes(final Particle particle, final List<Particle> particles) {
        final double angle = Math.toDegrees(particle.speed().angle());
        final double x = particle.position().x();
        final double y = particle.position().y();

        final Set<Quadrant> quadrants = Quadrant.getQuadrant(angle).getAdjacentQuadrants();

        return particles.parallelStream()
                .map(p -> {
                    final double otherX = p.position().x();
                    final double otherY = p.position().y();

                    return quadrants.parallelStream()
                            .filter(q -> q.isInQuadrant(x, y, otherX, otherY))
                            .map(q -> calculateCollisionTime(particle, p))
                            .findFirst()
                            .orElse(Double.POSITIVE_INFINITY);
                })
                .filter(time -> time != Double.POSITIVE_INFINITY)
                .collect(Collectors.toCollection(PriorityQueue::new));
    }


    private static double calculateCollisionTime(Particle particle, Particle otherParticle) {
        final double sigma = particle.radius() + otherParticle.radius();
        final Vector deltaR = otherParticle.position().subtract(particle.position());
        final Vector deltaV = deltaR.multiply(particle.speed().magnitude());

        final double deltaRDotDeltaR = deltaR.dot(deltaR);
        final double deltaVDotDeltaV = deltaV.dot(deltaV);
        final double deltaVDotDeltaR = deltaV.dot(deltaR);
        if (deltaVDotDeltaR >= 0)
            return Double.POSITIVE_INFINITY;

        final double d = Math.pow(deltaVDotDeltaR, 2) - (deltaVDotDeltaV) * (deltaRDotDeltaR - Math.pow(sigma, 2));
        if (d < 0)
            return Double.POSITIVE_INFINITY;

        return -((deltaVDotDeltaR + Math.sqrt(d)) / deltaVDotDeltaV);
    }
}
