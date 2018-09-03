/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int OPEN = 1;
    private final int CLOSE = 0;
    // private final WeightedQuickUnionUF qu;
    private WeightedQuickUnionUF qu;
    private final int gridSize;
    private int[][] grid;
    private boolean doesPercolate = false;

    public Percolation(int n) {
        gridSize = n;
        qu = configureUnionFind(gridSize);

        int allocatedSize = gridSize + 1;
        grid = new int[allocatedSize][allocatedSize];
    }

    private WeightedQuickUnionUF configureUnionFind(int size) {
        // Number of rows is size + 1 row for top virtual zone
        //  and 1 row for bottom virtual zone
        int rows = size + 2;
        int cols = size;
        WeightedQuickUnionUF qf = new WeightedQuickUnionUF(rows * cols);

        // The sites in the top virtual zone need to be connected
        int firstSite = rowColTo1D(0, 1);
        for (int i = 2; i <= gridSize ; i++) {
            qf.union(firstSite, rowColTo1D(0, i));
        }

        // The sites in the bottom virtual zone need to be connected
        firstSite = rowColTo1D(gridSize + 1, 1);
        for (int i = 2; i <= gridSize ; i++) {
            qf.union(firstSite, rowColTo1D(gridSize + 1, i));
        }
        return qf;
    }

    public void open(int row, int col) {
        grid[row][col] = OPEN;
        linkWithOpenNeighbors(row, col);

    }

    private void linkWithOpenNeighbors(int row, int col) {
        int from_id = rowColTo1D(row, col);
        linkWithOpenNeighbor(from_id, row - 1, col);
        linkWithOpenNeighbor(from_id, row + 1, col);
        linkWithOpenNeighbor(from_id, row, col - 1);
        linkWithOpenNeighbor(from_id, row, col + 1);
    }

    private void linkWithOpenNeighbor(int fromSite, int row, int col) {
        if (isValidCoordinats(row, col) && isOpen(row, col )) qu.union(fromSite, rowColTo1D(row, col));
    }

    private boolean isValidCoordinats(int row, int col){
        return (row >= 0 && row <= gridSize + 1 && col > 0 && col <= gridSize);
    }

    public boolean isOpen(int row, int col) {
        if (row == 0 || row == gridSize + 1)
            return true;
        else
            return grid[row][col] == OPEN;
    }

    public boolean isFull(int row, int col) {
        int targetIndex = rowColTo1D(row, col);
        int memberOfTopVirtualZoneIndex = rowColTo1D(0, 1);
        boolean connected = qu.connected(memberOfTopVirtualZoneIndex, targetIndex);

        return connected;
    }

    public int numberOfOpenSites() {
        return 2;
    }

    public boolean percolates() {
        if (doesPercolate == false) {
            int topVirtualRowIndex = rowColTo1D(0, 1);
            int bottomvirtualRowIndex = rowColTo1D(gridSize + 1, 1);
            doesPercolate = qu.connected(topVirtualRowIndex, bottomvirtualRowIndex);
        }
        return doesPercolate;
    }

    private int rowColTo1D(int row, int col) {
        return gridSize * row + col - 1;
    }

    public void printInternals() {
        System.out.print("qf:[");
        for (int i = 0; i < (gridSize +2) * gridSize; i++) {
            System.out.print(qu.find(i) + ",");
        }
        System.out.println("]");
    }

}
