package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.BeemanAlgorithm;
import ar.edu.itba.ss.algorithms.EulerAlgorithm;
import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.SystemParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.SystemOneparameters;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Exercise1 {
    public static void run(Parameters parameters) throws IOException {
        SystemOneparameters systemOneParameters = parameters.getPlots().getSystemOne();
        // Set system parameters
        final int k = 10000;
        final int gamma = 100;
        final int A = 1;
        final SystemParameters systemParameters = new SystemParameters(k, gamma, A);

        final int totalTime = 5;

        // Set particle parameters
        final double mass = 70;
        // Set initial conditions for the particle
        final double initialPosition = 1;
        final double initialVelocity = (double) (-A * gamma) / (2 * mass);

        // Set time step
        final double deltaT = systemOneParameters.getDt(); ;

        // Set initial state
        final Particle particle = new Particle(1,
                initialPosition,
                EulerAlgorithm.calculatePosition(systemParameters, -deltaT, new Particle(1, initialPosition, initialVelocity, mass)),
                initialVelocity,
                EulerAlgorithm.calculateVelocity(systemParameters, -deltaT, new Particle(1, initialPosition, initialVelocity, mass)),
                mass
        );
        final List<Particle> particles = List.of(particle);

        final SimulationState state = new SimulationState(particles, systemParameters);

        // Create output file
        // CAMBIAR A MANO
        final String algorithm = systemOneParameters.getAlgorithm();
        final String outputFile = String.format(Locale.US, FilePaths.OUTPUT_DIR + "ej1/%s_%f.txt", algorithm, deltaT);
        final FileWriter writer = new FileWriter(outputFile);

        // Iterate over time
        while(state.timeElapsed() < totalTime) {
            OutputUtils.printPositionNoId(writer, state.timeElapsed(), state.particles().get(0));

            // Run algorithm
            if (algorithm.equals("beeman")) {
                BeemanAlgorithm.runIteration(state, deltaT);
            } else if (algorithm.equals("verlet")) {
                VerletAlgorithm.runIteration(state, deltaT);
            } else if ( algorithm.equals("gear")) {
                // GearAlgorithm.runIteration(state, deltaT);
            }
        }
    }
}
