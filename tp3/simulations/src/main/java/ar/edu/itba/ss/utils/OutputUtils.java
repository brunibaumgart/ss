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

    public static void printTime(final FileWriter writer, final double time) {
        try {
            writer.write("Time " + time + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printVideoFrameHeader(final FileWriter writer) {
        try {
            writer.write("id x y vx vy\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printVideoFrame(final FileWriter writer, final List<Particle> particles) {
        particles
                .forEach(p -> {
                    try {
                        writer.write(p.id() + " " + p.position().x() + " " + p.position().y() + " " + p.speed().x() + " " + p.speed().y() + "\n");
                        writer.flush();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void printSeparator(final FileWriter writer) {
        try {
            writer.write("#######\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTimeAndPosition(final FileWriter writer, final Particle particle, final double time) {
        try {
            writer.write(time + " " + particle.position().x() + " " + particle.position().y() + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printPressureAndTemperatureHeader(final FileWriter writer){
        try {
            writer.write("w_mean w_std o_mean o_std t" + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printPressureAndTemperature(final FileWriter writer,
                                                   final double wallPressureMean,
                                                   final double wallPressureDeviation,
                                                   final double obstaclePressureMean,
                                                   final double obstaclePressureDeviation,
                                                   final double temperature){
        try {
            writer.write(wallPressureMean + " " + wallPressureDeviation + " " + obstaclePressureMean + " "
                    + obstaclePressureDeviation + " " + temperature + "\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
