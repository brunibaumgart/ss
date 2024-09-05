package ar.edu.itba.ss.models;

import java.util.Set;

/**
 * Ascii art of quadrants
 * <pre>
 *     | 2 | 1 |
 *     | 3 | 4 |
 * </pre>
 */
public enum Quadrant {
    ONE {
        @Override
        public boolean isInQuadrant(double x, double y, double otherX, double otherY) {
            return otherX > x && otherY > y;
        }
        @Override
        public Set<Quadrant> getAdjacentQuadrants() {
            return Set.of(ONE, TWO, FOUR);
        }
    },
    TWO {
        @Override
        public boolean isInQuadrant(double x, double y, double otherX, double otherY) {
            return otherX < x && otherY > y;
        }
        @Override
        public Set<Quadrant> getAdjacentQuadrants() {
            return Set.of(TWO, ONE, THREE);
        }
    },
    THREE {
        @Override
        public boolean isInQuadrant(double x, double y, double otherX, double otherY) {
            return otherX < x && otherY < y;
        }
        @Override
        public Set<Quadrant> getAdjacentQuadrants() {
            return Set.of(THREE, TWO, FOUR);
        }
    },
    FOUR {
        @Override
        public boolean isInQuadrant(double x, double y, double otherX, double otherY) {
            return otherX > x && otherY < y;
        }
        @Override
        public Set<Quadrant> getAdjacentQuadrants() {
            return Set.of(FOUR, ONE, THREE);
        }
    };

    /**
     * Get the quadrant of a given angle
     * @param angle in degrees
     * @return the quadrant
     */
    public static Quadrant getQuadrant(double angle) {
        if (angle < 90) {
            return ONE;
        } else if (angle < 180) {
            return TWO;
        } else if (angle < 270) {
            return THREE;
        } else {
            return FOUR;
        }
    }

    /** Get the adjacent quadrants, including itself */
    public abstract Set<Quadrant> getAdjacentQuadrants();
    public abstract boolean isInQuadrant(double x, double y, double otherX, double otherY);
}