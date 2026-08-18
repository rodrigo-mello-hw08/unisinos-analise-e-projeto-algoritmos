import ordenacao.BubbleSort;
import ordenacao.Generator;
import ordenacao.InsertSort;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class Main {

    public static final String SEPARADOR = "------------------------------------";
    public static final Locale LOCALE_PTBR = Locale.forLanguageTag("pt-BR");

    public static void main(String[] args) {
        List<Integer> entradas = Arrays.asList(1000, 5000, 10000, 50000, 100000);
        entradas.forEach(Main::comparacaoAlgoritimos);
    }

    private static void comparacaoAlgoritimos(Integer tamanhoEntrada) {
        System.out.println("Gerando relatorio para entrada [" + tamanhoEntrada + "]");

        int[] arrayNaoOrdenado = medirTempo(
                () -> Generator.generateArray(tamanhoEntrada),
                "Geracao"
        );

        int[] arrayBubble = Arrays.copyOf(arrayNaoOrdenado, arrayNaoOrdenado.length);
        int[] bubbleOrdenado = medirTempo(
                () -> BubbleSort.sort(arrayBubble),
                "Ordenacao Bubble"
        );
        medirTempo(
                () -> BubbleSort.sort(bubbleOrdenado),
                "Ordenacao Bubble Ordenado"
        );

        int[] arrayInsert = Arrays.copyOf(arrayNaoOrdenado, arrayNaoOrdenado.length);
        int[] insertOrdenado = medirTempo(
                () -> InsertSort.sort(arrayInsert),
                "Ordenacao Insert"
        );
        medirTempo(
                () -> InsertSort.sort(insertOrdenado),
                "Ordenacao Insert Ordenado"
        );
        System.out.println(SEPARADOR);
    }

    private static <T> T medirTempo(Supplier<T> operacao, String nomeOperacao) {
        long inicio = System.nanoTime();
        T resultado = operacao.get();
        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1_000_000.0;

        String mensagem = String.format(LOCALE_PTBR, "Tempo de %s: %.3f ms", nomeOperacao, tempoMs);
        System.out.println(mensagem);
        return resultado;
    }
}