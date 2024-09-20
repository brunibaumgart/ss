package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VideoAnimation {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        SimulationState simulationState = new SimulationState(particles, parameters.getL(), parameters.isCircular());
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        for (int i = 0; simulationState.timeElapsed() < parameters.getTime() - 19 ; i++) {
            OutputUtils.printTime(writer, simulationState.timeElapsed());
            OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(simulationState );

            OutputUtils.printVideoFrame(writer, simulationState.particles());
            OutputUtils.printSeparator(writer);
        }
    }
}
