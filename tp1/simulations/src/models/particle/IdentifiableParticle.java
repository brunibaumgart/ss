package models.particle;

import models.Point;

public class IdentifiableParticle implements IParticle {
    private final Particle particle;
    private final int id;
    //private final Point position;
    //private final Double radius;

    public IdentifiableParticle(/* double x, double y , double radius */Particle particle, int id) {
        this.particle = particle;
        //this.position = new Point(x, y);
        this.id = id;
        //this.radius = radius;
    }


    @Override
    public Point getPosition() {
        return particle.getPosition();
    }

    @Override
    public Double getRadius() {
        return particle.getRadius();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        IdentifiableParticle other = (IdentifiableParticle) obj;
        return this.particle.equals(other.particle);
    }

    @Override
    public String toString() {
        return String.format("IdentifiableParticle{id=%d, particle=%s}", id, particle);
    }

    public int getId() {
        return id;
    }
}
