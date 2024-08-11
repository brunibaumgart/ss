package utils;

import models.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OutputUtils {
    private OutputUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void printIdsHeader(FileWriter writer) throws IOException {
        writer.write("id neighbours\n");
        writer.flush();
    }

    public static void printIds(FileWriter writer, Particle particle, List<Particle> neighbours) throws IOException {
        if(neighbours == null || neighbours.isEmpty()) {
            writer.write(String.format("%s []\n", particle.id()));
            writer.flush();
            return;
        }

        String neighbourIds = neighbours.stream()
                .map(Particle::id)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        writer.write(String.format("%s [%s]\n", particle.id(), neighbourIds));
        writer.flush();
    }

    public static void printPositionsHeader(FileWriter writer) throws IOException {
        writer.write("id x y neighbours\n");
        writer.flush();
    }

    public static void printPositions(FileWriter writer, Particle particle, List<Particle> neighbours) throws IOException {
        if(neighbours == null || neighbours.isEmpty()) {
            writer.write(String.format("%s %.2f %.2f []\n", particle.id(), particle.position().x(), particle.position().y()));
            writer.flush();
            return;
        }

        String neighboursIds = neighbours.stream()
                .map(Particle::id)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        writer.write(String.format("%s %.2f %.2f [%s]\n", particle.id(), particle.position().x(), particle.position().y(), neighboursIds));
        writer.flush();
    }

    public static void printTime(FileWriter writer, long time) throws IOException {
        writer.write(String.format("%d ms\n", time));
        writer.flush();
    }
}
