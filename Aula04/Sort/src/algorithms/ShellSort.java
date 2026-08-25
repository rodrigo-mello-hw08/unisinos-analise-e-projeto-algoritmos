package algorithms;

public class ShellSort {
    public static int[] sort(int[] arr){
        int n = arr.length;
        int[] gaps = {701, 301, 132, 57, 23, 10, 4, 1};

        for (int gap : gaps) {
            if (gap >= n) continue;
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
        return arr;
    }
}
