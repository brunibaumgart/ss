package ar.edu.itba.ss.models.events;

import ar.edu.itba.ss.models.Particle;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class ParticleCollisionEvent extends CollisionEvent implements Comparable<CollisionEvent> {
    private final Particle p1;
    private final Particle p2;

    public ParticleCollisionEvent(double time, Particle p1, Particle p2) {
        super(EventType.PARTICLES_COLLISION, time);
        this.p1 = p1;
        this.p2 = p2;
    }


    @Override
    public List<Particle> getParticles() {
        final List<Particle> particles = new ArrayList<>();
        particles.add(p1);
        particles.add(p2);
        return particles;
    }

    @Override
    public boolean containsParticles(List<Particle> particles) {
        return particles.contains(p1) || particles.contains(p2);
    }

}
