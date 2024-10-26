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
public class PositionVsTimeParameters
{
    private boolean enabled;
    @JsonProperty("delta_t")
    private double deltaT; // s
    @JsonProperty("total_time")
    private double totalTime; // s
    @JsonProperty("seed")
    private long seed;
}
