package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.HashSet;
import java.util.Set;

import java.util.*;

public class BenchmarkPriestDevilMCTS {

    static class BenchmarkResult {
        int simulations;
        int successCount;
        long totalTime;
        int totalSteps;

        BenchmarkResult(int simulations) {
            this.simulations = simulations;
        }

        double successRate(int trials) {
            return (successCount * 100.0) / trials;
        }

        double avgTime() {
            return successCount == 0 ? 0 : (totalTime * 1.0) / successCount;
        }

        double avgSteps() {
            return successCount == 0 ? 0 : (totalSteps * 1.0) / successCount;
        }
    }

    public static void main(String[] args) {
        int[] simulationCounts = {1000, 5000, 10000};
        int trialsPerSim = 30;
        List<BenchmarkResult> results = new ArrayList<>();
        System.out.printf("%-15s %-17s %-19s %-12s%n", "Simulations", "Avg Time (ms)", "Success Rate (%)", "Avg Steps");
        System.out.println("-----------------------------------------------------------------------");

        for (int simulations : simulationCounts) {
            BenchmarkResult result = new BenchmarkResult(simulations);
            for (int trial = 0; trial < trialsPerSim; trial++) {
                MCTPriestDevilState startState = new MCTPriestDevilState(GameState.initialState());
                PriestDevilNode root = new PriestDevilNode(startState);
                PriestDevilMCTS mcts = new PriestDevilMCTS(root, simulations);  // pass simulations

                long start = System.currentTimeMillis();
                int steps = 0;
                GameState current = startState.unwrap();
                Set<GameState> visited = new HashSet<>();
                boolean solved = false;

                while (steps++ < 50 && current.isValid() && !visited.contains(current)) {
                    visited.add(current);
                    State<PriestDevilGame> returned = mcts.runMCTS(new MCTPriestDevilState(current));
                    GameState next = ((MCTPriestDevilState) returned).unwrap();
                    if (next.isGoal()) {
                        solved = true;
                        break;
                    }
                    current = next;
                }

                long end = System.currentTimeMillis();
                if (solved) {
                    result.successCount++;
                    result.totalTime += (end - start);
                    result.totalSteps += steps;
                }
            }

            results.add(result);
            System.out.printf("%-15d %-17.2f %-19.2f %-12.2f%n",
                    result.simulations, result.avgTime(), result.successRate(trialsPerSim), result.avgSteps());
        }
        System.out.printf("%-15s %-17s %-19s %-12s%n", "Simulations", "Avg Time (ms)", "Success Rate (%)", "Avg Steps");
        System.out.println("-----------------------------------------------------------------------");
        for (BenchmarkResult r : results) {
            System.out.printf("%-15d %-17.2f %-19.2f %-12.2f%n",
                    r.simulations, r.avgTime(), r.successRate(trialsPerSim), r.avgSteps());
        }
    }
}
