package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;

public class PositionVsTime {
    public static void run(final SimulationState state, final Parameters parameters) throws Exception {
        final double deltaT = parameters.getPlots().getPositionVsTime().getDeltaT();
        final double totalTime = parameters.getPlots().getPositionVsTime().getTotalTime();

        final FileWriter videoWriter = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final FileWriter tryWriter = new FileWriter(FilePaths.OUTPUT_DIR + "try.txt");

        while (state.timeElapsed() < totalTime) {
            if(state.iteration() % 100 == 0) {
                OutputUtils.printTime(videoWriter, state.timeElapsed());
                OutputUtils.printPositions(videoWriter, state.particles());
                OutputUtils.printSeparator(videoWriter);
            }

            VerletAlgorithm.runIteration(state, deltaT);

            @SuppressWarnings("OptionalGetWithoutIsPresent")
            final Particle redParticle = state.particles().stream().filter(p -> p.id() == -1).findFirst().get();
            if(redParticle.position().x() == 0) { // Red player reached the end of the field
                break;
            }
            else if(state.particles().stream().filter(p -> !p.equals(redParticle)).anyMatch(p -> p.collidesWith(redParticle))) {
                break;
            }
        }

    }
}
