package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.VerletAlgorithm;
import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.forces.Force;
import ar.edu.itba.ss.models.forces.SocialForce;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws IOException {
        final int Nj = 15;
        final double width = 100; // m
        final double height = 70; // m
        final double mass = 80; // kg
        final double radius = 0.25; // m
        final Vector position = new Vector(0 + radius, height/2);

        final double tauRed = 0.3; // s
        final double tauBlue = 0.5; // s
        final double A = 2000; // N
        final double B = 0.08; // m
        final double kn = 120000; // kg/s
        final Force forceRed = new SocialForce(tauRed, A, B, kn);
        final Force forceBlue = new SocialForce(tauBlue, A, B, kn);

        final double desiredVelocityRed = 4; // m/s
        final double desiredVelocityBlue = 3.8; // m/s

        final double deltaT = 0.01;
        final double totalTime = 5;

        // 1. Crear corredor de equipo rojo a la derecha de la cancha.
        final Particle red = Particle.builder()
                .id(-1)
                .mass(mass)
                .lastPosition(position)
                .position(position)
                .force(forceRed)
                .radius(radius)
                .velocity(new Vector(0,0))
                .desiredVelocity(desiredVelocityRed)
                .target(new Vector(width, height/2))
                .build();
        // 2. Crear Nj jugadores del equipo azul distribuidos aleatoriamente
        final List<Particle> aux = new ArrayList<>();
        aux.add(red);
        final List<Particle> particles = ParticleUtils.createMovingParticles(
                aux,
                Nj,
                height,
                width,
                mass,
                forceBlue,
                radius,
                desiredVelocityBlue,
                red.position()
        );

        // 3. VerletAlgorithm.runIteration
        final FileWriter videoWriter = new FileWriter(FilePaths.OUTPUT_DIR + "video.txt");
        final SimulationState state = new SimulationState(particles);

        while (state.timeElapsed() < totalTime) {
            OutputUtils.printTime(videoWriter, state.timeElapsed());
            OutputUtils.printPositions(videoWriter, state.particles());
            OutputUtils.printSeparator(videoWriter);

            VerletAlgorithm.runIteration(state, deltaT);
        }
    }
}
