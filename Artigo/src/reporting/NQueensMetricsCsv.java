package reporting;

import metrics.SearchMetrics;

public final class NQueensMetricsCsv extends SearchMetricsCsv {
    public NQueensMetricsCsv() {
        super("problema,n,algoritmo,warmup_elapsed_ns,tempo_decorrido_ns,nos_explorados,tentativas_de_posicionamento,podas_por_coluna,podas_por_diagonal,podas_por_limite,configuracoes_completas_invalidas,avaliacoes_de_limite,quantidade_de_solucoes");
    }

    public void addExecution(String problem, int size,
                             String algorithm, long solutionCount,
                             SearchMetrics metrics, long elapsedNanos, long warmupElapsedNanos) {
        appendRow(
            problem,
            Integer.toString(size),
            algorithm,
            Long.toString(warmupElapsedNanos),
            Long.toString(elapsedNanos),
            Long.toString(metrics.getNodesExplored()),
            Long.toString(metrics.getPlacementAttempts()),
            Long.toString(metrics.getPrunedByColumn()),
            Long.toString(metrics.getPrunedByDiagonal()),
            Long.toString(metrics.getPrunedByBound()),
            Long.toString(metrics.getInvalidCompleteConfigurations()),
            Long.toString(metrics.getBoundEvaluations()),
            Long.toString(solutionCount)
        );
    }
}
