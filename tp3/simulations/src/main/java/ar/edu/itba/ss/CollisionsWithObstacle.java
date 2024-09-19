package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionsWithObstacle {
    public static void run(final Parameters parameters, final List<Particle> particles) throws IOException {
        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración
        SimulationState simulationState = new SimulationState(particles, parameters.getL(), parameters.isCircular());
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final FileWriter uniqueCollsionsTimeWriter = new FileWriter(FilePaths.OUTPUT_DIR + "unique_obstacle_collision_times.txt");
        final FileWriter allCollisionsTimeWriter = new FileWriter(FilePaths.OUTPUT_DIR + "obstacle_collision_times.txt");

        final Set<Particle> particlesThatAlreadyCollidedWithObstacle = new HashSet<>();

        for (int i = 0; simulationState.timeElapsed() < parameters.getTime(); i++) {
            //OutputUtils.printTime(writer, simulationState.timeElapsed());
            //OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(simulationState);
            if (simulationState.lastEvent().containsParticles(List.of(new Particle(ParticleUtils.BROWNIAN_ID)))) {
                OutputUtils.printTime(allCollisionsTimeWriter, simulationState.timeElapsed());

                final Particle notObstacle = simulationState.lastEvent().getParticles().get(0).id() == ParticleUtils.BROWNIAN_ID ?
                        simulationState.lastEvent().getParticles().get(1) : simulationState.lastEvent().getParticles().get(0);
                final boolean added = particlesThatAlreadyCollidedWithObstacle.add(notObstacle);
                if (added)
                    OutputUtils.printTime(uniqueCollsionsTimeWriter, simulationState.timeElapsed());
            }

            //OutputUtils.printVideoFrame(writer, simulationState.particles());
            //OutputUtils.printSeparator(writer);
        }
    }
}
