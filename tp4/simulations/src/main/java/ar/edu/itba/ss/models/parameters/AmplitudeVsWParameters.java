package ar.edu.itba.ss.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AmplitudeVsWParameters {
    private boolean enabled;
    @JsonProperty("initial_w")
    private double initialW;
    @JsonProperty("w_step")
    private double wStep;
    private double steps;
}
