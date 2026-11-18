import runner.ComparisonRunner;
import runner.DivideAndConquerRunner;
import runner.DynamicRunner;
import runner.GreedyRunner;

public class Main {

    public static void main(String[] args) {
        GreedyRunner.run();
        DynamicRunner.run();
        DivideAndConquerRunner.run();
        ComparisonRunner.run();
    }

}
