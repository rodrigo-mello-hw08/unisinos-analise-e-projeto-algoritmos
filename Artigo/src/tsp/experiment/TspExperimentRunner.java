package tsp.experiment;

import experiment.ExperimentRunner;
import metrics.SearchMetrics;
import reporting.TspMetricsCsv;
import tsp.domain.TspInstance;
import tsp.domain.TspInstanceGenerator;
import tsp.domain.TspResult;
import tsp.solver.TspBacktracking;
import tsp.solver.TspBranchAndBound;
import tsp.solver.TspBruteForce;
import tsp.solver.TspSolver;

import java.util.Arrays;
import java.util.List;

public final class TspExperimentRunner implements ExperimentRunner<TspSolver, TspInstance> {

    @Override
    public long warmup(TspSolver solver, TspInstance instance) {
        long startNanos = System.nanoTime();
        for (int warmup = 0; warmup < WARMUP_RUNS; warmup++) {
            solver.solve(instance);
        }
        return System.nanoTime() - startNanos;
    }

    public void run(TspMetricsCsv report, int[] cityCounts) {
        List<TspSolver> solvers = getSolvers();
        for (int cityCount : cityCounts) {
            TspInstance instance = TspInstanceGenerator.generate(cityCount);
            long expectedCost = -1;
            iterateSolvers(report, cityCount, solvers, instance, expectedCost);
        }
    }

    private void iterateSolvers(TspMetricsCsv report, int cityCount, List<TspSolver> solvers, TspInstance instance, long expectedCost) {
        for (TspSolver solver : solvers) {
            long warmupElapsedNanos = warmup(solver, instance);
            long[] elapsedNanos = new long[MEASURED_RUNS];
            TspResult result = solver.solve(instance);
            elapsedNanos[0] = result.metrics().getElapsedNanos();

            validateIllegalState(expectedCost, result);

            expectedCost = result.optimalCost();

            validateRoute(result.route(), cityCount);
            for (int repetition = 1; repetition < MEASURED_RUNS; repetition++) {
                TspResult repeatedResult = solver.solve(instance);
                validateIllegalState(repeatedResult, result);
                validateRoute(repeatedResult.route(), cityCount);
                elapsedNanos[repetition] = repeatedResult.metrics().getElapsedNanos();
            }
            Arrays.sort(elapsedNanos);
            long medianElapsedNanos = elapsedNanos[MEASURED_RUNS / 2];
            var metrics = result.metrics();
            report.addExecution("tsp", cityCount, result.algorithm(), result.optimalCost(), metrics,
                medianElapsedNanos, warmupElapsedNanos);
            printTSP(cityCount, result, metrics, medianElapsedNanos);
        }
    }

    private static void printTSP(int cityCount, TspResult result, SearchMetrics metrics, long medianElapsedNanos) {
        System.out.printf(
            "TSP cidades=%d %-16s custo=%d rota=%s nós=%d tempo=%d ns%n",
            cityCount,
            result.algorithm(),
            result.optimalCost(),
            Arrays.toString(result.route()),
            metrics.getNodesExplored(),
            medianElapsedNanos
        );
    }

    private static List<TspSolver> getSolvers() {
        return List.of(
            new TspBruteForce(),
            new TspBacktracking(),
            new TspBranchAndBound()
        );
    }

    private static void validateRoute(int[] route, int cityCount) {
        validateIllegalState(route, cityCount);
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

    private static void validateIllegalState(TspResult repeatedResult, TspResult result) {
        if (repeatedResult.optimalCost() != result.optimalCost()) {
            throw new IllegalStateException("As execuções TSP encontraram custos ótimos diferentes.");
        }
    }

    private static void validateIllegalState(long expectedCost, TspResult result) {
        if (expectedCost != -1 && result.optimalCost() != expectedCost) {
            throw new IllegalStateException("Os algoritmos TSP encontraram custos ótimos diferentes.");
        }
    }

    private static void validateIllegalState(int[] route, int cityCount) {
        boolean routeDoesNotClose = route.length != cityCount + 1
                || route[0] != 0
                || route[cityCount] != 0;
        if (routeDoesNotClose) {
            throw new IllegalStateException("A rota TSP deve iniciar e terminar na cidade de origem.");
        }
    }
}
