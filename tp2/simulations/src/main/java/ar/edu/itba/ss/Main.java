package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final String CONFIG_FILE = "example.json";

    public static void main(String[] args) throws IOException {
        // Get parameters from example.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);
        final FileWriter parametersWriter = new FileWriter(FilePaths.PARAMETERS_FILE);

        OutputUtils.printDefaultParameters(parametersWriter, parameters);
        OutputUtils.printCimParameters(parametersWriter, parameters.getCim());

        if (parameters.getPlots().getVideo().isEnabled()) {

            DefaultRun.run(parameters);
        }

        if (parameters.getPlots().getTimeVsVa().isEnabled()) {
            TimeVsVaRun.run(parameters);
        }
    }
}
