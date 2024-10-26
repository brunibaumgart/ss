package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.forces.Force;
import ar.edu.itba.ss.models.forces.SocialForceBlue;
import ar.edu.itba.ss.models.forces.SocialForceRed;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    private static final String CONFIG_FILE = "config.json";

    public static void main( String[] args ) throws Exception {
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);
        //final FileWriter parametersWriter = new FileWriter(FilePaths.PARAMETERS_FILE);

        final Vector position = new Vector(0 + parameters.getRadius(), parameters.getHeight()/2);

        final Force forceRed = new SocialForceRed(parameters.getTauRed(), parameters.getA(), parameters.getB(), parameters.getKn());
        final Force forceBlue = new SocialForceBlue(parameters.getTauBlue(), parameters.getA(), parameters.getB(), parameters.getKn());

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

        if(parameters.getPlots().getPositionVsTime().isEnabled()) {
            final List<Particle> aux = new ArrayList<>();
            aux.add(red);
            final List<Particle> particles = ParticleUtils.createMovingParticles(
                    parameters.getPlots().getPositionVsTime().getSeed(),
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

            PositionVsTime.run(state, parameters);
        }

        if(parameters.getPlots().getMaxDistance().isEnabled()) {
            MaxDistance.run(parameters);
        }
    }
}
