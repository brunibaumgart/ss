package ar.edu.itba.ss;


import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DefaultRun {
    public static void run(Parameters parameters) throws IOException {
        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(parameters.getN(), parameters.getL(), parameters.getR(), parameters.getV());

        CellIndexMethod cim = new CellIndexMethod(parameters.getM(), parameters.getL(), true, particles);
        final OffLaticeMethod ofm = new OffLaticeMethod(parameters.getV(), parameters.getRc(), parameters.getEtha());

        for (int i = 0; i < parameters.getIterations(); i++) {
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "outputs/output_" + i + ".txt");
            OutputUtils.printParticleDataHeader(writer);

            final List<MovingParticle> newParticles = ofm.runIteration(cim, parameters.getDt(), writer);
            cim = new CellIndexMethod(parameters.getM(), parameters.getL(), true, newParticles);
        }
    }
}
