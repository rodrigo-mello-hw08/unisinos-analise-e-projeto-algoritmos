package tsp.domain;

import java.util.Random;

public final class TspInstanceGenerator {
    private static final long SEED = 20261110L;

    private TspInstanceGenerator() {}

    public static TspInstance generate(int cityCount) {
        if (cityCount < 1) {
            throw new IllegalArgumentException("A instância deve conter ao menos uma cidade.");
        }
        long[][] distances = new long[cityCount][cityCount];
        Random random = new Random(SEED + cityCount);
        for (int from = 0; from < cityCount; from++) {
            for (int to = from + 1; to < cityCount; to++) {
                long distance = random.nextInt(100) + 1L;
                distances[from][to] = distance;
                distances[to][from] = distance;
            }
        }
        return new TspInstance(distances);
    }
}
