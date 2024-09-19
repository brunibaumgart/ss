package ar.edu.itba.ss.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PressureVsTemperatureParameters {
    private boolean enabled;
    private double dt;
}
