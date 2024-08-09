package models;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Particle> particles;

    public Cell(List<Particle> particles) {
        this.particles = new ArrayList<>();
    }
}
