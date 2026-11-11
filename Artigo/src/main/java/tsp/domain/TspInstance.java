package tsp.domain;

import java.util.Arrays;

/** Instância do TSP representada por uma matriz simétrica de distâncias. */
public final class TspInstance {
    private final long[][] distances;

    public TspInstance(long[][] distances) {
        validate(distances);
        this.distances = copy(distances);
    }

    public int cityCount() {
        return distances.length;
    }

    public long distanceBetween(int from, int to) {
        return distances[from][to];
    }

    private static void validate(long[][] distances) {
        boolean hasNoDistances = distances == null || distances.length == 0;
        if (hasNoDistances) {
            throw new IllegalArgumentException("A instância deve conter ao menos uma cidade.");
        }
        for (long[] longs : distances) {
            boolean rowIsInvalid = longs == null || longs.length != distances.length;
            if (rowIsInvalid) {
                throw new IllegalArgumentException("A matriz de distâncias deve ser quadrada.");
            }
        }
        for (int row = 0; row < distances.length; row++) {
            for (int column = row; column < distances.length; column++) {
                boolean distancesAreDifferent = isDistancesAreDifferent(distances, row, column);
                if (distancesAreDifferent) {
                    throw new IllegalArgumentException("A matriz de distâncias deve ser simétrica.");
                }
            }
        }
    }

    private static boolean isDistancesAreDifferent(long[][] distances, int row, int column) {
        long distance = distances[row][column];
        boolean distanceIsNegative = distance < 0;
        if (distanceIsNegative) {
            throw new IllegalArgumentException("As distâncias não podem ser negativas.");
        }
        boolean isDiagonal = row == column;
        boolean diagonalIsNotZero = isDiagonal && distance != 0;
        if (diagonalIsNotZero) {
            throw new IllegalArgumentException("A distância de uma cidade para si mesma deve ser zero.");
        }
        return distance != distances[column][row];
    }

    private static long[][] copy(long[][] source) {
        long[][] result = Arrays.copyOf(source, source.length);
        for (int row = 0; row < source.length; row++) {
            result[row] = source[row].clone();
        }
        return result;
    }
}
