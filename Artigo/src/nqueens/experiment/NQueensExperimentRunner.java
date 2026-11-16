package nqueens.experiment;

import experiment.ExperimentRunner;
import metrics.SearchMetrics;
import nqueens.domain.NQueensResult;
import nqueens.solver.NQueensBacktracking;
import nqueens.solver.NQueensBranchAndBound;
import nqueens.solver.NQueensBruteForce;
import nqueens.solver.NQueensSolver;
import reporting.NQueensMetricsCsv;

import java.util.Arrays;
import java.util.List;

public final class NQueensExperimentRunner implements ExperimentRunner<NQueensSolver, Integer> {

    @Override
    public long warmup(NQueensSolver solver, Integer size) {
        long startNanos = System.nanoTime();
        for (int warmup = 0; warmup < WARMUP_RUNS; warmup++) {
            solver.solve(size);
        }
        return System.nanoTime() - startNanos;
    }

    public void run(NQueensMetricsCsv report, int[] sizes) {
        List<NQueensSolver> solvers = getSolvers();
        for (int size : sizes) {
            for (NQueensSolver solver : solvers) {
                long warmupElapsedNanos = warmup(solver, size);
                long[] elapsedNanos = new long[MEASURED_RUNS];
                var result = solver.solve(size);
                elapsedNanos[0] = result.metrics().getElapsedNanos();
                for (int repetition = 1; repetition < MEASURED_RUNS; repetition++) {
                    var repeatedResult = solver.solve(size);
                    validateIllegalState(repeatedResult, result);
                    elapsedNanos[repetition] = repeatedResult.metrics().getElapsedNanos();
                }
                Arrays.sort(elapsedNanos);
                long medianElapsedNanos = elapsedNanos[MEASURED_RUNS / 2];
                var metrics = result.metrics();
                report.addExecution("n_queens", size, result.algorithm(), result.solutionCount(), metrics,
                    medianElapsedNanos, warmupElapsedNanos);
                printNQueens(size, result, metrics, medianElapsedNanos);
            }
        }
    }

    private static void printNQueens(int size, NQueensResult result, SearchMetrics metrics, long medianElapsedNanos) {
        System.out.printf(
            "N=%d %-16s soluções=%d nós=%d tempo=%d ns%n",
            size,
            result.algorithm(),
            result.solutionCount(),
            metrics.getNodesExplored(),
            medianElapsedNanos
        );
    }

    private static void validateIllegalState(NQueensResult repeatedResult, NQueensResult result) {
        if (repeatedResult.solutionCount() != result.solutionCount()) {
            throw new IllegalStateException("As execuções de N-Queens encontraram quantidades de soluções diferentes.");
        }
    }

    private static List<NQueensSolver> getSolvers() {
        return List.of(
            new NQueensBruteForce(),
            new NQueensBacktracking(),
            new NQueensBranchAndBound()
        );
    }
}
