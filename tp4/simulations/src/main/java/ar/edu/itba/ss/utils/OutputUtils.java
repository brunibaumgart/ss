package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.FileWriter;

public class OutputUtils {
    private OutputUtils() {
        throw new AssertionError("This class should not be instantiated.");
    }

    public static void printPositionNoId(final FileWriter writer, final double time, final  Particle particle) {
        try {
            writer.write(time + " " + particle.position() + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
