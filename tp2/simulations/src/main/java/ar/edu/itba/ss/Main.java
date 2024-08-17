package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;

import java.io.IOException;

public class Main {
    private static final String PARAMETERS_FILE = "example.json";

    public static void main(String[] args) throws IOException {
        // Get parameters from example.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(PARAMETERS_FILE);

        if (parameters.getRuns() == 1) {
            DefaultRun.run(parameters);
        } else {
            if (parameters.isPlotTimeVsM()) {
                final Parameters cim = parameters.clone();
                parameters.setMethod("CIM");
                MultipleRunsTimevsM.run(cim, "cim_times_vs_m.txt");
            }

            if (parameters.isPlotTimeVsN()) {
                final Parameters bfm = parameters.clone();
                bfm.setMethod("BFM");
                MultipleRunsTimevsN.run(bfm, "bfm_times_vs_n.txt");

                final Parameters cim = parameters.clone();
                parameters.setMethod("CIM");
                MultipleRunsTimevsN.run(cim, "cim_times_vs_n.txt");
            }
        }
    }
}
