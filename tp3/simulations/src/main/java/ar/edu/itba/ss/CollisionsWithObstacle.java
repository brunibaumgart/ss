package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.MolecularDynamicsMethod;
import ar.edu.itba.ss.models.BoxState;
import ar.edu.itba.ss.models.Particle;
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
        final BoxState boxState = new BoxState(particles, parameters.getL());
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final FileWriter uniqueCollsionsTimeWriter = new FileWriter(FilePaths.OUTPUT_DIR + "unique_obstacle_collision_times.txt");
        final FileWriter allCollisionsTimeWriter = new FileWriter(FilePaths.OUTPUT_DIR + "obstacle_collision_times.txt");

        final Set<Particle> particlesThatAlreadyCollidedWithObstacle = new HashSet<>();

        for (int i = 0; boxState.timeElapsed() < parameters.getTime(); i++) {
            OutputUtils.printTime(writer, boxState.timeElapsed());
            OutputUtils.printVideoFrameHeader(writer);

            MolecularDynamicsMethod.runIteration(boxState);
            if (boxState.lastEvent().containsParticles(List.of(new Particle(ParticleUtils.BROWNIAN_ID)))) {
                OutputUtils.printTime(allCollisionsTimeWriter, boxState.timeElapsed());

                final Particle notObstacle = boxState.lastEvent().getParticles().get(0).id() == ParticleUtils.BROWNIAN_ID ?
                        boxState.lastEvent().getParticles().get(1) : boxState.lastEvent().getParticles().get(0);
                final boolean added = particlesThatAlreadyCollidedWithObstacle.add(notObstacle);
                if (added)
                    OutputUtils.printTime(uniqueCollsionsTimeWriter, boxState.timeElapsed());
            }

            OutputUtils.printVideoFrame(writer, boxState.particles());
            OutputUtils.printSeparator(writer);
        }
    }
}
