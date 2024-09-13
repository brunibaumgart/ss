package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VideoAnimation {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
        final BoxState boxState = new BoxState(particles, parameters.getL());
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        for (int i = 0; i < parameters.getIterations(); i++) {
            OutputUtils.printTime(writer, boxState.timeElapsed());
            OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(boxState);

            OutputUtils.printVideoFrame(writer, boxState.particles());
            OutputUtils.printSeparator(writer);
        }
    }
}
