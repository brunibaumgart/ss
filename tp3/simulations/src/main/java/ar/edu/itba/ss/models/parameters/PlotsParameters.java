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
public class PlotsParameters {
    @JsonProperty("pressure_vs_time")
    private PressureVsTimeParameters pressureVsTime;
    @JsonProperty("collisions_with_obstacle")
    private CollisionsWIthObstacleParameters collisionsWithObstacle;
    private MSDParameters msd;
    @JsonProperty("pressure_vs_temperature")
    private PressureVsTemperatureParameters pressureVsTemperature;
}
