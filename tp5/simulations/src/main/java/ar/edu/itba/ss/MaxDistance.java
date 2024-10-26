package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.forces.Force;
import ar.edu.itba.ss.models.forces.SocialForceBlue;
import ar.edu.itba.ss.models.forces.SocialForceRed;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MaxDistance {
    public static void run(final Parameters parameters) throws Exception {
        // Obtain parameters
        final int runs = parameters.getPlots().getMaxDistance().getRuns();
        final double deltaT = parameters.getPlots().getMaxDistance().getDeltaT();

        // Create output files
        final FileWriter positionWriter = new FileWriter(FilePaths.OUTPUT_DIR + "max_positions.txt");
        final FileWriter seedWriter = new FileWriter(FilePaths.OUTPUT_DIR + "try_seed.txt");
        for(int run = 0; run < runs; run++) {
            // Create a state for a specific run
            final Vector position = new Vector(0 + parameters.getRadius(), parameters.getHeight()/2);

            final Force forceRed = new SocialForceRed(parameters.getTauRed(), parameters.getA(), parameters.getB(), parameters.getKn());
            final Force forceBlue = new SocialForceBlue(parameters.getTauBlue(), parameters.getA(), parameters.getB(), parameters.getKn());

            final Particle red = Particle.builder()
                    .id(-1)
                    .mass(parameters.getMass())
                    .lastPosition(position)
                    .position(position)
                    .force(forceRed)
                    .radius(parameters.getRadius())
                    .velocity(new Vector(0,0))
                    .desiredVelocity(parameters.getDesiredVelocityRed())
                    .target(new Vector(parameters.getWidth(), parameters.getHeight()/2))
                    .build();
            final List<Particle> aux = new ArrayList<>();
            aux.add(red);
            final Random random = new Random();
            final long seed = random.nextLong();
            final List<Particle> particles = ParticleUtils.createMovingParticles(
                    seed,
                    aux,
                    parameters.getNj(),
                    parameters.getHeight(),
                    parameters.getWidth(),
                    parameters.getMass(),
                    forceBlue,
                    parameters.getRadius(),
                    parameters.getDesiredVelocityBlue(),
                    red.position()
            );

            final SimulationState state = new SimulationState(particles, parameters.getWidth());

            // Run simulation until red player reaches the end of the field, or collides with another player
            while(true) {
                // Run simulation
                VerletAlgorithm.runIteration(state, deltaT);

                // Check if red player reached the end of the field
                final Particle redPlayer = state.particles().get(0);
                if((int) redPlayer.position().x() <= (int) state.width()) { // Red player reached the end of the field
                    break;
                }
                else if(state.particles().stream().filter(p -> !p.equals(redPlayer)).anyMatch(p -> p.collidesWith(redPlayer))) {
                    break;
                }
            }

            // Print distance of red player
            final Particle r = state.particles().get(0);
            OutputUtils.printDistance(positionWriter, r.position().x());
            if((int) r.position().x() <= (int) state.width()) { // Red player reached the end of the field
                OutputUtils.printSeed(seedWriter, seed);
            }
        }
    }
}
