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
public class VaVsEthaParameters {
    private boolean enabled;
    @JsonProperty("stationary_iterations")
    private int stationaryIterations;
    @JsonProperty("total_iterations")
    private int totalIterations;
    private double etha;
    private int steps;
    @JsonProperty("etha_step")
    private Double ethaStep;
}
