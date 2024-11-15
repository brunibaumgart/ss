package models;

public record Particle(int id, Point position, Double radius) {

    public double distanceTo(Particle other) {
        // https://stackoverflow.com/a/5509234
        return this.position().distanceTo(other.position()) - this.radius() - other.radius();
    }

    public double periodicDistanceTo(Particle particle, Double L) {
        // https://math.stackexchange.com/a/1364646
        double dx = Math.abs(this.position().x() - particle.position().x());
        if (dx > L / 2)
            dx = L - dx;

        double dy = Math.abs(this.position().y() - particle.position().y());
        if (dy > L / 2)
            dy = L - dy;

        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) - this.radius() - particle.radius();
    }

    @Override
    public int hashCode() {
        return id;
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
        if (this.position.equals(other.position))
            return true;

        return this.collidesWith(other);
    }

    private boolean collidesWith(Particle other) {
        // Calculamos la distancia entre los centros de las partículas
        double distance = Math.sqrt(Math.pow(this.position.x() - other.position.x(), 2)
                + Math.pow(this.position.y() - other.position.y(), 2));

        // Verificamos si la distancia es menor o igual a la suma de los radios
        return distance <= (this.radius + other.radius);
    }

    @Override
    public String toString() {
        return String.format("Particle{id=%s, position=%s, radius=%.2f}", id, position, radius);
    }
}
