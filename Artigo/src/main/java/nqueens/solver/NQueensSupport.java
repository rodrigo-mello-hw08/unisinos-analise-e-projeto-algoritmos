package nqueens.solver;

final class NQueensSupport {
    private NQueensSupport() {}

    static void validateSize(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("N deve ser maior ou igual a 1.");
        }
        if (n > 46340) {
            throw new IllegalArgumentException("N excede o limite suportado pelas diagonais.");
        }
    }
    static boolean isValid(int[] q) {
        for (int i = 0; i < q.length; i++) {
            for (int j = i + 1; j < q.length; j++) {
                boolean sameColumn = q[i] == q[j];
                boolean sameDiagonal = Math.abs(q[i] - q[j]) == j - i;
                boolean conflict = sameColumn || sameDiagonal;
                if (conflict) {
                    return false;
                }
            }
        }
        return true;
    }
}
