package nqueens.experiment;

import nqueens.solver.NQueensBacktracking;
import nqueens.solver.NQueensBranchAndBound;
import nqueens.solver.NQueensBruteForce;
import nqueens.solver.NQueensSolver;
import reporting.NQueensMetricsCsv;

import java.util.List;

public final class NQueensExperimentRunner {
    private NQueensExperimentRunner() {}

    public static void run(NQueensMetricsCsv report, int[] sizes) {
        List<NQueensSolver> solvers = List.of(
                new NQueensBruteForce(),
                new NQueensBacktracking(),
                new NQueensBranchAndBound()
        );
        for (int size : sizes) {
            for (NQueensSolver solver : solvers) {
                var result = solver.solve(size);
                var metrics = result.metrics();
                report.addExecution("n_queens", size, result.algorithm(), result.solutionCount(), metrics);
                System.out.printf(
                        "N=%d %-16s soluções=%d nós=%d tempo=%d ns%n",
                        size,
                        result.algorithm(),
                        result.solutionCount(),
                        metrics.getNodesExplored(),
                        metrics.getElapsedNanos()
                );
            }
        }
    }
}
