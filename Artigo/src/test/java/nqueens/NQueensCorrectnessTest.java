package nqueens;

import nqueens.solver.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NQueensCorrectnessTest {
    private final NQueensSolver[] efficientSolvers = {new NQueensBacktracking(), new NQueensBranchAndBound()};

    @Test
    void knownSolutionCountsAndAlgorithmAgreement() {
        int[] sizes = {1,2,3,4,5,6,7,8,9,10,11,12};
        long[] expected = {1,0,0,2,10,4,40,92,352,724,2680,14200};
        for (int i = 0; i < sizes.length; i++) {
            NQueensSolver[] solvers = sizes[i] <= 8
                    ? new NQueensSolver[]{new NQueensBruteForce(), efficientSolvers[0], efficientSolvers[1]}
                    : efficientSolvers;
            for (NQueensSolver solver : solvers) {
                var result=solver.solve(sizes[i]);
                assertEquals(expected[i], result.solutionCount(), solver.getClass().getSimpleName()+" N="+sizes[i]);
                assertTrue(result.metrics().getElapsedNanos() >= 0);
                assertTrue(result.metrics().getNodesExplored() > 0);
            }
        }
    }
}
