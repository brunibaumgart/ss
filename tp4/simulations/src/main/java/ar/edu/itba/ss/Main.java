package ar.edu.itba.ss;

import ar.edu.itba.ss.constants.FilePaths;
import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;
import ar.edu.itba.ss.utils.OutputUtils;

import java.io.FileWriter;
import java.io.IOException;
/**
 * Hello world!
 *
 */
public class Main {
    private static final String CONFIG_FILE = "config.json";

    public static void main(String[] args) throws IOException {
        // Get parameters from example.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);
        final FileWriter parametersWriter = new FileWriter(FilePaths.PARAMETERS_FILE);

        if (parameters.getPlots().getSystemOne().isEnabled()) {
            Exercise1.run(parameters);
        }
        else {
            if (parameters.getPlots().getFrecuencyVsK().isEnabled()){
                //FrecuencyVsK.run(parameters);
            }
            if (parameters.getPlots().getFrecuencyVsK().isEnabled()){
                //FrecuencyVsK.run(parameters);
            }
            if (parameters.getPlots().getW0VsK().isEnabled()){
                //W0VsK.run(parameters);
            }
        }
    }
}
