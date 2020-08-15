import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {

    private int size;
    private int[] blocks;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("Illegal argument");
        }
        blocks = new int[tiles.length * tiles.length];
        size = tiles.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocks[i * size + j] = tiles[i][j];
            }
        }

    }

    // string representation of this board
    public String toString() {
        String s = size + "\n";
        for (int i = 0; i < blocks.length; i++) {
            if (i % size == 0 && i != 0) {
                s += "\n " + blocks[i];
            } else s += " " + blocks[i];
        }
        return s;
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != 0 && blocks[i] != i + 1) {
                distance++;
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != 0 && blocks[i] != i + 1) {
                distance += Math.abs(((blocks[i] - 1) % size) - (i % size))
                        + Math.abs(((blocks[i] - 1) / size) - (i / size));
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (blocks[blocks.length - 1] == 0) {
            for (int i = 0; i < blocks.length - 1; i++) {
                if (blocks[i] != i + 1) {
                    return false;
                }
            }
            return true;
        } else return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.equals(this.blocks, that.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        Stack<Board> neighbors = new Stack<Board>();
        int blankPos = 0;
        int i = 0;
        while (blocks[i] != 0)
            i++;

        blankPos = i;
        int[] blocksCopy;
        // blankPos is not on the last column
        if (blankPos / size == (blankPos + 1) / size) {
            blocksCopy = blocks.clone();
            blocksCopy[blankPos] = blocksCopy[blankPos + 1];
            blocksCopy[blankPos + 1] = 0;
            neighbors.push(new Board(toTwoDArray(blocksCopy)));
        }
        // blankPos is not on the first column
        if (blankPos / size == (blankPos - 1) / size && blankPos != 0) {
            blocksCopy = blocks.clone();
            blocksCopy[blankPos] = blocksCopy[blankPos - 1];
            blocksCopy[blankPos - 1] = 0;
            neighbors.push(new Board(toTwoDArray(blocksCopy)));
        }
        // blankPos is not on the first row
        if (blankPos - size >= 0) {
            blocksCopy = blocks.clone();
            blocksCopy[blankPos] = blocksCopy[blankPos - size];
            blocksCopy[blankPos - size] = 0;
            neighbors.push(new Board(toTwoDArray(blocksCopy)));
        }
        // blankPos is not on the last row
        if (blankPos + size < blocks.length) {
            blocksCopy = blocks.clone();
            blocksCopy[blankPos] = blocksCopy[blankPos + size];
            blocksCopy[blankPos + size] = 0;
            neighbors.push(new Board(toTwoDArray(blocksCopy)));
        }


        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] blocksCopy = toTwoDArray(blocks);
        int temp = 0;
        if (blocksCopy[0][0] != 0 && blocksCopy[0][1] != 0) {
            temp = blocksCopy[0][1];
            blocksCopy[0][1] = blocksCopy[0][0];
            blocksCopy[0][0] = temp;
        } else {
            temp = blocksCopy[1][1];
            blocksCopy[1][1] = blocksCopy[1][0];
            blocksCopy[1][0] = temp;
        }
        return new Board(blocksCopy);
//        int swapElem1 = StdRandom.uniform(0, blocks.length);
//        int swapElem2 = StdRandom.uniform(0, blocks.length);
//        while (blocksCopy[swapElem1] == 0 || swapElem1 == swapElem2) {
//            swapElem1 = StdRandom.uniform(0, blocks.length);
//        }
//        while (blocksCopy[swapElem2] == 0 || swapElem1 == swapElem2) {
//            swapElem2 = StdRandom.uniform(0, blocks.length);
//        }

    }

    private int[][] toTwoDArray(int[] arr) {
        int n = 0;
        int[][] twoD = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                twoD[i][j] = arr[n];
                n++;
            }
        }
        return twoD;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{2, 4, 1},
                {3, 8, 5},
                {6, 7, 0}};
        int[][] tiles2 = {{0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}};

        Board b = new Board(tiles);
        Board b2 = new Board(tiles2);
        System.out.println(b.toString());
        System.out.println(b.dimension());
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.isGoal());
        System.out.println(b.equals(b2));
        System.out.println(b.neighbors());
        System.out.println(b2.neighbors());
        System.out.println(b.twin().toString());

    }

}
