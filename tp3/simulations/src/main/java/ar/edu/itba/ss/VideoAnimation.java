package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.CircularState;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VideoAnimation {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        if (parameters.isCircular()){
            final CircularState circularState = new CircularState(particles, parameters.getL());
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
            for (int i = 0; circularState.timeElapsed() < parameters.getTime() ; i++) {
                OutputUtils.printTime(writer, circularState.timeElapsed());
                OutputUtils.printVideoFrameHeader(writer);

                MolecularDynamicsMethod.runCircularIteration(circularState);

                OutputUtils.printVideoFrame(writer, circularState.particles());
                OutputUtils.printSeparator(writer);
            }
        }
        else {
            // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
            final BoxState boxState = new BoxState(particles, parameters.getL());
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
            for (int i = 0; boxState.timeElapsed() < parameters.getTime() ; i++) {
                OutputUtils.printTime(writer, boxState.timeElapsed());
                OutputUtils.printVideoFrameHeader(writer);

                MolecularDynamicsMethod.runIteration(boxState);

                OutputUtils.printVideoFrame(writer, boxState.particles());
                OutputUtils.printSeparator(writer);
            }
        }
    }
}
