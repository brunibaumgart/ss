package ar.edu.itba.ss.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class SystemParameters {
    private final double k;
    private final double gamma;
    private final double A;
    private final double omega;
}
