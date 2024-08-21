package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.CimParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.TimeVsVaParameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TimeVsVaRun {
    public static void run(Parameters parameters) throws IOException {
        final CimParameters cimParameters = parameters.getCim();
        final TimeVsVaParameters timeVsVaParameters = parameters.getPlots().getTimeVsVa();

        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(cimParameters.getN(), cimParameters.getL(), cimParameters.getR(), parameters.getSpeed());

        final OffLaticeMethod ofm = new OffLaticeMethod(parameters.getSpeed(), parameters.getCim().getRc(), timeVsVaParameters.getEtha());
        CellIndexMethod cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, particles);
        List<MovingParticle> updatedParticles = Collections.unmodifiableList(particles);

        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "time_vs_va.txt");

        OutputUtils.printTimeVsVaParameters(writer, timeVsVaParameters);
        OutputUtils.printTimeVsVaHeader(writer);

        for (int i = 0; i < timeVsVaParameters.getIterations(); i++) {
            OutputUtils.printTimeVsVa(writer, i, OffLaticeMethod.calculateVa(updatedParticles));

            updatedParticles = ofm.runIterationNoPrints(cim, parameters.getDt(), updatedParticles);

            cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, updatedParticles);
        }
    }
}
