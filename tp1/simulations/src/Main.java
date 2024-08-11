import methods.BruteForceMethod;
import methods.CellIndexMethod;
import models.Particle;
import utils.ParticleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        int N = 30;
        double L = 7.00;
        double r = 0.1;
        boolean isPeriodic = false;

        // Generar particulas (sin que se pisen)
        final List<Particle> particles = ParticleUtils.createParticles(N, L, r);

        // Hacemos el metodo que toque (CIM ó FUERZA BRUTA)
        final CellIndexMethod cim = new CellIndexMethod(M, L, isPeriodic, particles);
        final BruteForceMethod bfm = new BruteForceMethod(particles);

        // Calcular vecinos
        final Map<Particle, List<Particle>> neighborsCIM = cim.calculateNeighbors(rc);
        final Map<Particle, List<Particle>> neighborsBFM = bfm.calculateNeighbors(rc);

        // Imprimir resultados
        System.out.println("--------------");
        System.out.println("Neighbors CIM");
        neighborsCIM.forEach((k, v) -> System.out.println(k.id() + " -> " + Arrays.toString(v.stream().map(Particle::id).toArray())));

        System.out.println("--------------");
        System.out.println("Neighbors BFM");
        neighborsBFM.forEach((k, v) -> System.out.println(k.id() + " -> " + Arrays.toString(v.stream().map(Particle::id).toArray())));
    }
}
