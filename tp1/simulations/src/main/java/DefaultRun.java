import constants.FilePaths;
import methods.BruteForceMethod;
import methods.CellIndexMethod;
import models.Parameters;
import models.Particle;
import utils.OutputUtils;
import utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class DefaultRun {
    public static void run(Parameters parameters) {
        // Generate particles without overlapping
        final List<Particle> particles = ParticleUtils.createParticles(parameters.getN(), parameters.getL(), parameters.getR());

        // Execute method (CIM or BFM)
        final long startTime, endTime;
        final Map<Particle, List<Particle>> neighbors;
        if(parameters.getMethod().equalsIgnoreCase("CIM")) {
            final CellIndexMethod cim = new CellIndexMethod(parameters.getM(), parameters.getL(), parameters.isPeriodic(), particles);

            startTime = System.currentTimeMillis();
            neighbors = cim.calculateNeighbors(parameters.getRc());
            endTime = System.currentTimeMillis();
        } else if (parameters.getMethod().equalsIgnoreCase("BFM")) {
            final BruteForceMethod bfm = new BruteForceMethod(parameters.getL(), parameters.isPeriodic(), particles);

            startTime = System.currentTimeMillis();
            neighbors = bfm.calculateNeighbors(parameters.getRc());
            endTime = System.currentTimeMillis();
        } else {
            throw new IllegalArgumentException("Method must be `cim` or `bfm`");
        }

        final long timeElapsed = endTime - startTime;

        // Print results
        try {
            final FileWriter idWriter = new FileWriter(FilePaths.OUTPUT_DIR + "output.txt");
            final FileWriter positionWriter = new FileWriter(FilePaths.OUTPUT_DIR + "positions.txt");

            // output.txt
            OutputUtils.printTime(idWriter, timeElapsed);
            OutputUtils.printIdsHeader(idWriter);
            // positions.txt
            OutputUtils.printTime(positionWriter, timeElapsed);
            OutputUtils.printPositionsHeader(positionWriter);

            for(Particle particle : particles) {
                final List<Particle> pNeighbors = neighbors.get(particle);

                OutputUtils.printIds(idWriter, particle, pNeighbors);
                OutputUtils.printPositions(positionWriter, particle, pNeighbors);
            }
        } catch (IOException e) {
            System.out.println("Error writing output file");
            e.printStackTrace();
            exit(1);
        }
    }
}
