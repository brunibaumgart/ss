package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.SimulationUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MaxDistanceHeatmap {
    public static void run(final Parameters parameters) throws Exception {
        // Obtain parameters
        double aMin = parameters.getPlots().getMaxDistanceHeatmap().getAMin();
        double aMax = parameters.getPlots().getMaxDistanceHeatmap().getAMax();
        int aSteps = parameters.getPlots().getMaxDistanceHeatmap().getASteps();
        final double aIncrement = (aMax - aMin) / aSteps;

        double bMin = parameters.getPlots().getMaxDistanceHeatmap().getBMin();
        double bMax = parameters.getPlots().getMaxDistanceHeatmap().getBMax();
        int bSteps = parameters.getPlots().getMaxDistanceHeatmap().getBSteps();
        final double bIncrement = (bMax - bMin) / bSteps;

        // Create output files
        for(double A = aMin; A <= aMax; A += aIncrement) {
            for(double B = bMin; B <= bMax; B += bIncrement) {
                final Parameters newParameters = parameters.clone();
                newParameters.setA(A);
                newParameters.setB(B);

                final ExecutorService executor = Executors.newCachedThreadPool();
                executor.submit(new MyRunner(newParameters, A, B));
            }
        }
    }

    static class MyRunner implements Runnable {
        private final Parameters parameters;
        private final double A;
        private final double B;

        public MyRunner(Parameters parameters, double A, double B) {
            this.parameters = parameters;
            this.A = A;
            this.B = B;
        }

        @Override
        public void run() {
            try {
                runWithDiffParams(parameters, A, B);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void runWithDiffParams(final Parameters parameters, final double A, final double B) throws IOException {
        final double deltaT = parameters.getDeltaT();

        final String positionFileName = String.format("%smax_distance_heatmap/max_position_%.2f_%.2f.txt", FilePaths.OUTPUT_DIR, A, B);
        final FileWriter positionWriter = new FileWriter(positionFileName);
        for(int run = 0; run < parameters.getPlots().getMaxDistanceHeatmap().getRunsPerIteration(); run++) {
            double maxDistance = 0;
            final Random random = new Random();
            final long seed = random.nextLong();
            final SimulationState state = SimulationUtils.createState(parameters, seed);

            // Run simulation until red player reaches the end of the field, or collides with another player
            while(true) {
                // Run simulation
                VerletAlgorithm.runIteration(state, deltaT);

                // Check if red player reached the end of the field
                final Particle redPlayer = state.particles().get(0);
                if(maxDistance < redPlayer.position().x()) {
                    maxDistance = redPlayer.position().x();
                }

                if((int) (redPlayer.position().x()) >= (int) state.width()) { // Red player reached the end of the field
                    break;
                }
                else if(state.particles().stream().filter(p -> !p.equals(redPlayer)).anyMatch(p -> p.collidesWith(redPlayer))) {
                    break;
                }
            }

            // Print max distance of red player
            OutputUtils.printDistance(positionWriter, maxDistance);
        }
    }
}
