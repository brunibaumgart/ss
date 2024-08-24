package ar.edu.itba.ss.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaVsRhoParameters {
    private boolean enabled;
    @JsonProperty("stationary_iterations")
    private int stationaryIterations;
    @JsonProperty("total_iterations")
    private int totalIterations;
    @JsonProperty("initial_n")
    private int initialN;
    private int steps;
    @JsonProperty("n_step")
    private int nStep;
    private double etha;
}
