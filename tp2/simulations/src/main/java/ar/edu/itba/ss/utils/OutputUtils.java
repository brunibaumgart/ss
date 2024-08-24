package ar.edu.itba.ss.utils;


import ar.edu.itba.ss.models.MovingParticle;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.parameters.CimParameters;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.models.parameters.TimeVsVaParameters;
import ar.edu.itba.ss.models.parameters.VaVsEthaParameters;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class OutputUtils {
    private final static Locale LOCALE = Locale.US;


    private OutputUtils() {
        throw new IllegalStateException("Utility class");
    }

    /* PARAMETERS */
    public static void printCimParameters(FileWriter writer, CimParameters parameters) {
        try {
            writer.write(String.format(LOCALE, "N %d\n", parameters.getN()));
            writer.write(String.format(LOCALE, "L %.2f\n", parameters.getL()));
            writer.write(String.format(LOCALE, "M %d\n", parameters.getM()));
            writer.write(String.format(LOCALE, "R %.2f\n", parameters.getR()));
            writer.write(String.format(LOCALE, "RC %.2f\n", parameters.getRc()));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printDefaultParameters(FileWriter writer, Parameters parameters) {
        try {
            writer.write(String.format(LOCALE, "Speed %.2f\n", parameters.getSpeed()));
            writer.write(String.format(LOCALE, "Dt %d\n", parameters.getDt()));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* PARTICLE DATA (for video) */
    public static void printVideoParameters(FileWriter writer, double etha, int iterations) {
        try {
            writer.write(String.format(LOCALE, "Etha %.2f\n", etha));
            writer.write(String.format(LOCALE, "Iterations %d\n", iterations));
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
            writer.write(String.format(LOCALE, "%s %.2f %.2f %.2f %.2f []\n", particle.id(), particle.position().x(), particle.position().y(), particle.speed().x(), particle.speed().y()));
            writer.flush();
            return;
        }

        String neighboursIds = neighbours.stream()
                .map(Particle::id)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
        writer.write(String.format(LOCALE, "%s %.2f %.2f %.2f %.2f [%s]\n", particle.id(), particle.position().x(), particle.position().y(), particle.speed().x(), particle.speed().y(), neighboursIds));
        writer.flush();
    }

    /* TIME VS. VA */
    public static void printTimeVsVaParameters(FileWriter writer, TimeVsVaParameters parameters) {
        try {
            writer.write(String.format(LOCALE, "Etha %.2f\n", parameters.getEtha()));
            writer.write(String.format(LOCALE, "Iterations %d\n", parameters.getIterations()));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTimeVsVaHeader(FileWriter writer) {
        try {
            writer.write("time va\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTimeVsVa(FileWriter writer, int time, double va) throws IOException {
        writer.write(String.format(LOCALE, "%d %.2f\n", time, va));
        writer.flush();
    }

    /* VA VS. ETHA */
    public static void printVaVsEthaParameters(FileWriter writer, VaVsEthaParameters parameters) {
        // TODO: complete this
        try {
            writer.write(String.format(LOCALE, "Etha %.2f\n", parameters.getEtha()));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printVaVsEthaHeader(FileWriter writer) {
        try {
            writer.write("etha mean_va std_va\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printVaVsEtha(FileWriter writer, double etha, double meanVa, double stdVa) throws IOException {
        writer.write(String.format(LOCALE, "%.2f %.2f %.2f\n", etha, meanVa, stdVa));
        writer.flush();
    }

    public static void printVaVsRhoHeader(FileWriter writer) {
        try {
            writer.write("rho mean_va std_va\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printVaVsRho(FileWriter writer, double rho, double meanVa, double stdVa) throws IOException {
        writer.write(String.format(LOCALE, "%.2f %.2f %.2f\n", rho, meanVa, stdVa));
        writer.flush();
    }
}
