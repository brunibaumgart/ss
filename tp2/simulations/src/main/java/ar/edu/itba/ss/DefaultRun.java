package ar.edu.itba.ss;


import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DefaultRun {
    public static void run(Parameters parameters) throws IOException {
        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(parameters.getCim().getN(), parameters.getCim().getL(), parameters.getCim().getR(), parameters.getSpeed());

        CellIndexMethod cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, particles);
        final OffLaticeMethod ofm = new OffLaticeMethod(parameters.getSpeed(), parameters.getCim().getRc(), parameters.getEtha());

        for (int i = 0; i < parameters.getIterations(); i++) {
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "outputs/output_" + i + ".txt");
            OutputUtils.printParticleDataHeader(writer);

            final List<MovingParticle> newParticles = ofm.runIteration(cim, parameters.getDt(), writer);
            cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, newParticles);
        }
    }
}
