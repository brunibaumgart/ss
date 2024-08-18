package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final String PARAMETERS_FILE = "example.json";

    public static void main(String[] args) throws IOException {
        // Get parameters from example.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(PARAMETERS_FILE);
        final FileWriter parametersWriter = new FileWriter(FilePaths.OUTPUT_DIR + "parameters.txt");

        OutputUtils.printParameters(parametersWriter, parameters);
        DefaultRun.run(parameters);
    }
}
