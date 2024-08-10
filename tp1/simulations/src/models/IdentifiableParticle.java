package models;

public record IdentifiableParticle(int id, Point position, Double radius) {

    public double distanceTo(IdentifiableParticle other) {
        // https://stackoverflow.com/a/5509234
        return this.position().distanceTo(other.position()) - this.radius() - other.radius();
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

        IdentifiableParticle other = (IdentifiableParticle) obj;
        if (this.position.equals(other.position))
            return true;

        return this.collidesWith(other);
    }

    private boolean collidesWith(IdentifiableParticle other) {
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
