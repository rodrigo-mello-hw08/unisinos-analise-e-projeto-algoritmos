import nqueens.solver.NQueensBacktracking;
import nqueens.solver.NQueensBranchAndBound;
import nqueens.solver.NQueensBruteForce;
import nqueens.solver.NQueensSolver;
import reporting.SearchMetricsCsv;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        nqueens(new int[]{4, 5, 6, 7});
    }

    private static void nqueens(int[] sizes) throws IOException {
        List<NQueensSolver> solvers = List.of(
                new NQueensBruteForce(),
                new NQueensBacktracking(),
                new NQueensBranchAndBound()
        );
        SearchMetricsCsv report = new SearchMetricsCsv();

        for (int n : sizes) {
            for (NQueensSolver solver : solvers) {
                var result = solver.solve(n);
                var metrics = result.metrics();
                report.addExecution("n_queens", n, result.algorithm(), result.solutionCount(), metrics);
                System.out.printf(
                        "N=%d %-16s soluções=%d nós=%d tempo=%d ns%n",
                        n,
                        result.algorithm(),
                        result.solutionCount(),
                        metrics.getNodesExplored(),
                        metrics.getElapsedNanos()
                );
            }
        }

        Path output = Path.of("resultados-n-rainhas.csv");
        report.writeTo(output);
        System.out.println("CSV: " + output.toAbsolutePath());
    }
}
