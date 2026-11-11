package nqueens;

import nqueens.solver.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.SearchMetricsCsv;
import support.BenchmarkSupport;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("benchmark")
class NQueensBenchmarkTest {

    @Test
    void runBatteriesAndExportCsv() {
        SearchMetricsCsv csv=new SearchMetricsCsv();
        NQueensSolver[] solvers={new NQueensBruteForce(),new NQueensBacktracking(),new NQueensBranchAndBound()};
        for(int n:new int[]{4,5,6,7,8}) for(NQueensSolver solver:solvers) {
            if(n>=8 && solver instanceof NQueensBruteForce){BenchmarkSupport.skipped("n_queens",n, NQueensBruteForce.BRUTE_FORCE);continue;}
            var r=solver.solve(n);
            BenchmarkSupport.row(csv,"n_queens",n,r.algorithm(),r.solutionCount(),Long.toString(r.solutionCount()),r.metrics());
        }
        for(int n:new int[]{9,10,11,12}) for(NQueensSolver solver:new NQueensSolver[]{new NQueensBacktracking(),new NQueensBranchAndBound()}) {
            var r=solver.solve(n); BenchmarkSupport.row(csv,"n_queens",n,r.algorithm(),r.solutionCount(),Long.toString(r.solutionCount()),r.metrics());
        }
        assertEquals(92,new NQueensBacktracking().solve(8).solutionCount());
        BenchmarkSupport.write(csv,"nqueens.csv");
    }
}
