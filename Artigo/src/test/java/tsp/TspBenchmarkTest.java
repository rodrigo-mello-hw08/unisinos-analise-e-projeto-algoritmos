package tsp;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.SearchMetricsCsv;
import support.BenchmarkSupport;
import tsp.solver.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("benchmark")
class TspBenchmarkTest {

    @Test
    void runBatteriesAndExportCsv() {
        SearchMetricsCsv csv=new SearchMetricsCsv();
        TspSolver[] solvers={new TspBruteForce(),new TspBacktracking(),new TspBranchAndBound()};
        for(int n:new int[]{2,4,6,8,10}) {
            var instance=BenchmarkSupport.instance(n); long expected=-1;
            for(TspSolver solver:solvers) {
                if(n==10 && solver instanceof TspBruteForce){BenchmarkSupport.skipped("tsp",n,TspBruteForce.ALGORITHM);continue;}
                var r=solver.solve(instance); if(expected<0)expected=r.optimalCost(); assertEquals(expected,r.optimalCost());
                BenchmarkSupport.row(csv,"tsp",n,r.algorithm(),0,Long.toString(r.optimalCost()),r.metrics());
            }
        }
        BenchmarkSupport.write(csv,"tsp.csv");
    }
}
