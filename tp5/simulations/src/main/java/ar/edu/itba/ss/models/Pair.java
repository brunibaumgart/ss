package ar.edu.itba.ss.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class Pair<T1, T2> {
    private final T1 first;
    private final T2 second;
}
