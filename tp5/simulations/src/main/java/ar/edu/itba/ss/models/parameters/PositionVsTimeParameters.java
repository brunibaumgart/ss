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
public class PositionVsTimeParameters implements Cloneable {
    private boolean enabled;
    @JsonProperty("total_time")
    private double totalTime; // s
    @JsonProperty("seed")
    private long seed;

    @Override
    public PositionVsTimeParameters clone() {
        try {
            final PositionVsTimeParameters clone = (PositionVsTimeParameters) super.clone();
            clone.setEnabled(enabled);
            clone.setTotalTime(totalTime);
            clone.setSeed(seed);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
