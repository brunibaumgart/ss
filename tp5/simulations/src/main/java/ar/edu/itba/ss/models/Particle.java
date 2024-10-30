package ar.edu.itba.ss.models;


import ar.edu.itba.ss.models.forces.Force;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode
@ToString
@Builder
public class Particle {
    @EqualsAndHashCode.Include
    private final int id;
    @EqualsAndHashCode.Exclude
    private final double mass;
    @EqualsAndHashCode.Exclude
    private final Vector position;
    @EqualsAndHashCode.Exclude
    private final Vector lastPosition;
    @EqualsAndHashCode.Exclude
    private final Force force;
    @EqualsAndHashCode.Exclude
    private final double radius;
    @EqualsAndHashCode.Exclude
    private final Vector velocity;
    @EqualsAndHashCode.Exclude
    private final double desiredVelocity;
    @EqualsAndHashCode.Exclude
    private final Vector target;

    public Vector desiredVelocityVector() {
        Vector direction = target.subtract(this.position);
        direction = direction.normalize();

        return direction.multiply(desiredVelocity);
    }

    public boolean collidesWith(final Particle other) {
        return this.position().distanceTo(other.position()) <= (this.radius + other.radius);
    }

    public boolean hasVisionOf(final Particle other) {
        final Vector et = this.target().subtract(this.position()).normalize();

        final double m = et.y() == 0 ? 0 : et.x() / et.y();
        final double b = position().y() - m * position().x();

        if(m == 0)
            return other.position().x() >= position().x();
        return m > 0 ? other.position().y() <= m * other.position().x() + b : other.position().y() >= m * other.position().x() + b;
    }
}
