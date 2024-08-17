package ar.edu.itba.ss.models;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Accessors(fluent = true)
public class MovingParticle extends Particle {
    private final Vector velocity;

    public MovingParticle(int id, Double radius, Vector postition, Vector velocity) {
        super(id, radius, postition);
        this.velocity = velocity;
    }
}
