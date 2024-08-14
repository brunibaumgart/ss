package methods;

import models.Cell;
import models.Particle;

import java.util.*;

public class CellIndexMethod implements IMethod {
    private final boolean isPeriodic;
    private final Double L;
    private final Cell[][] grid;

    /**
     * @param M         number of cells in the grid
     * @param L         length of the box
     * @param particles list of particles
     */
    public CellIndexMethod(final Integer M,
                           final Double L,
                           final boolean isPeriodic,
                           final List<Particle> particles
    ) {
        this.L = L;
        this.isPeriodic = isPeriodic;
        this.grid = new Cell[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                this.grid[i][j] = new Cell(i, j);
            }
        }

        for (Particle particle : particles) {
            final int x = (int) (particle.position().x() / (L / M));
            final int y = (int) (particle.position().y() / (L / M));
            this.grid[x][y].addParticle(particle);
        }
    }

    public Map<Particle, List<Particle>> calculateNeighbors(final Double rc) {
        final Map<Particle, List<Particle>> neighbors = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                final List<Cell> neighbouringCells = getNeighbouringCells(i, j);

                for (Particle particle : grid[i][j].getParticles()) {
                    for (Cell cell : neighbouringCells) {
                        final List<Particle> neighbouringParticles = getNeighbouringParticles(particle, cell, rc);
                        for (Particle neighbourParticle : neighbouringParticles) {
                            if (neighbors.containsKey(particle) && neighbors.get(particle).contains(neighbourParticle)) {
                                continue;
                            }

                            neighbors.computeIfAbsent(particle, k -> new ArrayList<>()).add(neighbourParticle);
                            neighbors.computeIfAbsent(neighbourParticle, k -> new ArrayList<>()).add(particle);
                        }
                    }
                }
            }
        }

        return neighbors;
    }

    private List<Cell> getNeighbouringCells(int i, int j) {
        // the grid is square so we can use grid.length because it's the same as grid[i].length
        final int top = (i + 1) % grid.length;
        final int right = (j + 1) % grid.length;
        final int bottom = (i - 1 + grid.length) % grid.length;

        final Cell thisCell = grid[i][j];
        final Cell topCell = grid[top][j];
        final Cell diagonalTopRightCell = grid[top][right];
        final Cell rightCell = grid[i][right];
        final Cell diagonalBottomRightCell = grid[bottom][right];

        final List<Cell> result = new ArrayList<>();
        result.add(thisCell);
        result.add(topCell);
        result.add(diagonalTopRightCell);
        result.add(rightCell);
        result.add(diagonalBottomRightCell);

        return Collections.unmodifiableList(result);
    }

    private List<Particle> getNeighbouringParticles(Particle particle, Cell cell, Double rc) {
        final List<Particle> result = new ArrayList<>();
        for (Particle p : cell.getParticles()) {
            if (p.id() != particle.id() && isNeighbour(particle, p, rc)) {
                result.add(p);
            }
        }

        return Collections.unmodifiableList(result);
    }

    private boolean isNeighbour(Particle particle, Particle neighbourParticle, Double rc) {
        if (isPeriodic)
            return particle.periodicDistanceTo(neighbourParticle, this.L) <= rc - particle.radius();

        return particle.distanceTo(neighbourParticle) <= rc - particle.radius();
    }

    public Cell[][] getGrid() {
        return grid;
    }
}


