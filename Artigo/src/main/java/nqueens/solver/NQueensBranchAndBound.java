package nqueens.solver;

import metrics.SearchMetrics;
import nqueens.domain.NQueensResult;

/** Backtracking com limite superior otimista de linhas ainda posicionáveis. */
public final class NQueensBranchAndBound implements NQueensSolver {

    public static final String BRANCH_AND_BOUND = "branch_and_bound";

    @Override
    public NQueensResult solve(int n) {
        NQueensSupport.validateSize(n);
        SearchMetrics m = new SearchMetrics();
        long start = System.nanoTime();
        long[] count = {0};
        search(0, n, new boolean[n], new boolean[2 * n - 1], new boolean[2 * n - 1], count, m);
        m.finish(start);
        return new NQueensResult(n, count[0], m, BRANCH_AND_BOUND);
    }

    private void search(int row, int n, boolean[] cols, boolean[] down, boolean[] up, long[] count, SearchMetrics m) {
        m.addNode();
        if (row == n) {
            count[0]++;
            return;
        }
        m.evaluateBound();
        int possibleRows = 0;

        for (int future = row; future < n; future++) {
            boolean hasPosition = false;
            for (int col = 0; col < n; col++) {
                if (hasPosition) {
                    break;
                }
                int downDiagonal = future - col + n - 1;
                int upDiagonal = future + col;
                boolean columnAvailable = !cols[col];
                boolean downDiagonalAvailable = !down[downDiagonal];
                boolean upDiagonalAvailable = !up[upDiagonal];
                boolean positionAvailable = columnAvailable && downDiagonalAvailable && upDiagonalAvailable;
                if (positionAvailable) {
                    hasPosition = true;
                }
            }

            if (hasPosition) {
                possibleRows++;
            }
        }
        boolean boundIsInsufficient = row + possibleRows < n;
        if (boundIsInsufficient) {
            m.pruneBound();
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
