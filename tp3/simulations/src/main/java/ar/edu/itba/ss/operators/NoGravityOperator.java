package ar.edu.itba.ss.operators;

import ar.edu.itba.ss.models.Pair;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.Wall;
import ar.edu.itba.ss.models.events.CollisionEvent;

public class NoGravityOperator {
    private NoGravityOperator() {
        throw new IllegalStateException("Non instantiable class");
    }

    public static Pair<Vector, Vector> collide(final Particle p1, final Particle p2) {
        final double sigma = p1.radius() + p2.radius();
        final Vector deltaV = p2.speed().subtract(p1.speed());
        final Vector deltaR = p2.position().subtract(p1.position());
        final double deltaVdotDeltaR = deltaV.dot(deltaR);

        final double J = 2 * p1.mass() * deltaVdotDeltaR / (sigma * ((p1.mass() / p2.mass()) + 1));

        final double Jx = J * deltaR.x() / sigma;
        final double Jy = J * deltaR.y() / sigma;

        final Vector p1Speed = new Vector(p1.speed().x() + Jx / p1.mass(), p1.speed().y() + Jy / p1.mass());
        final Vector p2Speed = new Vector(p2.speed().x() - Jx / p2.mass(), p2.speed().y() - Jy / p2.mass());

        return new Pair<>(p1Speed, p2Speed);
    }

    public static Vector collideWithWall(final Particle p, final Wall wall) {
        return switch (wall.type()) {
            case TOP, BOTTOM -> new Vector(p.speed().x(), -p.speed().y()); // horizontal
            case LEFT, RIGHT -> new Vector(-p.speed().x(), p.speed().y()); // vertical
        };
    }

    public static Vector collideWithCircularWall(final Particle p, final Vector center){
        Vector normal = p.position().subtract(center); // normal vector of particle
        normal = normal.multiply(1 / normal.magnitude()); // normal unit vector

        final Vector vNormal = normal.multiply(p.speed().dot(normal)); // vn magnitude of speed vector
        final Vector vTangencial = p.speed().subtract(vNormal); // vt magnitude of speed vector

        return vTangencial.subtract(vNormal); // v = vt - vn because collision changes vn to -vn
    }
}
