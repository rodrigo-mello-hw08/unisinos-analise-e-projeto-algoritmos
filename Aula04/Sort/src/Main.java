import algorithms.*;
import infra.Generator;


import java.util.*;

public class Main {

    public static final String SEPARADOR = "------------------------------------";
    public static final Locale LOCALE_PTBR = Locale.forLanguageTag("pt-BR");
    public static final long STACK_SIZE_FOR_REAL = 512L * 1024 * 1024;

    public static final List<Integer> ENTRADAS = Arrays.asList(1000, 5000, 10000, 50000, 100000);
    public static final List<SortAlg> ALGS = List.of(new BubbleSort(), new InsertSort(), new ShellSort(), new QuickSort(), new MergeSort());

    static void main(String[] args) throws Exception{
        // Tem que criar uma thread pro Windows não sacanear e capar o tamanho da Stack
        Thread t = new Thread(null, Main::executarBenchmark, "worker", STACK_SIZE_FOR_REAL);
        t.start();
        t.join();
    }

    private static void executarBenchmark() {
        for (SortAlg alg : ALGS) {
            benchmarkAlgoritmo(alg);
        }
    }

    private static void benchmarkAlgoritmo(SortAlg alg) {
        System.out.println();
        System.out.println(alg.getNome());

        System.out.println("Entrada\t"
                + alg.getNome() + "\t"
                + alg.getNome() + " Ordenado"
        );

        for (Integer tamanhoEntrada : ENTRADAS) {
            int[] arrayNaoOrdenado = Generator.generateArray(tamanhoEntrada);
            medirTempos(alg, arrayNaoOrdenado, tamanhoEntrada);
        }

        System.out.println(SEPARADOR);
    }

    private static void medirTempos(SortAlg alg, int[] arrayNaoOrdenado, int tamanhoEntrada) {
        int[] copiaArray = Arrays.copyOf(arrayNaoOrdenado, arrayNaoOrdenado.length);

        long inicio = System.nanoTime();
        int[] arrayOrdenado = alg.sort(copiaArray);
        double tempoNaoOrdenado = medirTempoDecorrido(inicio);

        inicio = System.nanoTime();
        alg.sort(arrayOrdenado);
        double tempoOrdenado = medirTempoDecorrido(inicio);

        System.out.printf(LOCALE_PTBR,
                "%d\t%.3f\t%.3f%n",
                tamanhoEntrada,
                tempoNaoOrdenado,
                tempoOrdenado
        );
    }

    private static double medirTempoDecorrido(long inicio) {
        return (System.nanoTime() - inicio) / 1_000_000.0;
    }
}