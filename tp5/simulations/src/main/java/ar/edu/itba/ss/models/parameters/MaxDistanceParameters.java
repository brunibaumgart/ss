package ar.edu.itba.ss.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaxDistanceParameters implements Cloneable {
    private boolean enabled;
    private int runs;

    @Override
    public MaxDistanceParameters clone() {
        try {
            MaxDistanceParameters clone = (MaxDistanceParameters) super.clone();
            clone.setEnabled(enabled);
            clone.setRuns(runs);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
