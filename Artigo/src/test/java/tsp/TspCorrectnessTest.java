package tsp;

import org.junit.jupiter.api.Test;
import tsp.domain.*;
import tsp.solver.*;
import static org.junit.jupiter.api.Assertions.*;

class TspCorrectnessTest {
    private final TspSolver[] solvers = {new TspBruteForce(),new TspBacktracking(),new TspBranchAndBound()};

    @Test
    void algorithmsAgreeOnFixedInstancesAndRoutesAreValid() {
        for (int n=1;n<=8;n++) {
            TspInstance instance=TspInstanceGenerator.generate(n);
            long expected=-1;
            for (TspSolver solver:solvers) {
                TspResult r=solver.solve(instance);
                if(expected<0) expected=r.optimalCost();
                assertEquals(expected,r.optimalCost());
                int[] route=r.route();
                assertEquals(n+1,route.length); assertEquals(0,route[0]); assertEquals(0,route[n]);
                boolean[] seen=new boolean[n];
                for(int i=0;i<n;i++){assertTrue(route[i]>=0&&route[i]<n);assertFalse(seen[route[i]]);seen[route[i]]=true;}
                assertTrue(r.metrics().getElapsedNanos()>=0); assertTrue(r.metrics().getNodesExplored()>0);
            }
        }
    }
    @Test
    void rejectsMalformedDistanceMatrices() {
        assertThrows(IllegalArgumentException.class,()->new TspInstance(new long[][]{{0,1},{2,0}}));
        assertThrows(IllegalArgumentException.class,()->new TspInstance(new long[][]{{0,-1},{-1,0}}));
    }
}
