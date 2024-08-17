package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.BruteForceMethod;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultipleRunsTimevsN {
    public static void run(Parameters parameters, String outputFile) throws IOException {
        final FileWriter timeWriter = new FileWriter(FilePaths.OUTPUT_DIR + outputFile);
        OutputUtils.printTimeAndNHeader(timeWriter);

        for (int i = 0; i < parameters.getRuns(); i++) {
            runSingle(parameters, timeWriter);

            parameters.setN(parameters.getN() + parameters.getSteps());
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
        OutputUtils.printTime(timeWriter, parameters.getN(), timeElapsed);
    }
}
