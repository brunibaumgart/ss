package ar.edu.itba.ss.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CimParameters {
    private double L;
    private double rc;
    private double r;
    private int M;
    private int N;

    // Custom setter for 'M' to include validation logic
    public void setM(int m) {
        if (this.L / m < this.rc + this.r)
            throw new IllegalArgumentException("The cutoff radius + radius of particles must be smaller than L/M, M was " + m);

        this.M = m;
    }
}
