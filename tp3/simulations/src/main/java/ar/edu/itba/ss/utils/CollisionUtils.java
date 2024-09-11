package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.Wall;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.models.events.ParticleCollisionEvent;
import ar.edu.itba.ss.models.events.WallCollisionEvent;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class CollisionUtils {
    private CollisionUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static PriorityQueue<WallCollisionEvent> calculateTcWithWalls(final Particle particle, final double L) {
        final List<Wall> walls = List.of(
                new Wall(Wall.WallType.TOP, L),
                new Wall(Wall.WallType.BOTTOM, L),
                new Wall(Wall.WallType.LEFT, L),
                new Wall(Wall.WallType.RIGHT, L)
        );

        return walls.parallelStream()
                .map(wall -> calculateTcWithWall(particle, wall))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(e -> e.getTime() != Double.POSITIVE_INFINITY && e.getTime() != Double.NEGATIVE_INFINITY)
                .collect(Collectors.toCollection(PriorityQueue::new));
    }

    private static Optional<WallCollisionEvent> calculateTcWithWall(final Particle particle, final Wall wall) {
        final double x = particle.position().x();
        final double y = particle.position().y();
        final double vx = particle.speed().x();
        final double vy = particle.speed().y();
        final double r = particle.radius();

        return switch (wall.type()) {
            case TOP -> {
                if (vy > 0)
                    yield Optional.of(new WallCollisionEvent((wall.position().y() - r - y) / vy, particle, wall));
                yield Optional.empty();
            }
            case BOTTOM -> {
                if (vy < 0)
                    yield Optional.of(new WallCollisionEvent((wall.position().y() + r - y) / vy, particle, wall));
                yield Optional.empty();
            }
            case LEFT -> {
                if (vx < 0)
                    yield Optional.of(new WallCollisionEvent((wall.position().x() + r - x) / vx, particle, wall));
                yield Optional.empty();
            }
            case RIGHT -> {
                if (vx > 0)
                    yield Optional.of(new WallCollisionEvent((wall.position().x() - r - x) / vx, particle, wall));
                yield Optional.empty();
            }
        };
    }

    public static PriorityQueue<ParticleCollisionEvent> calculateTcWithParticles(final Particle particle, final List<Particle> particles) {
        return particles.parallelStream()
                .map(p -> {
                    final Optional<Double> time = calculateTcWithParticle(particle, p);

                    return time.map(t -> new ParticleCollisionEvent(t, particle, p));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(e -> e.getTime() != Double.POSITIVE_INFINITY && e.getTime() != Double.NEGATIVE_INFINITY)
                .collect(Collectors.toCollection(PriorityQueue::new));
    }

    public static PriorityQueue<CollisionEvent> calculateAllCollisions(final Particle particle, final List<Particle> particles, final double L) {
        final PriorityQueue<ParticleCollisionEvent> particleCollisions = CollisionUtils.calculateTcWithParticles(particle, particles);
        final PriorityQueue<WallCollisionEvent> wallCollisions = CollisionUtils.calculateTcWithWalls(particle, L);

        final PriorityQueue<CollisionEvent> collisions = new PriorityQueue<>();
        collisions.addAll(particleCollisions);
        collisions.addAll(wallCollisions);

        return collisions;
    }

    private static Optional<Double> calculateTcWithParticle(final Particle particle, final Particle otherParticle) {
        if (particle.equals(otherParticle))
            return Optional.empty();

        final double sigma = particle.radius() + otherParticle.radius();
        final Vector deltaR = otherParticle.position().subtract(particle.position());
        final Vector deltaV = otherParticle.speed().subtract(particle.speed());

        final double deltaRDotDeltaR = deltaR.dot(deltaR);
        final double deltaVDotDeltaV = deltaV.dot(deltaV);
        final double deltaVDotDeltaR = deltaV.dot(deltaR);
        if (deltaVDotDeltaR >= 0)
            return Optional.empty();

        final double d = Math.pow(deltaVDotDeltaR, 2) - (deltaVDotDeltaV * (deltaRDotDeltaR - Math.pow(sigma, 2)));
        if (d < 0)
            return Optional.empty();

        return Optional.of(-((deltaVDotDeltaR + Math.sqrt(d)) / deltaVDotDeltaV));
    }
}
