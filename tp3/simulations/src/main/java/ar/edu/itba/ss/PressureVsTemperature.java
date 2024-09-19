package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.Pair;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.models.events.ParticleCollisionEvent;
import ar.edu.itba.ss.models.events.WallCollisionEvent;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PressureVsTemperature {

    public static void run(final Parameters parameters, final Particle obstacleParticle) throws IOException {
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "pressure_vs_temperature" + ".txt");
        OutputUtils.printPressureAndTemperatureHeader(writer);

        final List<Particle> aux = new ArrayList<>();
        aux.add(obstacleParticle);

        final List<Double> speeds = new ArrayList<>();
        speeds.add(3.0);
        speeds.add(6.0);
        speeds.add(10.0);

        final double dt = parameters.getPlots().getPressureVsTemperature().getDt();
        final int intervals = (int) (parameters.getTime() / dt);

        for (final double speed : speeds) {
            // 1. Calculamos presiones para cada intervalo de tiempo
            List<Particle> particles;
            if (parameters.isCircular()){
                particles = ParticleUtils.createMovingParticlesCircular(
                        aux,
                        parameters.getN(),
                        parameters.getL(),
                        parameters.getRp(),
                        speed,
                        parameters.getMassP());
            }
            else {
                particles = ParticleUtils.createMovingParticles(
                        aux,
                        parameters.getN(),
                        parameters.getL(),
                        parameters.getRp(),
                        speed,
                        parameters.getMassP()
                );
            }
            final List<Double> wallPressure = new ArrayList<>();
            final List<Double> brownianPressure = new ArrayList<>();

            // Seteo las variables de la presión en 0 (las usamos para calcular la cantidad de movimiento de las particulas)
            for ( int i = 0 ; i < intervals ; i++ ) {
                wallPressure.add(0.0);
                brownianPressure.add(0.0);
            }

            SimulationState simulationState = new SimulationState(particles, parameters.getL(), parameters.isCircular());
            for (int i = 0; Double.compare(simulationState.timeElapsed(), parameters.getTime()) < 0 ; i++) {

                MolecularDynamicsMethod.runIteration(simulationState);
                if(Double.compare(simulationState.timeElapsed(), parameters.getTime()) >= 0)
                    break;

                int currentInterval = (int) (simulationState.timeElapsed() / dt) ;

                if (simulationState.lastEvent().getType() == CollisionEvent.EventType.PARTICLES_COLLISION){
                    final ParticleCollisionEvent event = (ParticleCollisionEvent) simulationState.lastEvent();
                    Particle p1 = event.getParticles().get(0);
                    Particle p2 = event.getParticles().get(1);
                    // Checkeamos que la colision sea entre la particula browniana (id -1) y otra particula
                    if (p1.id() == ParticleUtils.BROWNIAN_ID || p2.id() == ParticleUtils.BROWNIAN_ID) {
                        // Calculamos el impulso para la partícula browniana
                        Particle particle = (p1.id() == ParticleUtils.BROWNIAN_ID) ? p2 : p1;
                        double deltaVx = particle.speed().x() - particle.previousSpeed().x();
                        double deltaVy = particle.speed().y() - particle.previousSpeed().y();
                        double impulse = particle.mass() * Math.sqrt(deltaVx * deltaVx + deltaVy * deltaVy);
                        brownianPressure.set(currentInterval, brownianPressure.get(currentInterval) + impulse);
                    }
                }
                else {
                    // Sucedió una colisión con una pared
                    final List<Particle> particle = simulationState.lastEvent().getParticles();
                    final Particle p = particle.get(0);
                    double deltaVx = p.speed().x() - p.previousSpeed().x();
                    double deltaVy = p.speed().y() - p.previousSpeed().y();
                    double impulse = p.mass() * Math.sqrt(deltaVx * deltaVx + deltaVy * deltaVy);
                    wallPressure.set(currentInterval, wallPressure.get(currentInterval) + impulse);
                }
            }

            for ( int i = 0 ; i < intervals ; i++ ) {
                wallPressure.set(i, wallPressure.get(i) / ( 4 * parameters.getL() * dt));
                brownianPressure.set(i, brownianPressure.get(i) / (2 * Math.PI * parameters.getRb() *  dt));
            }

            // 2. Calculamos el promedio de las presiones contra paredes y su desvio estándar.
            final Pair<Double, Double> wallMeanAndDeviation = processPressures(wallPressure);

            // 3. Calculamos el promedio de las presiones contra el obstaculo y su desvio estándar.
            final Pair<Double, Double> obstacleMeanAndDeviation = processPressures(brownianPressure);

            // 4. Escribimos a archivo
            final double temperature = speed*speed;
            OutputUtils.printPressureAndTemperature(writer,
                    wallMeanAndDeviation.first(), wallMeanAndDeviation.second(),
                    obstacleMeanAndDeviation.first(), obstacleMeanAndDeviation.second(),
                    temperature
                    );
        }
    }

    private static Pair<Double, Double> processPressures(final List<Double> pressures){
        double sum = 0.0;
        for (double pressure : pressures) {
            sum += pressure;
        }
        final double mean = sum / pressures.size();

        double sumOfSquares = 0.0;
        for (double pressure : pressures) {
            sumOfSquares += Math.pow(pressure - mean , 2);
        }
        final double standardDeviation = Math.sqrt(sumOfSquares / pressures.size());

        return new Pair<>(mean, standardDeviation);
    }
}
