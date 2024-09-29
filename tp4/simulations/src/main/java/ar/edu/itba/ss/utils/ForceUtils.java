package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.SystemParameters;

public interface ForceUtils {

    double r2(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass);

    double r3(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass);

    double r4(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass);

    double r5(final SystemParameters parameters, final double r0, final double r, final double r1, final double mass);

}
