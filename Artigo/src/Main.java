import nqueens.experiment.NQueensExperimentRunner;
import reporting.NQueensMetricsCsv;
import reporting.TspMetricsCsv;
import tsp.experiment.TspExperimentRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        NQueensMetricsCsv nQueensReport = new NQueensMetricsCsv();
        TspMetricsCsv tspReport = new TspMetricsCsv();
        NQueensExperimentRunner.run(nQueensReport, new int[]{4, 5, 6, 7});
        TspExperimentRunner.run(tspReport, new int[]{1, 2, 4, 6, 8, 10});

        Path outputDirectory = Path.of("resources");
        Files.createDirectories(outputDirectory);
        Path nQueensOutput = outputDirectory.resolve("resultados-nqueens.csv");
        Path tspOutput = outputDirectory.resolve("resultados-tsp.csv");
        nQueensReport.writeTo(nQueensOutput);
        tspReport.writeTo(tspOutput);
        System.out.println("CSV N-Queens: " + nQueensOutput.toAbsolutePath());
        System.out.println("CSV TSP: " + tspOutput.toAbsolutePath());
    }
}
