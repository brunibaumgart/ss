package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.parameters.CimParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.VaVsEthaParameters;
import ar.edu.itba.ss.utils.OutputUtils;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VaVsEthaRun {
    public static void run(Parameters parameters) throws IOException {

        // Create file
        final FileWriter writer = new FileWriter(FilePaths.OUTPUT_DIR + "va_vs_etha.txt");
        OutputUtils.printVaVsEthaHeader(writer);

        // Get parameters
        final CimParameters cimParameters = parameters.getCim();
        final VaVsEthaParameters vaVsEthaParameters = parameters.getPlots().getVaVsEtha();
        Double etha = vaVsEthaParameters.getEtha();

        for (int i=0; i < vaVsEthaParameters.getSteps(); i++){
            // Find out va mean and std for given etha
            final List<MovingParticle> particles = ParticleUtils.createMovingParticles(cimParameters.getN(), cimParameters.getL(), cimParameters.getR(), parameters.getSpeed());
            List<MovingParticle> updatedParticles = Collections.unmodifiableList(particles);
            final OffLaticeMethod ofm = new OffLaticeMethod(parameters.getCim().getRc(), parameters.getSpeed(), etha);
            CellIndexMethod cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, particles);

            List<Double> va = new ArrayList<>();
            for (int j = 0; j < vaVsEthaParameters.getTotalIterations(); j++) {

                updatedParticles = ofm.runIterationNoPrints(cim, parameters.getDt(), updatedParticles);
                if (j >= vaVsEthaParameters.getStationaryIterations()){
                    va.add(OffLaticeMethod.calculateVa(updatedParticles, parameters.getSpeed()));
                }
                cim = new CellIndexMethod(parameters.getCim().getM(), parameters.getCim().getL(), true, updatedParticles);
            }

            System.out.println(va);
            final Double mean = va.stream()
                    .mapToDouble(Double::doubleValue).average().orElse(0);
            final Double std = Math.sqrt(va.stream()
                    .mapToDouble(num -> Math.pow(num - mean, 2))
                    .average()
                    .orElse(0.0));

            OutputUtils.printVaVsEtha(writer, etha, mean, std);

            etha += vaVsEthaParameters.getEthaStep();
        }
    }
}
