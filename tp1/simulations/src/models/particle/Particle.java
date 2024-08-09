package models.particle;

import models.Point;

public class Particle implements IParticle {
    private final Point position;

    private final Double radius;

    public Particle(Double x, Double y, Double radius) {
        this.position = new Point(x, y);
        this.radius = radius;
    }

    public Point getPosition() {
        return position;
    }

    public Double getRadius() {
        return radius;
    }

    /**
     * Check if two particles are equal/collide
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Particle other = (Particle) obj;
        if(this.position.equals(other.position))
            return true;

        // Calculamos la distancia entre los centros de las partículas
        double distance = Math.sqrt(Math.pow(this.position.getX() - other.position.getX(), 2)
                + Math.pow(this.position.getY() - other.position.getY(), 2));

        // Verificamos si la distancia es menor o igual a la suma de los radios
        return distance <= (this.radius + other.radius);
    }

    @Override
    public String toString() {
        return String.format("Particle{position=%s, radius=%.2f}", position, radius);
    }
}
