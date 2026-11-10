package nqueens;

import metrics.SearchMetrics;

/** Backtracking com verificação incremental de colunas e diagonais. */
public final class NQueensBacktracking implements NQueensSolver {
    @Override public NQueensResult solve(int n) {
        NQueensSupport.validateSize(n);
        SearchMetrics m = new SearchMetrics(); long start = System.nanoTime();
        long[] count = {0}; search(0, n, new boolean[n], new boolean[2*n-1], new boolean[2*n-1], count, m);
        m.finish(start); return new NQueensResult(n, count[0], m, "backtracking");
    }
    static void search(int row, int n, boolean[] cols, boolean[] down, boolean[] up, long[] count, SearchMetrics m) {
        m.addNode();
        if (row == n) { count[0]++; return; }
        for (int col=0; col<n; col++) {
            m.addAttempt(); int d=row-col+n-1, u=row+col;
            if (cols[col]) { m.pruneColumn(); continue; }
            if (down[d] || up[u]) { m.pruneDiagonal(); continue; }
            cols[col]=down[d]=up[u]=true;
            search(row+1,n,cols,down,up,count,m);
            cols[col]=down[d]=up[u]=false;
        }
    }
}
