package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.Wall;
import ar.edu.itba.ss.models.events.CircularWallCollisionEvent;
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

    private static Optional<CircularWallCollisionEvent> calculateTcWithCircularWall(final Particle particle, final double wallRadius){
        final double x0 = particle.position().x() - wallRadius; // Makes the calculation easier
        final double y0 = particle.position().y() - wallRadius;
        final double vx = particle.speed().x();
        final double vy = particle.speed().y();

        if(vx == 0 && vy == 0) {
            return Optional.empty();
        }

        final double A = vx*vx + vy*vy;
        final double B = 2 * ((x0)*vx + (y0)*vy);
        final double rdiff = wallRadius - particle.radius();
        final double C = x0*x0 + y0*y0 - rdiff*rdiff;

        final double discriminant = B * B - 4 * A * C;

        if (discriminant < 0) {
            return Optional.empty();
        }

        final double t1 = (-B + Math.sqrt(discriminant)) / (2 * A);
        //final double t2 = (-B - Math.sqrt(discriminant)) / (2 * A);
        return Optional.of(new CircularWallCollisionEvent(t1, particle));
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

    public static PriorityQueue<CollisionEvent> calculateAllCollisionsCircular(final Particle particle, final List<Particle> particles, final Vector center, final double radius){
        final PriorityQueue<ParticleCollisionEvent> particleCollisions = CollisionUtils.calculateTcWithParticles(particle, particles);
        final Optional<CircularWallCollisionEvent> circularWallCollision = CollisionUtils.calculateTcWithCircularWall(particle, center, radius);

        final PriorityQueue<CollisionEvent> collisions = new PriorityQueue<>();
        collisions.addAll(particleCollisions);

        circularWallCollision.ifPresent(collisions::add);

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
