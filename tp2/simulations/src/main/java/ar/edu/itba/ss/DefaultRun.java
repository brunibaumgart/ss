package ar.edu.itba.ss;


import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.VideoParameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class DefaultRun {
    public static void run(Parameters parameters) throws IOException {
        final VideoParameters videoParameters = parameters.getPlots().getVideo();
        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(parameters.getCim().getN(), parameters.getCim().getL(), parameters.getCim().getR(), videoParameters.getSpeed());

        final OffLaticeMethod ofm = new OffLaticeMethod(videoParameters.getSpeed(), parameters.getCim().getRc(), videoParameters.getEtha());
        CellIndexMethod cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, particles);
        List<MovingParticle> updatedParticles = Collections.unmodifiableList(particles);

        for (int i = 0; i < videoParameters.getIterations(); i++) {
            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "outputs/output_" + i + ".txt");
            OutputUtils.printParticleDataHeader(writer);

            updatedParticles = ofm.runIteration(cim, parameters.getDt(), updatedParticles, writer);

            cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, updatedParticles);
        }
    }
}
