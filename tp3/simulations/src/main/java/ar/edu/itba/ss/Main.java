package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main
{
    private static final String CONFIG_FILE = "config.json";

    private static final int BROWNIAN_ID = -1;

    public static void main(String[] args) throws IOException
    {
        // 0. Levantar los parámetros del config.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);

        // 1.a. Crear partícula Browniana
        final Particle brownianParticle = new Particle(
                BROWNIAN_ID,
                parameters.getRb(),
                new Vector(parameters.getL() / 2, parameters.getL() / 2),
                Vector.fromPolar(0, 0),
                parameters.isMovable()? parameters.getMassB() : Double.POSITIVE_INFINITY
        );
        final List<Particle> aux = new ArrayList<>();
        aux.add(brownianParticle);

        // 1.b. Se definen las posiciones y velocidades iniciales, los radios y tamaño de la caja
        final List<Particle> particles = ParticleUtils.createMovingParticles(
                new ArrayList<>(),
                parameters.getN(),
                parameters.getL(),
                parameters.getRp(),
                parameters.getSpeed(),
                parameters.getMassP()
        );

        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
        final FileWriter frame0Writer = new FileWriter(FilePaths.OUTPUT_DIR + "video_frames/frame_0.txt");
        OutputUtils.printVideoFrameHeader(frame0Writer);
        OutputUtils.printVideoFrame(frame0Writer, particles);
        frame0Writer.close();

        BoxState boxState = new BoxState(particles, parameters.getL());
        for(int i = 0; i < parameters.getIterations(); i++){
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video_frames/frame_" + (i + 1) + ".txt");
            OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(boxState);

            OutputUtils.printVideoFrame(writer, boxState.particles());
        }
    }
}
