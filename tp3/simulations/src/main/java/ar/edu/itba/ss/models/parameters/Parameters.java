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
    private double rp;
    private double rb;
    private double time;
    private PlotsParameters plots;
    private VideoParameters video;
}
