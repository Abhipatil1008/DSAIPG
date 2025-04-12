package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

import java.util.*;

public class BenchmarkMCTS {

    public static void main(String[] args) {
        int[] simulationCounts = {100_000, 500_000, 1_000_000};
        int runsPerSimulation = 10;

        System.out.printf("%-15s %-15s %-15s %-25s%n", "Simulations", "Avg Time (ms)", "AI Win Rate", "Avg Moves");

        for (int sim : simulationCounts) {
            long totalTime = 0;
            int aiWins = 0;
            int totalMoves = 0;

            for (int i = 0; i < runsPerSimulation; i++) {
                TicTacToe game = new TicTacToe();
                State<TicTacToe> state = game.start();
                int player = game.opener();
                int moveCount = 0;
                MCTS mcts = new MCTS(state);

                long startTime = System.nanoTime();
                while (!state.isTerminal()) {
                    if (player == TicTacToe.X) {
                        List<Move<TicTacToe>> moves = new ArrayList<>(state.moves(player));
                        Collections.shuffle(moves);
                        state = state.next(moves.get(0));
                        mcts.updateRoot(state);
                    } else {
                        state = mcts.runMCTS(sim);
                    }
                    moveCount++;
                    player = 1 - player;
                }
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
                totalMoves += moveCount;

                if (state.winner().isPresent() && state.winner().get() == TicTacToe.O)
                    aiWins++;
            }

            double avgTimeMs = totalTime / 1_000_000.0 / runsPerSimulation;
            double winRate = (aiWins * 100.0) / runsPerSimulation;
            double avgMoves = totalMoves / (double) runsPerSimulation;

            System.out.printf("%-15d %-15.2f %-15.2f %-25.2f%n", sim, avgTimeMs, winRate, avgMoves);
        }
    }
}
