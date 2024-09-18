package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MSD {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
        final SimulationState simulationState = new SimulationState(particles, parameters.getL(), parameters.isCircular());

        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final FileWriter msdWriter = new FileWriter(FilePaths.OUTPUT_DIR + "msd.txt");

        while (simulationState.timeElapsed() < parameters.getTime()) {
            OutputUtils.printTime(writer, simulationState.timeElapsed());
            OutputUtils.printVideoFrameHeader(writer);

            OutputUtils.printTimeAndPosition(
                    msdWriter,
                    simulationState.particles().parallelStream().filter(p -> p.id() == ParticleUtils.BROWNIAN_ID).findFirst().orElseThrow(IllegalAccessError::new),
                    simulationState.timeElapsed()
            );

            MolecularDynamicsMethod.runIteration(simulationState);

            OutputUtils.printVideoFrame(writer, simulationState.particles());
            OutputUtils.printSeparator(writer);
        }
    }
}
