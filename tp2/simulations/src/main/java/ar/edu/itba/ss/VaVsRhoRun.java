package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.CimParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.VaVsRhoParameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VaVsRhoRun {
    public static void run(Parameters parameters) throws IOException {

        // Create file
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "va_vs_rho.txt");
        OutputUtils.printVaVsRhoHeader(writer);

        final CimParameters cimParameters = parameters.getCim();
        final VaVsRhoParameters vaVsRhoParameters = parameters.getPlots().getVaVsRho();

        int currentN = vaVsRhoParameters.getInitialN();
        for (int i = 0; i < vaVsRhoParameters.getSteps(); i++) {
            //steps run completas

            final List<MovingParticle> particles = ParticleUtils.createMovingParticles(currentN, cimParameters.getL(), cimParameters.getR(), parameters.getSpeed());
            List<MovingParticle> updatedParticles = Collections.unmodifiableList(particles);

            final OffLaticeMethod ofm = new OffLaticeMethod(cimParameters.getRc(), parameters.getSpeed(), vaVsRhoParameters.getEtha());
            CellIndexMethod cim = new CellIndexMethod(cimParameters.getM(), cimParameters.getL(), true, particles);

            List<Double> va = new ArrayList<>();
            for (int j = 0; j < vaVsRhoParameters.getTotalIterations(); j++) {

                updatedParticles = ofm.runIterationNoPrints(cim, parameters.getDt(), updatedParticles);
                if (j >= vaVsRhoParameters.getStationaryIterations()){
                    va.add(OffLaticeMethod.calculateVa(updatedParticles, parameters.getSpeed()));
                }
                cim = new CellIndexMethod(cimParameters.getM(), cimParameters.getL(), true, updatedParticles);
            }
            final Double mean = va.stream()
                    .mapToDouble(Double::doubleValue).average().orElse(0);
            final Double std = Math.sqrt(va.stream()
                    .mapToDouble(num -> Math.pow(num - mean, 2))
                    .average()
                    .orElse(0.0));

            double rho = currentN / Math.pow(cimParameters.getL(), 2);

            OutputUtils.printVaVsRho(writer,rho,mean,std);

            currentN += vaVsRhoParameters.getNStep();
        }
    }
}
