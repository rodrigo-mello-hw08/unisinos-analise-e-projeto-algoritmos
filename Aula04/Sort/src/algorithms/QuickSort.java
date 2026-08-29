package algorithms;

public class QuickSort implements SortAlg {
    void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                swap(a, ++i, j);
            }
        }

        swap(a,i + 1, high);
        return i + 1;
    }
    void recursive_sort(int[] a, int low, int high) {
        if (low >= high) return;
        int p = partition(a, low, high);
        recursive_sort(a, low, p - 1);
        recursive_sort(a, p + 1, high);
    }

    @Override
    public int[] sort(int[] arr){
        recursive_sort(arr, 0, arr.length - 1);
        return arr;
    }

    @Override
    public String getNome() {
        return "Quick";
    }
}
