package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.FileWriter;
import java.util.List;

public class OutputUtils {
    private OutputUtils() {
        throw new AssertionError("This class should not be instantiated.");
    }

    public static void printPosition(final FileWriter writer, final double time, final double position) {
        try {
            writer.write(time + " " + position + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printPositionNoId(final FileWriter writer, final double time, final  Particle particle) {
        try {
            writer.write(time + " " + particle.position() + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTime(final FileWriter writer, final double time) {
        try {
            writer.write(time + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printPositions(final FileWriter writer, final List<Particle> particles) {
        particles.stream().forEach(particle -> {
            try {
                writer.write(particle.id() + " " + particle.position() + "\n");
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void printSeparator(final FileWriter writer) {
        try {
            writer.write("########\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
