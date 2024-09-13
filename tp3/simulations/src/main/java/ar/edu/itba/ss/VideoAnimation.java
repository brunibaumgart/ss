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
        BoxState boxState = new BoxState(particles, parameters.getL());
        for (int i = 0; boxState.timeElapsed() < parameters.getTime() ; i++) {
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video_frames/frame_" + (i + 1) + ".txt");
            OutputUtils.printTime(writer, boxState.timeElapsed());

            OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(boxState);

            OutputUtils.printVideoFrame(writer, boxState.particles());
        }
    }
}
