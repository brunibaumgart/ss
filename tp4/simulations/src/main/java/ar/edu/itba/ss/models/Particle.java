package ar.edu.itba.ss.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Optional;

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

    public double acceleration(SystemParameters parameters) {
        return force(parameters, Optional.empty(), Optional.empty()) / mass;
    }

    public double force(final SystemParameters parameters) {
        return -parameters.k() * position - parameters.gamma() * velocity;
    }

    public double force(final SystemParameters parameters, final Optional<Particle> nextParticle, final Optional<Particle> prevParticle) {
        if (nextParticle.isEmpty() && prevParticle.isEmpty())
            return force(parameters);
        else if (prevParticle.isPresent() && nextParticle.isPresent())
            return -parameters.k() * (position - prevParticle.get().position()) - parameters.k() * (position - nextParticle.get().position());
        else if (prevParticle.isEmpty())
            return -parameters.k() * (position - nextParticle.get().position());
        else
            return -parameters.k() * (position - prevParticle.get().position());
    }
}
