import tsp.domain.TspInstance;
import tsp.domain.TspInstanceGenerator;
import tsp.domain.TspResult;
import tsp.solver.TspBranchAndBound;

/**
 * Auxiliar das figuras (não faz parte do experimento). Gera a instância do TSP com o
 * mesmo gerador do Main e imprime, em JSON, a matriz de distâncias, a rota ótima e o custo.
 * Uso: java -cp out;figuras/out ExportarInstanciaTsp <n>
 */
public class ExportarInstanciaTsp {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        TspInstance inst = TspInstanceGenerator.generate(n);
        TspResult r = new TspBranchAndBound().solve(inst);
        StringBuilder sb = new StringBuilder("{\"n\":").append(n).append(",\"distancias\":[");
        for (int i = 0; i < n; i++) {
            sb.append(i > 0 ? "," : "").append('[');
            for (int j = 0; j < n; j++) sb.append(j > 0 ? "," : "").append(inst.distanceBetween(i, j));
            sb.append(']');
        }
        sb.append("],\"rota\":[");
        int[] rota = r.route();
        for (int i = 0; i < rota.length; i++) sb.append(i > 0 ? "," : "").append(rota[i]);
        sb.append("],\"custo\":").append(r.optimalCost()).append('}');
        System.out.println(sb);
    }
}
