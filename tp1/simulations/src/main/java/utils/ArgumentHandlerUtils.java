package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.FilePaths;
import models.Parameters;

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
