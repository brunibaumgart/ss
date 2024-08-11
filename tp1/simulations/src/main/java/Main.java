import methods.BruteForceMethod;
import methods.CellIndexMethod;
import models.Parameters;
import models.Particle;
import utils.ArgumentHandlerUtils;
import utils.ParticleUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class Main {
    private static final String PARAMETERS_FILE = "cim.json";

    public static void main(String[] args) {
        // Get parameters from cim.json
        final Parameters parameters;
        try {
            parameters = ArgumentHandlerUtils.getParameters(PARAMETERS_FILE);
        } catch (IOException e) {
            System.out.println("Error reading parameters file");
            e.printStackTrace();
            exit(1);
            return;
        }

        // Generar particulas (sin que se pisen)
        final List<Particle> particles = ParticleUtils.createParticles(parameters.getN(), parameters.getL(), parameters.getR());

        // Hacemos el metodo que toque (CIM ó FUERZA BRUTA)
        final CellIndexMethod cim = new CellIndexMethod(parameters.getM(), parameters.getL(), parameters.isPeriodic(), particles);
        final BruteForceMethod bfm = new BruteForceMethod(parameters.getL(), parameters.isPeriodic(), particles);

        // Calcular vecinos
        final Map<Particle, List<Particle>> neighborsCIM = cim.calculateNeighbors(parameters.getRc());
        final Map<Particle, List<Particle>> neighborsBFM = bfm.calculateNeighbors(parameters.getRc());

        // Imprimir resultados
        System.out.println("--------------");
        System.out.println("Neighbors CIM");
        neighborsCIM.forEach((k, v) -> System.out.println(k.id() + " -> " + Arrays.toString(v.stream().map(Particle::id).toArray())));

        System.out.println("--------------");
        System.out.println("Neighbors BFM");
        neighborsBFM.forEach((k, v) -> System.out.println(k.id() + " -> " + Arrays.toString(v.stream().map(Particle::id).toArray())));
    }
}
