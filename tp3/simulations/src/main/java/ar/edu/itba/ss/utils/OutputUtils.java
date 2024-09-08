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

    public static void printVideoFrameHeader(final FileWriter writer) {
        try {
            writer.write("id x y vx vy\n");
            writer.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void printVideoFrame(final FileWriter writer, final List<Particle> particles) {
        particles.stream()
                .map(p -> String.format(LOCALE, "%d %.2f %.2f %.2f %.2f\n", p.id(), p.position().x(), p.position().y(), p.speed().x(), p.speed().y()))
                .forEach(s -> {
                    try {
                        writer.write(s);
                        writer.flush();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
