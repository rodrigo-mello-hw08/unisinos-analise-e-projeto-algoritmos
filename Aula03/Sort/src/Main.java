import ordenacao.BubbleSort;
import ordenacao.InsertSort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        BubbleSort.sort(new int[]{2, 3, 10, 1});
        System.out.println("---- Insertion ----");
        int[] sorted = InsertSort.sort(new int[]{2, 3, 10, 1});
        System.out.println(Arrays.toString(sorted));
    }
}