package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.Wall;
import ar.edu.itba.ss.models.forces.Force;
import ar.edu.itba.ss.models.forces.SocialForceBlue;
import ar.edu.itba.ss.models.forces.SocialForceRed;
import ar.edu.itba.ss.models.parameters.Parameters;

import java.util.ArrayList;
import java.util.List;

public class SimulationUtils {
    private SimulationUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create a simulation state with the given parameters and seed
     *
     * @param parameters parameters of the simulation
     * @param seed seed for the creation of the particles
     *
     * @return the simulation state
     */
    public static SimulationState createState(final Parameters parameters, final long seed) {
        final Vector position = new Vector(0 + parameters.getRadius(), parameters.getHeight()/2);


        final double width = parameters.getWidth();
        final double height = parameters.getHeight();
        final List<Wall> walls = List.of(
                new Wall(Wall.Side.DOWN, new Vector(0, 0), new Vector(width, 0)),
                new Wall(Wall.Side.LEFT, new Vector(0, 0), new Vector(0, height)),
                new Wall(Wall.Side.UP, new Vector(width, height), new Vector(0, height))
        );

        final Force forceRed = new SocialForceRed(parameters.getTauRed(), parameters.getA(), parameters.getB(), parameters.getKn(), walls);
        final Force forceBlue = new SocialForceBlue(parameters.getTauBlue(), parameters.getA(), parameters.getB(), parameters.getKn());

        // Create red player at the beginning of the field
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

        return new SimulationState(particles, width, height, walls);
    }
}
