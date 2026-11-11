package tsp.solver;

import tsp.domain.TspInstance;

final class TspSupport {
    private TspSupport() {
    }

    static boolean isCanonicalOrientation(int[] route, int cityCount) {
        boolean orientationMatters = cityCount > 2;
        if (!orientationMatters) {
            return true;
        }
        return route[1] < route[cityCount - 1];
    }

    static long routeCost(TspInstance instance, int[] route, int cityCount) {
        long cost = 0;
        for (int index = 0; index < cityCount - 1; index++) {
            cost += instance.distanceBetween(route[index], route[index + 1]);
        }
        cost += instance.distanceBetween(route[cityCount - 1], route[0]);
        return cost;
    }

    static int[] completedRoute(int[] partialRoute, int cityCount) {
        int[] route = new int[cityCount + 1];
        System.arraycopy(partialRoute, 0, route, 0, cityCount);
        route[cityCount] = partialRoute[0];
        return route;
    }
}
