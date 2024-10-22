package ar.edu.itba.ss.models;


import ar.edu.itba.ss.models.forces.Force;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class Particle {
    private final int id;
    private final double mass;
    private final Vector position;
    private final Force force;
    private final double radius;
    private final Vector velocity;
    private final Vector desiredVelocity;
}
