package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.CimParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.TimeVsVaParameters;
import ar.edu.itba.ss.models.parameters.VaVsEthaParameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VaVsEthaRun {
    public static void run(Parameters parameters) throws IOException {
        // Find out t value for given initial etha so that va = 1
        final CimParameters cimParameters = parameters.getCim();
        final VaVsEthaParameters vaVsEthaParameters = parameters.getPlots().getVaVsEtha();

        final OffLaticeMethod ofm = new OffLaticeMethod(parameters.getSpeed(), parameters.getCim().getRc(), vaVsEthaParameters.getEtha());

        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(cimParameters.getN(), cimParameters.getL(), cimParameters.getR(), parameters.getSpeed());
        CellIndexMethod cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, particles);
        List<MovingParticle> updatedParticles = Collections.unmodifiableList(particles);

        int cutoffIterations = 0;
        boolean isFinished = false;
        for (int i = 0; i < vaVsEthaParameters.getInitialIterations() && !isFinished; i++) {

            updatedParticles = ofm.runIterationNoPrints(cim, parameters.getDt(), updatedParticles);
            if (OffLaticeMethod.calculateVa(updatedParticles) > 0.99){
                cutoffIterations =  i;
                isFinished = true;
            }

            cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, updatedParticles);
        }

        // Write plot data in files
        for (int j=0; j < vaVsEthaParameters.getRepeatPerEtha(); j++) {

            final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "va_vs_etha/" +
                    "va_vs_etha_" + j + ".txt");

            OutputUtils.printVaVsEthaHeader(writer);

            Double etha = vaVsEthaParameters.getEtha();
            for (int i = 0; i < vaVsEthaParameters.getSteps(); i++) {

                final OffLaticeMethod plotOfm = new OffLaticeMethod(parameters.getSpeed(), parameters.getCim().getRc(), etha);
                final List<MovingParticle> plotParticles = ParticleUtils.createMovingParticles(cimParameters.getN(), cimParameters.getL(), cimParameters.getR(), parameters.getSpeed());
                List<MovingParticle> updatedPlotParticles = Collections.unmodifiableList(plotParticles);

                for (int k = 0; k < cutoffIterations; k++) {
                    updatedPlotParticles = plotOfm.runIterationNoPrints(cim, parameters.getDt(), updatedPlotParticles);
                    cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, updatedPlotParticles);
                }
                OutputUtils.printVaVsEtha(writer, OffLaticeMethod.calculateVa(updatedPlotParticles), etha);
                etha += vaVsEthaParameters.getEtha();
            }
        }
    }
}
