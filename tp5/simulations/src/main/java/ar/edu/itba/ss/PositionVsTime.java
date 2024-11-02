package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.SimulationUtils;

import java.io.FileWriter;

public class PositionVsTime {
    public static void run(final Parameters parameters) throws Exception {
        // Obtain time parameters
        final double deltaT = parameters.getDeltaT();
        final double totalTime = parameters.getPlots().getPositionVsTime().getTotalTime();

        // Create particles and state
        final SimulationState state = SimulationUtils.createState(parameters, parameters.getPlots().getPositionVsTime().getSeed());

        final FileWriter videoWriter = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        while (state.timeElapsed() < totalTime) {
            if(state.iteration() % 100 == 0) {
                OutputUtils.printTime(videoWriter, state.timeElapsed());
                OutputUtils.printPositions(videoWriter, state.particles());
                OutputUtils.printSeparator(videoWriter);
            }

            VerletAlgorithm.runIteration(state, deltaT);

            @SuppressWarnings("OptionalGetWithoutIsPresent")
            final Particle redParticle = state.particles().stream().filter(p -> p.id() == -1).findFirst().get();
            /*
            if(Double.compare(redParticle.position().x() + redParticle.radius(), parameters.getWidth()) == 0) { // Red player reached the end of the field
                break;
            }
            else if(state.particles().stream().filter(p -> !p.equals(redParticle)).anyMatch(p -> p.collidesWith(redParticle))) {
                break;
            }

             */
        }
    }
}
