package experiment;

public interface ExperimentRunner<S, I> {
    int WARMUP_RUNS = 1;
    int MEASURED_RUNS = 5;

    void warmup(S solver, I input);
}
