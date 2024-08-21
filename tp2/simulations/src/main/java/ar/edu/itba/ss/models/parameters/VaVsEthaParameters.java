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
    @JsonProperty("initial_iterations")
    private int initialIterations;
    private double etha;
    private int steps;
    @JsonProperty("repeat_per_etha")
    private int repeatPerEtha;
}
