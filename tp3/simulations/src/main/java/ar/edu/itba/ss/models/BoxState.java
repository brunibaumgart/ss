package ar.edu.itba.ss.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoxState {

    final List<Particle> particles;
    // PriorityQueue queue for finding out next tc in list.
    final double minimumCollisionTime;
    final int iteration;

    public BoxState(List<Particle> particles) {
        this.particles = particles;
        this.minimumCollisionTime = 0;
        this.iteration = 0;
    }

}
