package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.parameters.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ArgumentHandlerUtils {
    private ArgumentHandlerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Parameters getParameters(String file) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(new File(FilePaths.INPUT_DIR + file), Parameters.class);
    }
}