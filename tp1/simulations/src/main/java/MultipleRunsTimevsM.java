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

public class MultipleRunsTimevsM {
    public static void run(Parameters parameters, String outputFile) throws IOException {
        final FileWriter timeWriter = new FileWriter(FilePaths.OUTPUT_DIR + outputFile);
        OutputUtils.printTimeAndMHeader(timeWriter);

        for (int i = 0; i < parameters.getRuns(); i++) {
            runSingle(parameters, timeWriter);

            parameters.setM(parameters.getM() + 1);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private static void runSingle(Parameters parameters, FileWriter timeWriter) throws IOException {
        final List<Particle> particles = ParticleUtils.createParticles(parameters.getN(), parameters.getL(), parameters.getR());

        // Execute method (CIM or BFM)
        final long startTime, endTime;
        if (parameters.getMethod().equalsIgnoreCase("CIM")) {
            final CellIndexMethod cim = new CellIndexMethod(parameters.getM(), parameters.getL(), parameters.isPeriodic(), particles);

            startTime = System.currentTimeMillis();
            cim.calculateNeighbors(parameters.getRc());
            endTime = System.currentTimeMillis();
        } else if (parameters.getMethod().equalsIgnoreCase("BFM")) {
            final BruteForceMethod bfm = new BruteForceMethod(parameters.getL(), parameters.isPeriodic(), particles);

            startTime = System.currentTimeMillis();
            bfm.calculateNeighbors(parameters.getRc());
            endTime = System.currentTimeMillis();
        } else {
            throw new IllegalArgumentException("Method must be `cim` or `bfm`");
        }

        final long timeElapsed = endTime - startTime;

        // Print results
        OutputUtils.printTime(timeWriter, parameters.getM(), timeElapsed);
    }
}
