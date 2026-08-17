import ordenacao.BubbleSort;
import ordenacao.Generator;
import ordenacao.InsertSort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr1000 = Generator.generateArray(1000);

        System.out.println("Array antes da ordenacao: " + Arrays.toString(arr1000));

        System.out.println("Array depois da ordenacao: " + Arrays.toString(BubbleSort.sort(arr1000)));
    }
}