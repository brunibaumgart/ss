import models.Parameters;
import utils.ArgumentHandlerUtils;

import java.io.IOException;

public class Main {
    private static final String PARAMETERS_FILE = "example.json";

    public static void main(String[] args) throws IOException {
        // Get parameters from example.json
        final Parameters parameters = ArgumentHandlerUtils.getParameters(PARAMETERS_FILE);

        if(parameters.getRuns() == 1) {
            DefaultRun.run(parameters);
        } else {
            MultipleRuns.run(parameters);
        }
    }
}
