package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.SimulationUtils;

import java.io.FileWriter;
import java.util.Random;

public class MaxDistance {
    public static void run(final Parameters parameters) throws Exception {
        // Obtain parameters
        final int runs = parameters.getPlots().getMaxDistance().getRuns();
        final double deltaT = parameters.getDeltaT();

        // Create output files
        final FileWriter positionWriter = new FileWriter(FilePaths.OUTPUT_DIR + "max_positions.txt");
        final FileWriter seedWriter = new FileWriter(FilePaths.OUTPUT_DIR + "try_seed.txt");
        for(int run = 0; run < runs; run++) {
            final Random random = new Random();
            final long seed = random.nextLong();
            final SimulationState state = SimulationUtils.createState(parameters, seed);

            // Run simulation until red player reaches the end of the field, or collides with another player
            while(true) {
                // Run simulation
                VerletAlgorithm.runIteration(state, deltaT);

                // Check if red player reached the end of the field
                final Particle redPlayer = state.particles().get(0);
                if((int) (redPlayer.position().x()) >= (int) state.width()) { // Red player reached the end of the field
                    break;
                }
                else if(state.particles().stream().filter(p -> !p.equals(redPlayer)).anyMatch(p -> p.collidesWith(redPlayer))) {
                    break;
                }
            }

            // Print distance of red player
            final Particle r = state.particles().get(0);
            OutputUtils.printDistance(positionWriter, r.position().x());
            if((int) r.position().x() >= (int) state.width()) { // Red player reached the end of the field
                OutputUtils.printSeed(seedWriter, seed);
            }
        }
    }
}
