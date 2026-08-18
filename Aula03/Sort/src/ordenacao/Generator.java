package ordenacao;

public class Generator {
    public static final int MINIMO = 0;
    public static final int MAXIMO = 10500;

    public static int[] generateArray(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * (MAXIMO - MINIMO + 1)) + MINIMO;
        }
        return arr;
    }
}
