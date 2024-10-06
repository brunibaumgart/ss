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

public class AmplitudeVsW {

    public static void run(Parameters parameters) throws IOException {
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "amplitude_vs_w.txt");
        OutputUtils.printAmplitudeVsWHeader(writer);

        final double wStep = parameters.getPlots().getAmplitudeVsW().getWStep();
        final double steps = parameters.getPlots().getAmplitudeVsW().getSteps();
        final double initialW = parameters.getPlots().getAmplitudeVsW().getInitialW();
        final double lastW = initialW + wStep*steps;

        // Set system parameters
        final double k = parameters.getK(); // g/s^2
        final double A = parameters.getA(); // cm

        // Set particle parameters
        final int N = parameters.getN();
        final double mass = parameters.getM(); // kg

        for (double i = initialW; i <= lastW; i+=wStep){
            final double[] maxAmplitude = {0};

            final SystemParameters systemParameters = new SystemParameters(k, Double.NaN, A, i);

            final double dt = 1 / (100 * i); // s
            final double totalTime = parameters.getTime(); // s

            final List<Particle> particles = ParticleUtils.createParticles(N, mass, 0);

            final SimulationState state = new SimulationState(particles, systemParameters);

            while (state.timeElapsed() < totalTime) {
                state.particles().stream()
                        .filter(particle -> particle.id() != (N - 1) && particle.id() != 0)
                        .map(Particle::position)
                        .map(Math::abs)
                        .max(Comparator.comparingDouble(Double::doubleValue))
                        .ifPresent(amplitude -> {
                            if (amplitude > maxAmplitude[0]) {
                                maxAmplitude[0] = amplitude;
                            }
                        });

                VerletAlgorithm.runIteration(state, dt);
            }

            OutputUtils.printAmplitudeVsW(writer, maxAmplitude[0], i);
        }
    }
}
