import models.Particle;
import utils.ParticleUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
        Parsear parametros
        Parámetros:
            - N number of Particles
            - L length of the box
            - rc cutoff radius
            - M -> MxM grid
         */
        int N = 30;
        double L = 7.00;
        double r = 0.1;

        // Generar particulas (sin que se pisen)
        final List<Particle> particles = ParticleUtils.createParticles(N, L, r);

        // Hacemos el metodo que toque (CIM ó FUERZA BRUTA)

        // Imprimir resultados
    }
}
