package nqueens.solver;

import metrics.SearchMetrics;
import nqueens.domain.NQueensResult;

/** Enumera N^N tabuleiros e valida apenas as configurações completas. */
public final class NQueensBruteForce implements NQueensSolver {

    public static final String BRUTE_FORCE = "brute_force";

    @Override
    public NQueensResult solve(int n) {
        NQueensSupport.validateSize(n);
        SearchMetrics metrics = new SearchMetrics();
        long start = System.nanoTime();
        long[] count = {0};
        int[] queens = new int[n];
        enumerate(0, n, queens, count, metrics);
        metrics.finish(start);
        return new NQueensResult(n, count[0], metrics, BRUTE_FORCE);
    }

    private void enumerate(int row, int n, int[] queens, long[] count, SearchMetrics m) {
        m.addNode();
        if (row == n) {
            boolean validConfiguration = NQueensSupport.isValid(queens);
            if (validConfiguration) {
                count[0]++;
            } else {
                m.invalidComplete();
            }
            return;
        }
        for (int col = 0; col < n; col++) {
            m.addAttempt();
            queens[row] = col;
            enumerate(row + 1, n, queens, count, m);
        }
    }
}
