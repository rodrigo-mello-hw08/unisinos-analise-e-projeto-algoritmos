package reporting;

import metrics.SearchMetrics;

public final class TspMetricsCsv extends SearchMetricsCsv {
    public TspMetricsCsv() {
        super("problema,quantidade_de_cidades,algoritmo,tempo_decorrido_ns,nos_explorados,podas_por_cidade_visitada,solucoes_completas_avaliadas,custo_otimo");
    }

    public void addExecution(String problem, int cityCount, String algorithm, long optimalCost, SearchMetrics metrics) {
        appendRow(problem, Integer.toString(cityCount), algorithm,
                Long.toString(metrics.getElapsedNanos()), Long.toString(metrics.getNodesExplored()),
                Long.toString(metrics.getPrunedByVisitedCity()),
                Long.toString(metrics.getCompleteSolutionsEvaluated()), Long.toString(optimalCost));
    }
}
