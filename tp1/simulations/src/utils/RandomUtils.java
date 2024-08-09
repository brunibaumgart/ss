package utils;

public class RandomUtils {
    public static Double randomDouble() {
        return Math.random();
    }

    public static Double randomDouble(Double max) {
        return Math.random() * max;
    }

    private RandomUtils() {
        throw new IllegalStateException("Utility class");
    }
}
