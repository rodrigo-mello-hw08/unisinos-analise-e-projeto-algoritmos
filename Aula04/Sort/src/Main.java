import algorithms.*;
import infra.Generator;


import java.util.*;
import java.util.function.Supplier;

public class Main {

    public static final String SEPARADOR = "------------------------------------";
    public static final Locale LOCALE_PTBR = Locale.forLanguageTag("pt-BR");
    public static final long STACK_SIZE_FOR_REAL = 512L * 1024 * 1024;

    public static final List<Integer> ENTRADAS = Arrays.asList(1000, 5000, 10000, 50000, 100000);
    public static final List<SortAlg> algs = getAlgs();

    private static List<SortAlg> getAlgs() {
        return Arrays.asList(
            new BubbleSort(),
            new InsertSort(),
            new ShellSort(),
            new QuickSort(),
            new MergeSort()
        );
    }


    static void main(String[] args) throws Exception{
        // Tem que criar uma thread pro Windows não sacanear e capar o tamanho da Stack
        Thread t = new Thread(null, () -> {
            ENTRADAS.forEach(Main::comparacaoAlgoritimos);
        }, "worker", STACK_SIZE_FOR_REAL);
        t.start();
        t.join();
    }

    private static void comparacaoAlgoritimos(Integer tamanhoEntrada) {
        System.out.println("Gerando relatorio para entrada [" + tamanhoEntrada + "]");

        int[] arrayNaoOrdenado = medirTempo(
                () -> Generator.generateArray(tamanhoEntrada),
                "Geracao"
        );

        algs.forEach( a -> extrairTempo(a, arrayNaoOrdenado));

        System.out.println(SEPARADOR);
    }

    private static void extrairTempo(SortAlg alg, int[] arrayNaoOrdenado) {
        int[] copiaArray = Arrays.copyOf(arrayNaoOrdenado, arrayNaoOrdenado.length);

        int[] arrayOrdenado = medirTempo(
                () -> alg.sort(copiaArray),
                "Ordenação " + alg.getNome()
        );

        medirTempo(
                () -> alg.sort(arrayOrdenado),
                "Ordenação " + alg.getNome() + " ordenado"
        );
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