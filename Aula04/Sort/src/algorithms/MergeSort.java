package algorithms;

public class MergeSort {
    static void conquer(int[] a, int[] aux, int low, int middle, int high) {
        for (int k = low; k <= high; k++) aux[k] = a[k];

        int i = low;
        int j = middle + 1;
        for (int k = low; k <= high; k++) {
            if      (i > middle)       a[k] = aux[j++];
            else if (j > high)         a[k] = aux[i++];
            else if (aux[j] < aux[i])  a[k] = aux[j++];
            else                       a[k] = aux[i++];
        }

    }
    static void divide(int[] a, int[] aux, int low, int high) {
        if (low >= high) return;

        int middle = (low + high) / 2;
        divide(a, aux, low, middle);
        divide(a, aux, middle + 1, high);
        conquer(a, aux, low, middle, high);
    }

    public static int[] sort(int[] arr){
        int n = arr.length;
        int[] aux = new int[n];
        divide(arr, aux, 0, n - 1);
        return arr;
    }
}
