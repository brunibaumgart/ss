package methods;

import models.Cell;
import models.particle.IdentifiableParticle;
import models.particle.Particle;

import java.util.List;
import java.util.Map;

public class CellIndexMethod {
    private final Cell[][] grid;

    /**
     * @param M number of cells in the grid
     * @param L length of the box
     * @param particles list of particles
     */
    public CellIndexMethod(final Integer M,
                           final Double L,
                           final List<IdentifiableParticle> particles
    ) {
        this.grid = new Cell[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                this.grid[i][j] = new Cell();
            }
        }

        for (IdentifiableParticle particle : particles) {
            final int x = (int) (particle.getPosition().getX() / (L / M));
            final int y = (int) (particle.getPosition().getY() / (L / M));
            this.grid[x][y].addParticle(particle);
        }
    }

    public Map<Integer, List<Integer>> calculateNeighbors(){
        return null;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}


