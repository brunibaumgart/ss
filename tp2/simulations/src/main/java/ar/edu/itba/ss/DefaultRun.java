package ar.edu.itba.ss;


import ar.edu.itba.ss.methods.CellIndexMethod;
import ar.edu.itba.ss.methods.OffLaticeMethod;
import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.ParticleUtils;

import java.util.List;

public class DefaultRun {
    public static void run(Parameters parameters) {
        final List<MovingParticle> particles = ParticleUtils.createMovingParticles(parameters.getN(), parameters.getL(), parameters.getR(), parameters.getV());

        CellIndexMethod cim = new CellIndexMethod(parameters.getM(), parameters.getL(), true, particles);
        final OffLaticeMethod ofm = new OffLaticeMethod(parameters.getV(), parameters.getRc(), parameters.getEtha());

        for (int i = 0; i < parameters.getIterations(); i++) {
            final List<MovingParticle> newParticles = ofm.runIteration(cim, parameters.getDt());
            cim = new CellIndexMethod(parameters.getM(), parameters.getL(), true, newParticles);
        }

    }
}
