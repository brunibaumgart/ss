package ar.edu.itba.ss.utils;


import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OutputUtils {
    private OutputUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void printParameters(FileWriter writer, Parameters parameters) {
        try {
            writer.write(String.format("N %d\n", parameters.getN()));
            writer.write(String.format("L %.2f\n", parameters.getL()));
            writer.write(String.format("R %.2f\n", parameters.getR()));
            writer.write(String.format("V %.2f\n", parameters.getV()));
            writer.write(String.format("RC %.2f\n", parameters.getRc()));
            writer.write(String.format("Etha %.2f\n", parameters.getEtha()));
            writer.write(String.format("M %d\n", parameters.getM()));
            writer.write(String.format("Iterations %d\n", parameters.getIterations()));
            writer.write(String.format("Dt %d\n", parameters.getDt()));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printParticleDataHeader(FileWriter writer) {
        try {
            writer.write("id x y vx vy neighbours\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printParticleData(FileWriter writer, MovingParticle particle, List<? extends Particle> neighbours) throws IOException {
        if (neighbours == null || neighbours.isEmpty()) {
            writer.write(String.format("%s %.2f %.2f %.2f %.2f []\n", particle.id(), particle.position().x(), particle.position().y(), particle.velocity().x(), particle.velocity().y()));
            writer.flush();
            return;
        }

        String neighboursIds = neighbours.stream()
                .map(Particle::id)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        writer.write(String.format("%s %.2f %.2f %.2f %.2f [%s]\n", particle.id(), particle.position().x(), particle.position().y(), particle.velocity().x(), particle.velocity().y(), neighboursIds));
        writer.flush();
    }
}
