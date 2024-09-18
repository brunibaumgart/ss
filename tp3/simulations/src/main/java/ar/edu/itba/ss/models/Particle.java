package ar.edu.itba.ss.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
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
    @EqualsAndHashCode.Exclude
    private Vector previousSpeed;

    // for debugging purposes
    public Particle(int id) {
        this.id = id;
        this.radius = 0;
        this.position = new Vector(0, 0);
        this.speed = new Vector(0, 0);
        this.mass = 0;
        this.previousSpeed = new Vector(0, 0);
    }

    public Particle(int id, double radius, Vector position, Vector speed, double mass) {
        this.id = id;
        this.radius = radius;
        this.position = position;
        this.speed = speed;
        this.mass = mass;
        this.previousSpeed = new Vector(0, 0);
    }

    public double distanceTo(Particle other) {
        // https://stackoverflow.com/a/5509234
        return this.position().distanceTo(other.position()) - this.radius() - other.radius();
    }

    public boolean collidesWith(Particle other) {
        // Verificamos si la distancia es menor o igual a la suma de los radios
        return this.position().distanceTo(other.position()) <= (this.radius + other.radius);
    }
}
