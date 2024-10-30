package ar.edu.itba.ss.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode
public class Vector {
    private final double x;
    private final double y;

    public static Vector fromPolar(double magnitude, double angle) {
        return new Vector(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector multiply(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }

    public Vector divide(double scalar) {
        return new Vector(x / scalar, y / scalar);
    }

    public double dot(Vector other) {
        return x * other.x + y * other.y;
    }

    public double distanceTo(Vector other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    public Vector normalize() {
        final double magnitude = magnitude();
        if (magnitude == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector");
        }
        return new Vector(x / magnitude, y / magnitude);
    }

    public Vector rotate(double alpha) {
        return new Vector(x * Math.cos(alpha) - y * Math.sin(alpha), x * Math.sin(alpha) + y * Math.cos(alpha));
    }
}