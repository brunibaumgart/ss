package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.events.CircularWallCollisionEvent;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.models.events.ParticleCollisionEvent;
import ar.edu.itba.ss.models.events.WallCollisionEvent;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PressureVsTime {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        final double dt = parameters.getPlots().getPressureVsTime().getDt();
        final int intervals = (int) (parameters.getTime() / dt);
        final List<Double> wallPressure = new ArrayList<>();
        final List<Double> brownianPressure = new ArrayList<>();

        // Seteo las variables de la presión en 0 (las usamos para calcular la cantidad de movimiento de las particulas)

        for ( int i = 0 ; i < intervals ; i++ ) {
            wallPressure.add(0.0);
            brownianPressure.add(0.0);
        }

        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
        SimulationState simulationState = new SimulationState(particles, parameters.getL(), parameters.isCircular());
        for (int i = 0; Double.compare(simulationState.timeElapsed(), parameters.getTime()) < 0 ; i++) {
            //final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video_frames/frame_" + (i + 1) + ".txt");
            //OutputUtils.printTime(writer, boxState.timeElapsed());

            //OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(simulationState);
            if(Double.compare(simulationState.timeElapsed(), parameters.getTime()) >= 0)
                break;

            int currentInterval = (int) (simulationState.timeElapsed() / dt) ;
            if (simulationState.lastEvent().getType() == CollisionEvent.EventType.WALL_COLLISION) {
                final WallCollisionEvent event = (WallCollisionEvent) simulationState.lastEvent();
                double deltaVx = event.p().speed().x() - event.p().previousSpeed().x();
                double deltaVy = event.p().speed().y() - event.p().previousSpeed().y();
                double impulse = event.p().mass() * Math.sqrt(deltaVx * deltaVx + deltaVy * deltaVy);
                wallPressure.set(currentInterval, wallPressure.get(currentInterval) + impulse);
            } else if (simulationState.lastEvent().getType() == CollisionEvent.EventType.CIRCULAR_WALL_COLLISION) {
                final CircularWallCollisionEvent event = (CircularWallCollisionEvent) simulationState.lastEvent();
                double deltaVx = event.p().speed().x() - event.p().previousSpeed().x();
                double deltaVy = event.p().speed().y() - event.p().previousSpeed().y();
                double impulse = event.p().mass() * Math.sqrt(deltaVx * deltaVx + deltaVy * deltaVy);
                wallPressure.set(currentInterval, wallPressure.get(currentInterval) + impulse);
            } else {
                final ParticleCollisionEvent event = (ParticleCollisionEvent) simulationState.lastEvent();
                Particle p1 = event.getParticles().get(0);
                Particle p2 = event.getParticles().get(1);
                // Checkeamos que la colisión sea entre la partícula browniana (id -1) y otra partícula
                if (p1.id() == ParticleUtils.BROWNIAN_ID || p2.id() == ParticleUtils.BROWNIAN_ID) {
                    // Calculamos el impulso para la partícula browniana
                    Particle particle = (p1.id() == ParticleUtils.BROWNIAN_ID) ? p2 : p1;
                    double deltaVx = particle.speed().x() - particle.previousSpeed().x();
                    double deltaVy = particle.speed().y() - particle.previousSpeed().y();
                    double impulse = particle.mass() * Math.sqrt(deltaVx * deltaVx + deltaVy * deltaVy);
                    brownianPressure.set(currentInterval, brownianPressure.get(currentInterval) + impulse);
                }

            //OutputUtils.printVideoFrame(writer, boxState.particles());
            }
        }

        for ( int i = 0 ; i < intervals ; i++ ) {
            wallPressure.set(i, wallPressure.get(i) / ( 4 * parameters.getL() * dt));
            brownianPressure.set(i, brownianPressure.get(i) / (2 * Math.PI * parameters.getRb() *  dt));
        }

        writePressuresToFile(intervals, parameters, wallPressure, brownianPressure);
    }


    private static void writePressuresToFile(int intervals, Parameters parameters, List<Double> wallPressure, List<Double> brounianPressure) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FilePaths.OUTPUT_DIR + "pressures.txt"))) {
            double dt = parameters.getPlots().getPressureVsTime().getDt();
            int decimalPlaces = getDecimalPlaces(dt); // Obtenemos la cantidad de decimales de dt

            // Escribir los datos en filas
            for (int i = 0; i < intervals; i++) {
                double interval = i * dt; // Cálculo de cada intervalo sumando dt
                double wp = wallPressure.get(i); // Presión de pared para el intervalo actual
                double bp = brounianPressure.get(i); // Presión browniana para el intervalo actual

                // Formatear el valor de interval con la misma cantidad de decimales que dt
                String format = "%." + decimalPlaces + "f %f %f";
                writer.write(String.format(Locale.US, format, interval, wp, bp));
                writer.newLine(); // Nueva línea después de cada fila
            }

            System.out.println("Archivo pressures.txt escrito con éxito en formato de filas.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    // Función auxiliar para obtener la cantidad de decimales de un número
    private static int getDecimalPlaces(double number) {
        String[] parts = String.valueOf(number).split("\\.");
        return parts.length > 1 ? parts[1].length() : 0;
    }


}
