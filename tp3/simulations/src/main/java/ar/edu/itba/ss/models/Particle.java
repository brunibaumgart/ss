package ar.edu.itba.ss.models;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@ToString
@Accessors(fluent = true)
@EqualsAndHashCode
public class Particle {
    @EqualsAndHashCode.Include
    private final int id;
    @EqualsAndHashCode.Exclude
    private final double radius;
    @EqualsAndHashCode.Exclude
    private final Vector position;
    @EqualsAndHashCode.Exclude
    private final Vector speed;
    @EqualsAndHashCode.Exclude
    private final double mass;

    public double distanceTo(Particle other) {
        // https://stackoverflow.com/a/5509234
        return this.position().distanceTo(other.position()) - this.radius() - other.radius();
    }

    public boolean collidesWith(Particle other) {
        // Verificamos si la distancia es menor o igual a la suma de los radios
        return this.position().distanceTo(other.position()) <= (this.radius + other.radius);
    }
}
