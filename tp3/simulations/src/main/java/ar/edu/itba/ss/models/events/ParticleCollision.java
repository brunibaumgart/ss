package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ParticleCollision extends Event {
    private final Particle p1;
    private final Particle p2;

    public ParticleCollision(double time, Particle p1, Particle p2) {
        super(time, EventType.PARTICLES_COLLISION);
        this.p1 = p1;
        this.p2 = p2;
    }
}
