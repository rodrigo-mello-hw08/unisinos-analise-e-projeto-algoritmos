import nqueens.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        int[] sizes = args.length == 0 ? new int[]{4,5,6,7} : java.util.Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
        List<NQueensSolver> solvers = List.of(new NQueensBruteForce(), new NQueensBacktracking(), new NQueensBranchAndBound());
        StringBuilder csv = new StringBuilder("problem,n,algorithm,elapsed_nanos,nodes_explored,placement_attempts,pruned_by_column,pruned_by_diagonal,pruned_by_bound,invalid_complete_configurations,bound_evaluations,solution_count\n");
        for (int n : sizes) for (NQueensSolver solver : solvers) {
            NQueensResult r = solver.solve(n); var m = r.metrics();
            csv.append("n_queens,").append(n).append(',').append(r.algorithm()).append(',')
                    .append(m.getElapsedNanos()).append(',').append(m.getNodesExplored()).append(',')
                    .append(m.getPlacementAttempts()).append(',').append(m.getPrunedByColumn()).append(',')
                    .append(m.getPrunedByDiagonal()).append(',').append(m.getPrunedByBound()).append(',')
                    .append(m.getInvalidCompleteConfigurations()).append(',').append(m.getBoundEvaluations()).append(',')
                    .append(r.solutionCount()).append('\n');
            System.out.printf("N=%d %-16s soluções=%d nós=%d tempo=%d ns%n", n, r.algorithm(), r.solutionCount(), m.getNodesExplored(), m.getElapsedNanos());
        }
        Path output = Path.of("resultados-n-rainhas.csv");
        Files.writeString(output, csv);
        System.out.println("CSV: " + output.toAbsolutePath());
    }
}
