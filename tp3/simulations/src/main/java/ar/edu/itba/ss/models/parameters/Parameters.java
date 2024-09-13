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
public class Parameters {
    private int n;
    private double L;
    private double speed;
    @JsonProperty("mass_p")
    private double massP;
    @JsonProperty("mass_b")
    private double massB;
    private double rp;
    private double rb;
    private double time;
    private boolean movable; // whether the brownian particle moves or not
    private PlotsParameters plots;
    private VideoParameters video;
}
