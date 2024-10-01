package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.SystemParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class AmplitudeVsTime {
    public static void run(Parameters parameters) throws IOException {
        // set system parameters
        final double k = parameters.getK(); // g/s^2
        final double A = parameters.getA(); // cm
        final double omega = parameters.getPlots().getAmplitudeVsTime().getW();

        final SystemParameters systemParameters = new SystemParameters(k, Double.NaN, A, omega);

        final double dt = 1 / (100 * omega); // s
        final double totalTime = parameters.getTime(); // s

        // set particle parameters
        final int N = parameters.getN();
        final double mass = parameters.getM(); // kg
        final List<Particle> particles = ParticleUtils.createParticles(N, mass, 0);

        final SimulationState state = new SimulationState(particles, systemParameters);

        final FileWriter videoWriter = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final FileWriter amplitudeWriter = new FileWriter(FilePaths.OUTPUT_DIR + "amplitude_vs_time.txt");

        while (state.timeElapsed() < totalTime) {
            //OutputUtils.printTime(videoWriter, state.timeElapsed());
            //OutputUtils.printPositions(videoWriter, state.particles());

            // Print the maximum amplitud
            state.particles().parallelStream()
                    .filter(particle -> particle.id() != (N-1) && particle.id() != 0)
                    .map(Particle::position)
                    .map(Math::abs)
                    .max(Comparator.comparingDouble(Double::doubleValue))
                    .ifPresent(amplitude -> OutputUtils.printPosition(amplitudeWriter, state.timeElapsed(), amplitude));

            VerletAlgorithm.runIteration(state, dt);

            //OutputUtils.printSeparator(videoWriter);
        }
    }
}
