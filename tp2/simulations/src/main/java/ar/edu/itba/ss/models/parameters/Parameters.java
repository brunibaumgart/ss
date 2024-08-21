package ar.edu.itba.ss.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parameters implements Cloneable {
    private CimParameters cim;
    private PlotsParameters plots;
    private double speed;
    private int dt;

    @Override
    public Parameters clone() {
        try {
            return (Parameters) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Shouldn't happen because we're Cloneable
        }
    }
}
