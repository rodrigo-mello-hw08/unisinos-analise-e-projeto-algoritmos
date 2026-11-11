package support;

import metrics.SearchMetrics;
import reporting.SearchMetricsCsv;
import org.junit.jupiter.api.BeforeAll;
import tsp.domain.TspInstance;
import tsp.domain.TspInstanceGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BenchmarkSupport {
    private static final Path OUTPUT = Path.of("target", "test-results", "benchmarks");
    private BenchmarkSupport() { }

    @BeforeAll
    static void warmup() {
        new nqueens.solver.NQueensBacktracking().solve(6);
        new tsp.solver.TspBacktracking().solve(TspInstanceGenerator.generate(6));
    }

    public static void row(SearchMetricsCsv csv, String problem, int size, String algorithm,
                           long solutions, String result, SearchMetrics metrics) {
        csv.addExecution(problem, size, algorithm, solutions, result, metrics);
    }

    public static void skipped(String problem, int size, String algorithm) {
        try {
            Files.createDirectories(OUTPUT);
            Files.writeString(OUTPUT.resolve("skipped.csv"),
                    "problema,n,algoritmo,status\n" + problem + "," + size + "," + algorithm + ",SKIPPED_BY_BUDGET\n");
        } catch (IOException e) { throw new IllegalStateException(e); }
    }

    public static void write(SearchMetricsCsv csv, String file) {
        try {
            Files.createDirectories(OUTPUT);
            csv.writeTo(OUTPUT.resolve(file));
        } catch (IOException e) { throw new IllegalStateException(e); }
    }

    public static TspInstance instance(int cities) { return TspInstanceGenerator.generate(cities); }
}
