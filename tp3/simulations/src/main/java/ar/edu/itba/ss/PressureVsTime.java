package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.events.CollisionEvent;
import ar.edu.itba.ss.models.events.ParticleCollisionEvent;
import ar.edu.itba.ss.models.events.WallCollisionEvent;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        final BoxState boxState = new BoxState(particles, parameters.getL());
        for (int i = 0; Double.compare(boxState.timeElapsed(), parameters.getTime()) < 0 ; i++) {
            //final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video_frames/frame_" + (i + 1) + ".txt");
            //OutputUtils.printTime(writer, boxState.timeElapsed());

            //OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(boxState);
            if(Double.compare(boxState.timeElapsed(), parameters.getTime()) >= 0)
                break;

            int currentInterval = (int) (boxState.timeElapsed() / dt) ;
            if (boxState.lastEvent().getType() == CollisionEvent.EventType.WALL_COLLISION ) {
                final WallCollisionEvent event = (WallCollisionEvent) boxState.lastEvent();
                // sumo la cantidad de movimiento de esta colision a la presion
                wallPressure.set(currentInterval, wallPressure.get(currentInterval) + event.p().speed().x()); //todo
            }
            else {
                final ParticleCollisionEvent event = (ParticleCollisionEvent) boxState.lastEvent();
                // Checkeamos que la colision sea entre la particula browniana (id -1) y otra particula
                if (boxState.lastEvent().getParticles().get(0).id() == ParticleUtils.BROWNIAN_ID) {
                    //suma la cantidad de movimiento de la particula browniana a la que ya tiene
                    brownianPressure.set(currentInterval, brownianPressure.get(currentInterval) + 1); //todo
                }
                else {
                    brownianPressure.set(currentInterval, brownianPressure.get(currentInterval) + 1); //todo
                }

            }
            //OutputUtils.printVideoFrame(writer, boxState.particles());
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

            // Escribir los datos en filas
            for (int i = 0; i < intervals; i++) {
                double interval = i * dt; // Cálculo de cada intervalo sumando dt
                double wp = wallPressure.get(i); // Presión de pared para el intervalo actual
                double bp = brounianPressure.get(i); // Presión browniana para el intervalo actual

                // Escribe los valores separados por espacios
                writer.write(interval + " " + wp + " " + bp);
                writer.newLine(); // Nueva línea después de cada fila
            }

            System.out.println("Archivo pressures.txt escrito con éxito en formato de filas.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}
