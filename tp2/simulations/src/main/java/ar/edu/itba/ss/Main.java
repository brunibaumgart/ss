package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;

import java.io.IOException;

public class Main {
    private static final String PARAMETERS_FILE = "example.json";

    public static void main(String[] args) throws IOException {
        // Get parameters from example.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(PARAMETERS_FILE);

        DefaultRun.run(parameters);

    }
}
