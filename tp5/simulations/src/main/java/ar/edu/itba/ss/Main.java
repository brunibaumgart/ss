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
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    private static final String CONFIG_FILE = "config.json";

    public static void main( String[] args ) throws IOException {
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);
        //final FileWriter parametersWriter = new FileWriter(FilePaths.PARAMETERS_FILE);

        final Vector position = new Vector(0 + parameters.getRadius(), parameters.getHeight()/2);

        final Force forceRed = new SocialForceRed(parameters.getTauRed(), parameters.getA(), parameters.getA(), parameters.getKn());
        final Force forceBlue = new SocialForceBlue(parameters.getTauBlue(), parameters.getA(), parameters.getB(), parameters.getKn());

        final double deltaT = 0.001;
        final double totalTime = 20;

        // 1. Crear corredor de equipo rojo a la derecha de la cancha.
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
        // 2. Crear Nj jugadores del equipo azul distribuidos aleatoriamente
        final List<Particle> aux = new ArrayList<>();
        aux.add(red);
        final List<Particle> particles = ParticleUtils.createMovingParticles(
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

        // 3. VerletAlgorithm.runIteration
        final FileWriter videoWriter = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final SimulationState state = new SimulationState(particles);

        while (state.timeElapsed() < totalTime) {
            if(state.iteration() % 100 == 0) {
                OutputUtils.printTime(videoWriter, state.timeElapsed());
                OutputUtils.printPositions(videoWriter, state.particles());
                OutputUtils.printSeparator(videoWriter);
            }

            VerletAlgorithm.runIteration(state, deltaT);
        }
    }
}
