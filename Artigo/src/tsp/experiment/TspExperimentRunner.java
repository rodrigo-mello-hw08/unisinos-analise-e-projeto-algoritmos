package tsp.experiment;

import reporting.TspMetricsCsv;
import tsp.domain.TspInstance;
import tsp.domain.TspInstanceGenerator;
import tsp.domain.TspResult;
import tsp.solver.TspBacktracking;
import tsp.solver.TspBranchAndBound;
import tsp.solver.TspBruteForce;
import tsp.solver.TspSolver;

import java.util.List;

public final class TspExperimentRunner {
    private TspExperimentRunner() {}

    public static void run(TspMetricsCsv report, int[] cityCounts) {
        List<TspSolver> solvers = List.of(
                new TspBruteForce(),
                new TspBacktracking(),
                new TspBranchAndBound()
        );
        for (int cityCount : cityCounts) {
            TspInstance instance = TspInstanceGenerator.generate(cityCount);
            long expectedCost = -1;
            for (TspSolver solver : solvers) {
                TspResult result = solver.solve(instance);
                if (expectedCost == -1) {
                    expectedCost = result.optimalCost();
                } else if (result.optimalCost() != expectedCost) {
                    throw new IllegalStateException("Os algoritmos TSP encontraram custos ótimos diferentes.");
                }
                validateRoute(result.route(), cityCount);
                var metrics = result.metrics();
                report.addExecution("tsp", cityCount, result.algorithm(), result.optimalCost(), metrics);
                System.out.printf(
                        "TSP cidades=%d %-16s custo=%d rota=%s nós=%d tempo=%d ns%n",
                        cityCount,
                        result.algorithm(),
                        result.optimalCost(),
                        java.util.Arrays.toString(result.route()),
                        metrics.getNodesExplored(),
                        metrics.getElapsedNanos()
                );
            }
        }
    }

    private static void validateRoute(int[] route, int cityCount) {
        boolean routeDoesNotClose = route.length != cityCount + 1
                || route[0] != 0
                || route[cityCount] != 0;
        if (routeDoesNotClose) {
            throw new IllegalStateException("A rota TSP deve iniciar e terminar na cidade de origem.");
        }
        boolean[] visited = new boolean[cityCount];
        for (int index = 0; index < cityCount; index++) {
            int city = route[index];
            boolean invalidCity = city < 0 || city >= cityCount || visited[city];
            if (invalidCity) {
                throw new IllegalStateException("A rota TSP deve visitar cada cidade exatamente uma vez.");
            }
            visited[city] = true;
        }
    }
}
