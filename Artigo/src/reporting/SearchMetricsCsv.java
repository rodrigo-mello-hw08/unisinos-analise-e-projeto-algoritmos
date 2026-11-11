package reporting;

import metrics.SearchMetrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Gera relatórios CSV para execuções que usam métricas de busca compartilhadas. */
public final class SearchMetricsCsv {

    private static final String HEADER = "problema,n,algoritmo,tempo_decorrido_ns,nos_explorados,tentativas_de_posicionamento,podas_por_coluna,podas_por_diagonal,podas_por_limite,configuracoes_completas_invalidas,avaliacoes_de_limite,quantidade_de_solucoes\n";
    public static final char SEPARATOR = ',';

    private final StringBuilder content = new StringBuilder(HEADER);

    public void addExecution(String problem, int size, String algorithm, long solutionCount, SearchMetrics metrics) {
        content.append(escape(problem)).append(SEPARATOR)
                .append(size).append(SEPARATOR)
                .append(escape(algorithm)).append(SEPARATOR)
                .append(metrics.getElapsedNanos()).append(SEPARATOR)
                .append(metrics.getNodesExplored()).append(SEPARATOR)
                .append(metrics.getPlacementAttempts()).append(SEPARATOR)
                .append(metrics.getPrunedByColumn()).append(SEPARATOR)
                .append(metrics.getPrunedByDiagonal()).append(SEPARATOR)
                .append(metrics.getPrunedByBound()).append(SEPARATOR)
                .append(metrics.getInvalidCompleteConfigurations()).append(SEPARATOR)
                .append(metrics.getBoundEvaluations()).append(SEPARATOR)
                .append(solutionCount).append('\n');
    }

    public void writeTo(Path output) throws IOException {
        Files.writeString(output, content);
    }

    private String escape(String value) {
        boolean containsComma = value.contains(",");
        boolean containsQuote = value.contains("\"");
        boolean containsLineFeed = value.contains("\n");
        boolean containsCarriageReturn = value.contains("\r");
        boolean requiresQuotes = containsComma || containsQuote || containsLineFeed || containsCarriageReturn;
        if (!requiresQuotes) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
