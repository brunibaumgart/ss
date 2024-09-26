package ar.edu.itba.ss.models;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(fluent = true)
@EqualsAndHashCode
@AllArgsConstructor
public class Particle {
    @EqualsAndHashCode.Include
    private final int id;
    @EqualsAndHashCode.Exclude
    private final double position;
    @EqualsAndHashCode.Exclude
    private final double lastPosition;
    @EqualsAndHashCode.Exclude
    private final double velocity;
    @EqualsAndHashCode.Exclude
    private final double lastVelocity;
    @EqualsAndHashCode.Exclude
    private final double mass;

    public Particle(final int id, final double position, final double velocity, final double mass) {
        this(id, position, position, velocity, velocity, mass);
    }

    public double force(final double k, final double gamma) {
        return -k * position - gamma * velocity;
    }

    public double force(final double k, final Particle nextParticle, final Particle prevParticle) {
        return -k * (position - prevParticle.position()) - k * (position - nextParticle.position());
    }
}
