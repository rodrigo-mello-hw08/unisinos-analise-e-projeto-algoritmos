package service;

import domain.Knapsack;
import domain.classical.KnapsackItem;

import java.util.List;

public interface KnapsackProblem {
    Knapsack resolve(List<KnapsackItem> items);
}
