package nqueens.solver;

import metrics.SearchMetrics;
import nqueens.domain.NQueensResult;

/** Backtracking com verificação incremental de colunas e diagonais. */
public final class NQueensBacktracking implements NQueensSolver {

    public static final String BACKTRACKING = "backtracking";

    @Override
    public NQueensResult solve(int n) {
        NQueensSupport.validateSize(n);
        SearchMetrics m = new SearchMetrics();
        long start = System.nanoTime();
        long[] count = {0};
        search(0, n, new boolean[n], new boolean[2 * n - 1], new boolean[2 * n - 1], count, m);
        m.finish(start);
        return new NQueensResult(n, count[0], m, BACKTRACKING);
    }

    static void search(int row, int n, boolean[] cols, boolean[] down, boolean[] up, long[] count, SearchMetrics m) {
        m.addNode();
        if (row == n) {
            count[0]++;
            return;
        }

        for (int col = 0; col < n; col++) {
            m.addAttempt();
            int d = row - col + n - 1;
            int u = row + col;
            if (cols[col]) {
                m.pruneColumn();
                continue;
            }

            boolean diagonalOccupied = down[d] || up[u];
            if (diagonalOccupied) {
                m.pruneDiagonal();
                continue;
            }

            cols[col] = true;
            down[d] = true;
            up[u] = true;
            search(row + 1, n, cols, down, up, count, m);
            cols[col] = false;
            down[d] = false;
            up[u] = false;
        }
    }
}
