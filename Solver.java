import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> pqTwin;
    private Stack<Board> solution;
    private boolean solvable = false;

    private static class SearchNode implements Comparable<SearchNode> {
        private Board b;
        private int numMoves;
        private SearchNode prev;
        private final int priority;

        public SearchNode(Board bd, int n, SearchNode previous) {
            b = bd;
            numMoves = n;
            prev = previous;
            priority = numMoves + bd.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.priority - o.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null argument");
        }
        pq = new MinPQ<SearchNode>();
        pqTwin = new MinPQ<SearchNode>();
        SearchNode root = new SearchNode(initial, 0, null);
        SearchNode rootTwin = new SearchNode(initial.twin(), 0, null);
        SearchNode min;
        SearchNode minTwin;
        solution = new Stack<Board>();
        pq.insert(root);
        pqTwin.insert(rootTwin);
        while (!pq.min().b.isGoal() && !pqTwin.min().b.isGoal()) {
            min = pq.delMin();
            minTwin = pqTwin.delMin();

            for (Board neighbor : min.b.neighbors()) {
                if (min.prev == null || !neighbor.equals(min.prev.b)) {
                    pq.insert(new SearchNode(neighbor, min.numMoves + 1, min));
                }
            }
            for (Board neighbor : minTwin.b.neighbors()) {
                if (minTwin.prev == null || !neighbor.equals(minTwin.prev.b)) {
                    pqTwin.insert(new SearchNode(neighbor, minTwin.numMoves + 1, minTwin));
                }
            }
        }

        SearchNode current = pq.min();

        if (current.b.isGoal()) {
            solvable = true;
        }

        while (current.prev != null) {
            solution.push(current.b);
            current = current.prev;
        }
        //solution.push(initial);
        solution.push(current.b);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        } else return pq.min().numMoves;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable())
            return solution;
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);

        }
    }
}
