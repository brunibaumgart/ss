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

public class FractionTriesVsNj {
    public static void run(final Parameters parameters) throws Exception {
        final double deltaT = parameters.getDeltaT();

        final int njInitial = parameters.getPlots().getFractionTriesVsNj().getNjInitial();
        final int njStep = parameters.getPlots().getFractionTriesVsNj().getNjStep();
        final int njTotalSteps = parameters.getPlots().getFractionTriesVsNj().getNjTotalSteps();
        final int njTotal = njInitial + njStep*njTotalSteps;

        final int runs = parameters.getPlots().getFractionTriesVsNj().getRuns();
        final int attempts = parameters.getPlots().getFractionTriesVsNj().getAttempts();

        // Create output files
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "tries_vs_nj.txt");

        for (int nj= njInitial; nj < njTotal; nj += njStep){
            parameters.setNj(nj);
            for (int run = 0; run < runs; run++){
                int tries = 0;
                for (int attempt = 0; attempt < attempts; attempt++) {
                    final Random random = new Random();
                    final long seed = random.nextLong();
                    final SimulationState state = SimulationUtils.createState(parameters, seed);

                    // Run simulation until red player reaches the end of the field, or collides with another player
                    while (true) {
                        // Run simulation
                        VerletAlgorithm.runIteration(state, deltaT);

                        // Check if red player reached the end of the field
                        final Particle redPlayer = state.particles().get(0);
                        if ((int) (redPlayer.position().x()) >= (int) state.width()) {// Red player reached the end of the field
                            tries++;
                            break;
                        } else if (state.particles().stream().filter(p -> !p.equals(redPlayer)).anyMatch(p -> p.collidesWith(redPlayer))) {
                            break;
                        }
                    }
                }
                OutputUtils.printNjVsTries(writer, tries, nj);
            }
        }
    }
}
