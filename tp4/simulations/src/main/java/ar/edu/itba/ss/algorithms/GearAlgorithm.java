package ar.edu.itba.ss.algorithms;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimulationState;
import ar.edu.itba.ss.models.SystemParameters;
import ar.edu.itba.ss.utils.DampedOscillatorUtils;
import ar.edu.itba.ss.utils.ForceUtils;

import java.util.ArrayList;
import java.util.List;

public class GearAlgorithm {

    public static void runIteration(SimulationState state, final double deltaT, final double[] coefficients, final ForceUtils forceUtils){
        final List<Particle> updatedParticles = new ArrayList<>();

        final SystemParameters systemParameters = state.systemParameters();

        state.particles().forEach(p -> {
                final double mass = p.mass();
                final List<Double> lastRs = p.lastRs();

                final double r0 = lastRs.get(0);
                final double r = lastRs.get(1);
                final double r1 = lastRs.get(2);
                final double r2 = lastRs.get(3);
                final double r3 = lastRs.get(4);
                final double r4 = lastRs.get(5);
                final double r5 = lastRs.get(6);

                // Predict
                final double rp = r + r1*deltaT + r2*(deltaT*deltaT)/2.0 + r3*(Math.pow(deltaT, 3))/6.0
                        + r4*(Math.pow(deltaT, 4))/24.0 + r5*(Math.pow(deltaT, 5))/120.0;
                final double rp1 = r1 + r2*deltaT + r3*(deltaT*deltaT)/2.0 + r4*(Math.pow(deltaT, 3))/6.0
                        + r5*(Math.pow(deltaT, 4))/24.0;
                final double rp2 = r2 + r3*deltaT + r4*(deltaT*deltaT)/2.0 + r5*(Math.pow(deltaT, 3))/6.0;
                final double rp3 = r3 + r4*deltaT + r5*(deltaT*deltaT)/2.0;
                final double rp4 = r4 + r5*deltaT;
                final double rp5 = r5;

                // Evaluate
                final double rAux = forceUtils.r2(systemParameters, r0, rp, rp1, mass);
                final double deltaA = rAux - rp2;
                final double deltaR2 = deltaA*deltaT*deltaT/2.0;

                // Correct
                final double rc = rp + coefficients[0]*deltaR2;
                final double rc1 = rp1 + coefficients[1]*deltaR2/deltaT;
                final double rc2 = rp2 + coefficients[2]*deltaR2*2.0/(deltaT*deltaT);
                final double rc3 = rp3 + coefficients[3]*deltaR2*6.0/Math.pow(deltaT, 3);
                final double rc4 = rp4 + coefficients[4]*deltaR2*24.0/Math.pow(deltaT, 4);
                final double rc5 = rp5 + coefficients[5]*deltaR2*120.0/Math.pow(deltaT, 5);

                // Update particle
                lastRs.set(1, rc);
                lastRs.set(2, rc1);
                lastRs.set(3, rc2);
                lastRs.set(4, rc3);
                lastRs.set(5, rc4);
                lastRs.set(6, rc5);

                final Particle updatedParticle = new Particle(p.id(), rc, r, rc1, r1, p.mass(), lastRs);
                updatedParticles.add(updatedParticle);
        });

        state.particles(updatedParticles);

        state.addTime(deltaT);
        state.addIteration();
    }
}
