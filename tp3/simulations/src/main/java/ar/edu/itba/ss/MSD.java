package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MSD {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
        final BoxState boxState = new BoxState(particles, parameters.getL());
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final FileWriter dcmWriter = new FileWriter(FilePaths.OUTPUT_DIR + "msd.txt");

        for (int i = 0; boxState.timeElapsed() < parameters.getTime(); i++) {
            OutputUtils.printTime(writer, boxState.timeElapsed());
            OutputUtils.printVideoFrameHeader(writer);

            OutputUtils.printTimeAndPosition(
                    dcmWriter,
                    boxState.particles().parallelStream().filter(p -> p.id() == ParticleUtils.BROWNIAN_ID).findFirst().orElseThrow(IllegalAccessError::new),
                    boxState.timeElapsed()
            );

            MolecularDynamicsMethod.runIteration(boxState);

            OutputUtils.printVideoFrame(writer, boxState.particles());
            OutputUtils.printSeparator(writer);
        }
    }
}
