import algorithms.*;
import infra.Generator;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Main {

    public static final String SEPARADOR = "------------------------------------";
    public static final Locale LOCALE_PTBR = Locale.forLanguageTag("pt-BR");

    public static final List<Integer> ENTRADAS = Arrays.asList(1000, 5000, 10000, 50000, 100000);
    public static final Map<String, SortAlg> algs;

    static {
        Map<String, SortAlg> m = new LinkedHashMap<>();
        m.put("Bubble",    BubbleSort::sort);
        m.put("Insertion", InsertSort::sort);
        m.put("Shell",     ShellSort::sort);
        m.put("Quick",     QuickSort::sort);
        m.put("Merge",     MergeSort::sort);
        algs = Collections.unmodifiableMap(m);
    }

    public static final long STACK_SIZE_FOR_REAL = 512L * 1024 * 1024;

    public static void main(String[] args) throws Exception{
        // Tem que criar uma thread pro Windows não sacanear e capar o tamanho da Stack
        Thread t = new Thread(null, () -> {
            ENTRADAS.forEach(Main::comparacaoAlgoritimos);
        }, "worker", STACK_SIZE_FOR_REAL);
        t.start();
        t.join();
    }

    private static BiConsumer<String, SortAlg> benchmark(int[] arr){
        return (name, alg) -> {
            int[] ordenado = medirTempo(
                    () -> alg.sort(arr),
                    "Ordenação " + name
            );
            medirTempo(
                    () -> alg.sort(ordenado),
                    "Ordenação " + name + " ordenado"
            );
        };

    }

    private static void comparacaoAlgoritimos(Integer tamanhoEntrada) {
        System.out.println("Gerando relatorio para entrada [" + tamanhoEntrada + "]");

        int[] arrayNaoOrdenado = medirTempo(
                () -> Generator.generateArray(tamanhoEntrada),
                "Geracao"
        );

        algs.forEach(benchmark(Arrays.copyOf(arrayNaoOrdenado,arrayNaoOrdenado.length)));

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