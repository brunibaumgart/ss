package ar.edu.itba.ss.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parameters implements Cloneable{
    private PlotsParameters plots;
    private double k;
    private double m;
    private double A;
    private int N;
    private double time;
}
