package com.swrobotics.pathfinding.library.grid;

public final class GridUnion implements Grid {
    private final int width;
    private final int height;
    private final Grid[] grids;

    public GridUnion(Grid first, Grid... rest) {
        int width = first.getWidth();
        int height = first.getHeight();
        for (Grid grid : rest) {
            if (width != grid.getWidth() || height != grid.getHeight()) {
                throw new IllegalArgumentException("A grid union can only be made from grids of the same size");
            }
        }

        this.grids = new Grid[rest.length + 1];
        this.grids[0] = first;
        System.arraycopy(rest, 0, grids, 1, rest.length);

        this.width = width;
        this.height = height;
    }

    @Override
    public boolean get(int x, int y) {
        for (Grid grid : grids) {
            if (grid.get(x, y)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
