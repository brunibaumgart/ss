package ar.edu.itba.ss.methods;

import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OffLaticeMethod {
    private final Double speed;
    private final Double rc;
    private final Double etha;
    private final Random random;

    public OffLaticeMethod(Double rc, Double speed, Double etha) {
        this.speed = speed;
        this.rc = rc;
        this.etha = etha;
        this.random = new Random();
    }

    public static Double calculateVa(List<MovingParticle> particles) {
        if (particles.isEmpty())
            return 0.0;

        final Vector sum = particles.stream()
                .map(MovingParticle::speed)
                .reduce(Vector::add)
                .orElse(new Vector(0, 0));

        return sum.magnitude() / (particles.size());
    }

    private static Vector applyPeriodicBoundaryConditions(Vector position, Double L) {
        double x = position.x() % L;
        double y = position.y() % L;

        if (x < 0) x += L;
        if (y < 0) y += L;

        return new Vector(x, y);
    }

    public List<MovingParticle> runIteration(CellIndexMethod cim, Integer deltaT, List<MovingParticle> particles, FileWriter writer) throws IOException {
        // Calculamos los vecinos
        final Map<Particle, List<Particle>> neighborMap = cim.calculateNeighbors(this.rc);
        final List<MovingParticle> result = new ArrayList<>();

        for (MovingParticle particle : particles) {
            final List<Particle> neighbours = neighborMap.getOrDefault(particle, new ArrayList<>());
            neighbours.add(particle);

            // Calculamos los angulos en t+1
            final double newAngle = calculateNewAngle(neighbours) + getDeltaTheta();

            // Calcular las posiciones en t+1
            final Vector newPosition = OffLaticeMethod.applyPeriodicBoundaryConditions(
                    particle.position().add(particle.speed().multiply(deltaT)),
                    cim.L()
            );

            // Calculamos las velocidades en t+1
            final Vector newVelocity = Vector.fromPolar(this.speed, newAngle);

            final MovingParticle updatedParticle = new MovingParticle(particle.id(), particle.radius(), newPosition, newVelocity);
            result.add(updatedParticle);

            OutputUtils.printParticleData(writer, updatedParticle, neighbours);
        }

        return result;
    }

    public List<MovingParticle> runIterationNoPrints(CellIndexMethod cim, Integer deltaT, List<MovingParticle> particles) {
        // Calculamos los vecinos
        final Map<Particle, List<Particle>> neighborMap = cim.calculateNeighbors(this.rc);
        final List<MovingParticle> result = new ArrayList<>();

        for (MovingParticle particle : particles) {
            final List<Particle> neighbours = neighborMap.getOrDefault(particle, new ArrayList<>());
            neighbours.add(particle);

            // Calculamos los angulos en t+1
            final double newAngle = calculateNewAngle(neighbours) + getDeltaTheta();

            // Calcular las posiciones en t+1
            final Vector newPosition = OffLaticeMethod.applyPeriodicBoundaryConditions(
                    particle.position().add(particle.speed().multiply(deltaT)),
                    cim.L()
            );

            // Calculamos las velocidades en t+1
            final Vector newVelocity = Vector.fromPolar(this.speed, newAngle);

            final MovingParticle updatedParticle = new MovingParticle(particle.id(), particle.radius(), newPosition, newVelocity);
            result.add(updatedParticle);
        }

        return result;
    }

    public Double getDeltaTheta() {
        return this.etha * (this.random.nextDouble() - 0.5);
    }

    private Double calculateNewAngle(final List<Particle> neighbours) {
        final List<Double> sinAngles = new ArrayList<>();
        final List<Double> cosAngles = new ArrayList<>();

        for (Particle neighbour : neighbours) {
            MovingParticle movingNeighbour = (MovingParticle) neighbour;
            sinAngles.add(Math.sin(movingNeighbour.speed().angle()));
            cosAngles.add(Math.cos(movingNeighbour.speed().angle()));
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
