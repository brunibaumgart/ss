package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.SimulationUtils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FractionTriesVsNj {
    public static void run(final Parameters parameters) throws Exception {
        final double deltaT = parameters.getDeltaT();

        final List<Integer> njs = new ArrayList<>(); // Hardcoded njs because of the fast decrease in tries
        njs.add(15);
        njs.add(20);
        njs.add(30);
        njs.add(45);
        njs.add(100);

        //final int njInitial = parameters.getPlots().getFractionTriesVsNj().getNjInitial();
        //final int njStep = parameters.getPlots().getFractionTriesVsNj().getNjStep();
        //final int njTotalSteps = parameters.getPlots().getFractionTriesVsNj().getNjTotalSteps();
        //final int njTotal = njInitial + njStep*njTotalSteps;

        final int runs = parameters.getPlots().getFractionTriesVsNj().getRuns();
        final int attempts = parameters.getPlots().getFractionTriesVsNj().getAttempts();

        // Create output files
        final FileWriter fractionTriesVsNjWriter = new FileWriter(FilePaths.OUTPUT_DIR + "tries_vs_nj.txt");
        final FileWriter maxDistanceVsNjWriter = new FileWriter(FilePaths.OUTPUT_DIR + "max_distance_vs_nj.txt");

        for (Integer nj: njs){
            parameters.setNj(nj);
            int tries = 0;
            final List<Double> maxDistances = new ArrayList<>();
            for (int attempt = 0; attempt < attempts; attempt++) {
                final Random random = new Random();
                final long seed = random.nextLong();
                final SimulationState state = SimulationUtils.createState(parameters, seed);

                double maxDistance = 0;

                // Run simulation until red player reaches the end of the field, or collides with another player
                while (true) {
                    // Run simulation
                    VerletAlgorithm.runIteration(state, deltaT);

                    // Check if red player reached the end of the field
                    final Particle redPlayer = state.particles().get(0);
                    if( maxDistance < redPlayer.position().x()) {
                        maxDistance = redPlayer.position().x();
                    }
                    if ((int) (redPlayer.position().x()) >= (int) state.width()) {// Red player reached the end of the field
                        tries++;
                        break;
                    } else if (state.particles().stream().filter(p -> !p.equals(redPlayer)).anyMatch(p -> p.collidesWith(redPlayer))) {
                        break;
                    }
                }
                maxDistances.add(maxDistance);
            }
            OutputUtils.printNjVsTries(fractionTriesVsNjWriter, nj, (double) tries /attempts);

            final double meanDistance = maxDistances.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            final double stdDeviationDistance = Math.sqrt(maxDistances.stream()
                    .mapToDouble(v -> Math.pow(v - meanDistance, 2))
                    .average()
                    .orElse(0.0));

            OutputUtils.printMaxDistanceVsNj(maxDistanceVsNjWriter, nj, meanDistance, stdDeviationDistance);
        }
    }
}
