import methods.CellIndexMethod;
import models.particle.IdentifiableParticle;
import utils.ParticleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
        double rc = 0.5;
        int M = 4;
        int N = 5;
        double L = 7.00;
        double r = 0.1;

        // Generar particulas (sin que se pisen)
        final List<IdentifiableParticle> particles = ParticleUtils.createParticles(N, L, r);

        // Hacemos el metodo que toque (CIM ó FUERZA BRUTA)
        CellIndexMethod cim = new CellIndexMethod(M, L, particles);

        // Imprimir resultados
    }
}
