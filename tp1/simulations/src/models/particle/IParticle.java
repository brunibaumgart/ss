package models.particle;

import models.Point;

public interface IParticle {
    Point getPosition();
    Double getRadius();
    boolean equals(Object obj);
    String toString();
}
