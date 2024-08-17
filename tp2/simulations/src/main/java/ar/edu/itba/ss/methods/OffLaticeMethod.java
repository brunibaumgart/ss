package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OffLaticeMethod {
    private final Double speed;
    private final Double rc;
    private final Double deltaTheta;

    public OffLaticeMethod(Double rc, Double speed, Double etha) {
        this.speed = speed;
        this.rc = rc;
        this.deltaTheta = createDeltaTheta(etha);
    }

    public List<MovingParticle> runIteration(CellIndexMethod cim, Integer deltaT) {
        // Una run sería: (tiene las posiciones)
        // Calculamos los vecinos
        final Map<Particle, List<Particle>> particles = cim.calculateNeighbors(this.rc);
        final List<MovingParticle> newParticles = new ArrayList<>();

        for (Map.Entry<Particle, List<Particle>> entry : particles.entrySet()) {
            final Particle particle = entry.getKey();
            final List<Particle> neighbours = entry.getValue();

            // Calculamos los angulos en t+1
            final Double newAngleAverage = calculateNewAngle(neighbours);
            final Double newAngle = newAngleAverage + this.deltaTheta;

            // Calculamos las velocidades en t+1
            final Vector newVelocity = Vector.fromPolar(this.speed, newAngle);

            // Calcular las posiciones en t+1
            final Vector newPosition = particle.position().add(newVelocity.multiply(deltaT));

            newParticles.add(new MovingParticle(particle.id(), particle.radius(), newPosition, newVelocity));
        }

        return newParticles;
    }

    public static Double createDeltaTheta(Double etha) {
        final Random random = new Random();
        final double max = etha / 2;
        final double min = -max;

        return min + (max - min) * random.nextDouble();
    }

    private Double calculateNewAngle(final List<Particle> neighbours) {
        final List<Double> sinAngles = new ArrayList<>();
        final List<Double> cosAngles = new ArrayList<>();

        for (Particle neighbour : neighbours) {
            sinAngles.add(Math.sin(neighbour.position().angle()));
            cosAngles.add(Math.cos(neighbour.position().angle()));
        }

        return Math.atan2(getAverage(sinAngles), getAverage(cosAngles));
    }

    private Double getAverage(List<Double> angles) {
        return angles.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
