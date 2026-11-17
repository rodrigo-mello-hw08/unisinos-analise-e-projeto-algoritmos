package analysis;

import domain.classical.dynamic.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import runner.KnapsackInstances;
import service.classical.dynamic.KnapsackClassicDynamicProblem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Imprime, em Markdown, a tabela da Programacao Dinamica (mochila 0/1) da instancia PEQUENA
 * (5 itens, W = 10), o caminho de reconstrucao e um exemplo de reaproveitamento de celulas.
 * A recorrencia aqui e a mesma de KnapsackClassicDynamicProblem; ao final o resultado e
 * conferido contra a classe do projeto (valor total e itens escolhidos).
 */
public class DpTable {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        List<KnapsackItem> items = KnapsackInstances.createSmallInstance();
        int n = items.size(), cap = 10;
        int[] w = new int[n + 1];
        int[] v = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            w[i] = (int) items.get(i - 1).getWeight();
            v[i] = (int) items.get(i - 1).getValue();
        }

        int[][] dp = new int[n + 1][cap + 1];
        for (int i = 1; i <= n; i++) {
            for (int c = 0; c <= cap; c++) {
                dp[i][c] = dp[i - 1][c];
                if (w[i] <= c && dp[i - 1][c - w[i]] + v[i] > dp[i][c]) dp[i][c] = dp[i - 1][c - w[i]] + v[i];
            }
        }

        // reconstrucao (mesma regra do projeto: dp[i][c] > dp[i-1][c] => item escolhido)
        Set<String> path = new HashSet<>();
        List<String> steps = new ArrayList<>();
        List<String> chosen = new ArrayList<>();
        int c = cap;
        path.add(n + "," + c);
        for (int i = n; i >= 1; i--) {
            String name = items.get(i - 1).getName();
            if (dp[i][c] > dp[i - 1][c]) {
                steps.add(String.format("- `dp[%d][%d] = %d` > `dp[%d][%d] = %d` → o item **%s** (peso %d, valor %d) foi escolhido; capacidade restante %d → %d.",
                        i, c, dp[i][c], i - 1, c, dp[i - 1][c], name, w[i], v[i], c, c - w[i]));
                chosen.add(name);
                c -= w[i];
            } else {
                steps.add(String.format("- `dp[%d][%d] = %d` = `dp[%d][%d] = %d` → o item **%s** não foi escolhido; capacidade segue %d.",
                        i, c, dp[i][c], i - 1, c, dp[i - 1][c], name, c));
            }
            path.add((i - 1) + "," + c);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| i \\ c |");
        for (int k = 0; k <= cap; k++) sb.append(' ').append(k).append(" |");
        sb.append('\n').append("|---|");
        for (int k = 0; k <= cap; k++) sb.append("---|");
        sb.append('\n');
        for (int i = 0; i <= n; i++) {
            String label = i == 0 ? "0 (nenhum item)"
                    : i + " (" + items.get(i - 1).getName() + ": p=" + w[i] + ", v=" + v[i] + ")";
            sb.append("| ").append(label).append(" |");
            for (int k = 0; k <= cap; k++) {
                String cell = String.valueOf(dp[i][k]);
                sb.append(' ').append(path.contains(i + "," + k) ? "**" + cell + "**" : cell).append(" |");
            }
            sb.append('\n');
        }

        System.out.println("Instancia pequena: capacidade " + cap + ", itens "
                + items.stream().map(it -> it.getName() + "(" + (int) it.getWeight() + "," + (int) it.getValue() + ")")
                        .reduce((a, b) -> a + " " + b).orElse(""));
        System.out.println();
        System.out.println("Tabela DP (negrito = caminho percorrido na reconstrução):");
        System.out.println();
        System.out.print(sb);
        System.out.println();
        System.out.println("Reconstrução (de dp[" + n + "][" + cap + "] até dp[0][...]):");
        System.out.println();
        steps.forEach(System.out::println);
        System.out.println();
        System.out.println("Itens escolhidos: " + chosen + " | valor ótimo dp[" + n + "][" + cap + "] = " + dp[n][cap]);

        // exemplo de reaproveitamento: celula da linha 2 (item B) lida por duas celulas da linha 3 (item C)
        int i = 3, k = w[i] * 2;
        System.out.println();
        System.out.println("Exemplo de reaproveitamento (item " + items.get(i - 1).getName() + ", peso " + w[i] + "):");
        System.out.println();
        System.out.printf("- `dp[%d][%d] = max( dp[%d][%d] = %d , dp[%d][%d] + %d = %d + %d )` = %d%n",
                i, k, i - 1, k, dp[i - 1][k], i - 1, k - w[i], v[i], dp[i - 1][k - w[i]], v[i], dp[i][k]);
        System.out.printf("- A célula `dp[%d][%d] = %d` foi calculada uma única vez e é lida de novo por `dp[%d][%d]` (ramo \"não levar\") e por `dp[%d][%d]` (ramo \"levar\", pois %d + %d = %d).%n",
                i - 1, k - w[i], dp[i - 1][k - w[i]], i, k - w[i], i, k, k - w[i], w[i], k);

        // conferencia com a implementacao do projeto
        ClassicKnapsack project = (ClassicKnapsack) new KnapsackClassicDynamicProblem(cap).resolve(items);
        Set<String> projectNames = new HashSet<>();
        project.getSelectedItems().forEach(it -> projectNames.add(it.getName()));
        boolean ok = Math.abs(project.getTotalValue() - dp[n][cap]) < 1e-9 && projectNames.equals(new HashSet<>(chosen));
        System.out.println();
        System.out.println("Conferência com KnapsackClassicDynamicProblem: " + (ok ? "OK (mesmo valor e mesmos itens)" : "DIVERGENTE"));
        if (!ok) System.exit(1);
    }
}
