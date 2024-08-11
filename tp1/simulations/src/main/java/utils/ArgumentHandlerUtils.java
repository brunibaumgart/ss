package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Parameters;

import java.io.File;
import java.io.IOException;

public class ArgumentHandlerUtils {
    private static final String PATH = "src/main/resources/";

    private ArgumentHandlerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Parameters getParameters(String file) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(new File(PATH + file), Parameters.class);
    }
}
