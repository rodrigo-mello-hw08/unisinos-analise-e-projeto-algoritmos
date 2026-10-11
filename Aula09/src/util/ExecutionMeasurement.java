package util;

public class ExecutionMeasurement<T> {

    private final T result;
    private final long elapsedNanoseconds;

    public ExecutionMeasurement(T result, long elapsedNanoseconds) {
        this.result = result;
        this.elapsedNanoseconds = elapsedNanoseconds;
    }

    public T getResult() {
        return result;
    }

    public long getElapsedNanoseconds() {
        return elapsedNanoseconds;
    }

    public double getElapsedMilliseconds() {
        return elapsedNanoseconds / 1_000_000.0;
    }
}
