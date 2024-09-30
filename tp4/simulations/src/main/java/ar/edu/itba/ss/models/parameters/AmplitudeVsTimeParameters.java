package ar.edu.itba.ss.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AmplitudeVsTimeParameters {
    private boolean enabled;
    private double w;
}
