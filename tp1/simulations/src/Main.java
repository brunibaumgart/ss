import methods.CellIndexMethod;
import models.IdentifiableParticle;
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

        // Generar particulas (sin que se pisen)
        final List<IdentifiableParticle> particles = ParticleUtils.createParticles(N, L, r);

        // Hacemos el metodo que toque (CIM ó FUERZA BRUTA)
        CellIndexMethod cim = new CellIndexMethod(M, L, particles);

        // Imprimir grid
        System.out.println("Grid: ");
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                System.out.println(cim.getGrid()[i][j]);
            }
        }

        Map<IdentifiableParticle, List<IdentifiableParticle>> neighbors = cim.calculateNeighbors(rc);

        System.out.println("-----------------------------");
        System.out.println("Neighbours: ");
        neighbors.forEach((particle, neighbourParticles) -> {
            System.out.println("Particle: " + particle.id());
            System.out.println("Neighbours: " + Arrays.toString(neighbourParticles.toArray()));
        });

        // Imprimir resultados
    }
}