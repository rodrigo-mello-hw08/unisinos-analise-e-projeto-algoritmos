package tsp.solver;

import tsp.domain.TspInstance;
import tsp.domain.TspResult;

public interface TspSolver {
    TspResult solve(TspInstance instance);
}
