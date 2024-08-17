package ar.edu.itba.ss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parameters implements Cloneable {
    private double L;
    private double rc;
    private double r;
    private int M;
    private int N;
    private double v;
    private int dt;
    private double etha;
    private int iterations;

    @Override
    public Parameters clone() {
        try {
            return (Parameters) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Shouldn't happen because we're Cloneable
        }
    }

    // Custom setter for 'M' to include validation logic
    public void setM(int m) {
        if (this.L / m < this.rc + this.r)
            throw new IllegalArgumentException("The cutoff radius + radius of particles must be smaller than L/M, M was " + m);

        this.M = m;
    }
}
