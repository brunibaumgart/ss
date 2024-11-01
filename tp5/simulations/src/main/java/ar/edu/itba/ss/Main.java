package ar.edu.itba.ss;

import ar.edu.itba.ss.models.parameters.Parameters;
import ar.edu.itba.ss.utils.ArgumentHandlerUtils;

public class Main {
    private static final String CONFIG_FILE = "config.json";

    public static void main( String[] args ) throws Exception {
        final Parameters parameters = ArgumentHandlerUtils.getParameters(CONFIG_FILE);

        if(parameters.getPlots().getPositionVsTime().isEnabled()) {
            PositionVsTime.run(parameters);
        }

        if(parameters.getPlots().getMaxDistance().isEnabled()) {
            MaxDistance.run(parameters);
        }
    }
}
