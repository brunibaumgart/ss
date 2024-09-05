package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Quadrant;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.Wall;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class CollisionUtils {
    private CollisionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Double calculateTcWithWall(final Particle particle, final Wall wall) {
        final double x = particle.position().x();
        final double y = particle.position().y();
        final double vx = particle.speed().x();
        final double vy = particle.speed().y();
        final double r = particle.radius();

        return switch (wall.type()) {
            case TOP -> {
                if (vy < 0)
                    yield Double.POSITIVE_INFINITY;
                yield (wall.position().y() - r - y) / vy;
            }
            case BOTTOM -> {
                if (vy > 0)
                    yield Double.POSITIVE_INFINITY;
                yield (wall.position().y() + r - y) / vy;
            }
            case LEFT -> {
                if (vx > 0)
                    yield Double.POSITIVE_INFINITY;
                yield (wall.position().x() + r - x) / vx;
            }
            case RIGHT -> {
                if (vx < 0)
                    yield Double.POSITIVE_INFINITY;
                yield (wall.position().x() - r - x) / vx;
            }
        };
    }

    public static Queue<Double> calculateTcWithParticles(final Particle particle, final List<Particle> particles) {
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
                            .map(q -> calculateTcWithParticle(particle, p))
                            .findFirst()
                            .orElse(Double.POSITIVE_INFINITY);
                })
                .filter(time -> time != Double.POSITIVE_INFINITY)
                .collect(Collectors.toCollection(PriorityQueue::new));
    }


    private static double calculateTcWithParticle(final Particle particle, final Particle otherParticle) {
        final double sigma = particle.radius() + otherParticle.radius();
        final Vector deltaR = otherParticle.position().subtract(particle.position());
        final Vector deltaV = otherParticle.speed().subtract(particle.speed());

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
