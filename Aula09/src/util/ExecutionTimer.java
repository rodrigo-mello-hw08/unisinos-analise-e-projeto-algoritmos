package util;

import java.util.function.Supplier;

public final class ExecutionTimer {

    private ExecutionTimer() {
    }

    public static <T> ExecutionMeasurement<T> measure(Supplier<T> operation) {
        long startTime = System.nanoTime();
        T result = operation.get();
        long elapsedNanoseconds = System.nanoTime() - startTime;
        return new ExecutionMeasurement<>(result, elapsedNanoseconds);
    }
}
