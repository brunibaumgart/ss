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
    private final Vector desiredVelocity;
}
