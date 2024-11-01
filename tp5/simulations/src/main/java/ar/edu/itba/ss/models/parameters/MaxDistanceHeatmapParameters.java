package ar.edu.itba.ss.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaxDistanceHeatmapParameters implements Cloneable {
    private boolean enabled;
    @JsonProperty("runs_per_iteration")
    private int runsPerIteration;
    @JsonProperty("a_min")
    private double aMin;
    @JsonProperty("a_max")
    private double aMax;
    @JsonProperty("a_steps")
    private int aSteps;
    @JsonProperty("b_min")
    private double bMin;
    @JsonProperty("b_max")
    private double bMax;
    @JsonProperty("b_steps")
    private int bSteps;

    @Override
    public MaxDistanceHeatmapParameters clone() {
        try {
            MaxDistanceHeatmapParameters clone = (MaxDistanceHeatmapParameters) super.clone();
            clone.setEnabled(enabled);
            clone.setRunsPerIteration(runsPerIteration);
            clone.setAMax(aMax);
            clone.setAMin(aMin);
            clone.setASteps(aSteps);
            clone.setBMax(bMax);
            clone.setBMin(bMin);
            clone.setBSteps(bSteps);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
