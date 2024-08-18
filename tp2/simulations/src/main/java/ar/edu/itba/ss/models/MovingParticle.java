package ar.edu.itba.ss.models;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Accessors(fluent = true)
public class MovingParticle extends Particle {
    private final Vector speed;

    public MovingParticle(int id, Double radius, Vector postition, Vector speed) {
        super(id, radius, postition);
        this.speed = speed;
    }
}
