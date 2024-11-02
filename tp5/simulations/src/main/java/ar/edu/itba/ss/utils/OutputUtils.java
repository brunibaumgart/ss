package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.FileWriter;
import java.util.List;
import java.util.Locale;

public class OutputUtils {
    private static final Locale LOCALE = Locale.US;

    private OutputUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void printSeparator(final FileWriter writer) {
        try {
            writer.write("#######\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printPositions(final FileWriter writer, final List<Particle> particles){
        for (final Particle particle: particles){
            try {
                writer.write(particle.id() + " " + particle.position().x() + " " + particle.position().y() + "\n");
                writer.flush();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void printTime(final FileWriter writer, final double time) {
        try {
            writer.write(time + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printDistance(final FileWriter writer, final double distance) {
        try {
            writer.write(distance + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printSeed(final FileWriter writer, final long seed) {
        try {
            writer.write(seed + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printNjVsTries(final FileWriter writer, final int nj, final int tries){
        try {
            writer.write(tries + " " + nj + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
