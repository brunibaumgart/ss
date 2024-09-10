package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main
{
    private static final String CONFIG_FILE = "config.json";

    private static final int BROWNIAN_ID = -1;

    public static void main(String[] args) throws IOException
    {
        // 0. Levantar los parámetros del config.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);

        final Particle brownianParticle = new Particle(
                BROWNIAN_ID,
                parameters.getRb(),
                new Vector(parameters.getL() / 2, parameters.getL() / 2),
                Vector.fromPolar(0, 0),
                parameters.isMovable()? parameters.getMassB() : Double.POSITIVE_INFINITY
        );
        final List<Particle> aux = new ArrayList<>();
        aux.add(brownianParticle);

        final List<Particle> particles = ParticleUtils.createMovingParticles(
                new ArrayList<>(),
                parameters.getN(),
                parameters.getL(),
                parameters.getRp(),
                parameters.getSpeed(),
                parameters.getMassP()
        );

        VideoAnimation.run(parameters, particles);
    }
}
