package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class Main
{
    private static final String CONFIG_FILE = "config.json";

    private static final int BROWNIAN_ID = -1;

    public static void main(String[] args) throws IOException
    {
        // 0. Levantar los parámetros del config.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);

        // 1.a. Crear partícula Browniana
        final Particle brownianParticle = new Particle(
                BROWNIAN_ID,
                parameters.getRb(),
                new Vector(parameters.getL() / 2, parameters.getL() / 2),
                Vector.fromPolar(0, 0),
                parameters.isMovable()? parameters.getMassB() : Double.POSITIVE_INFINITY
        );

        // 1.b. Se definen las posiciones y velocidades iniciales, los radios y tamaño de la caja
        final List<Particle> particles = ParticleUtils.createMovingParticles(
                parameters.getN(),
                parameters.getL(),
                parameters.getRp(),
                parameters.getSpeed(),
                parameters.getMassP()
        );

        // Creamos una clase estatica, le pasamos las particulas y te hace una iteración

        // 4. Se guarda el estado del sistema (posiciones y velocidades) en t = tc
        // 5. Con el "operador de colisión" se determinan las nuevas velocidades después del choque, solo para las
        // partículas que chocaron
    }
}
