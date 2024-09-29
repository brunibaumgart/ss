package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.SystemParameters;

public class DampedOscillatorUtils implements ForceUtils{

    public double r2(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass){
        return -parameters.k()/mass * (r-r0) - parameters.gamma()/mass * r1;
    }

    public double r3(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass){
        return -parameters.k()/mass * r1 - parameters.gamma()/mass * r2(parameters, r0, r, r1, mass);
    }

    public double r4(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass){
        return -parameters.k()/mass * r2(parameters, r0, r, r1, mass) - parameters.gamma()/mass * r3(parameters, r0, r, r1, mass);
    }

    public double r5(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass){
        return -parameters.k()/mass * r3(parameters, r0, r, r1, mass) - parameters.gamma()/mass * r4(parameters, r0, r, r1, mass);
    }


}
