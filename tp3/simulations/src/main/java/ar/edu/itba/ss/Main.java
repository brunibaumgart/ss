package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String CONFIG_FILE = "config.json";

    public static void main(String[] args) throws IOException {
        // 0. Levantar los parámetros del config.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);

        final Particle brownianParticle = new Particle(
                ParticleUtils.BROWNIAN_ID,
                parameters.getRb(),
                new Vector(parameters.getL() / 2, parameters.getL() / 2),
                Vector.fromPolar(0, 0),
                parameters.getPlots().getMsd().isEnabled() ? parameters.getPlots().getMsd().getMassB() : Double.POSITIVE_INFINITY
        );
        final List<Particle> aux = new ArrayList<>();
        aux.add(brownianParticle);

        List<Particle> particles;

        if (parameters.isCircular()){
            particles = ParticleUtils.createMovingParticlesCircular(aux,
                    parameters.getN(),
                    parameters.getL(),
                    parameters.getRp(),
                    parameters.getSpeed(),
                    parameters.getMassP());
        }
        else {
            particles = ParticleUtils.createMovingParticles(
                    aux,
                    parameters.getN(),
                    parameters.getL(),
                    parameters.getRp(),
                    parameters.getSpeed(),
                    parameters.getMassP()
            );
        }

        if (parameters.getVideo().isEnabled()) {
            VideoAnimation.run(parameters, particles);
        }

        if (parameters.getPlots().getPressureVsTime().isEnabled()) {
            PressureVsTime.run(parameters, particles);
        }

        if (parameters.getPlots().getCollisionsWithObstacle().isEnabled()) {
            CollisionsWithObstacle.run(parameters, particles);
        }

        if (parameters.getPlots().getMsd().isEnabled()) {
            MSD.run(parameters, particles);
        }

        if (parameters.getPlots().getPressureVsTemperature().isEnabled()){
            PressureVsTemperature.run(parameters, brownianParticle);
        }
    }
}
