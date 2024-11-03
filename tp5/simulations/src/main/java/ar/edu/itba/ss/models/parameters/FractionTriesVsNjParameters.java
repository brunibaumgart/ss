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
public class FractionTriesVsNjParameters implements Cloneable {
    private boolean enabled;
    @JsonProperty("nj_initial")
    private int njInitial;
    @JsonProperty("nj_step")
    private int njStep;
    @JsonProperty("nj_total_steps")
    private int njTotalSteps;

    private int attempts;

    private int runs;

    @Override
    public FractionTriesVsNjParameters clone() {
        try {
            final FractionTriesVsNjParameters clone = (FractionTriesVsNjParameters) super.clone();
            clone.setEnabled(enabled);
            clone.setNjInitial(njInitial);
            clone.setNjStep(njStep);
            clone.setNjTotalSteps(njTotalSteps);
            clone.setAttempts(attempts);
            clone.setRuns(runs);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
