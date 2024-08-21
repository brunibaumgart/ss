package ar.edu.itba.ss.models.parameters;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoParameters {
    private boolean enabled;
    private int iterations;
    private double etha;
}
