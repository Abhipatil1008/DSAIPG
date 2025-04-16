package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    // All possible boat moves (1 or 2 passengers)
    private static final int[][] MOVES = {
            {1, 0}, // 1 priest
            {2, 0}, // 2 priests
            {0, 1}, // 1 devil
            {0, 2}, // 2 devils
            {1, 1}  // 1 priest + 1 devil
    };

    public static List<GameState> generateNextStates(GameState state) {
        List<GameState> nextStates = new ArrayList<>();

        for (int[] move : MOVES) {
            int priestsToMove = move[0];
            int devilsToMove = move[1];

            // Prevent move that removes people from wrong bank
            if (state.boatOnLeft) {
                if (state.priestsLeft < priestsToMove || state.devilsLeft < devilsToMove)
                    continue;
            } else {
                if (state.priestsRight < priestsToMove || state.devilsRight < devilsToMove)
                    continue;
            }

            GameState newState = state.move(priestsToMove, devilsToMove);
            if (newState != null && newState.isValid()) {
                nextStates.add(newState);
            }
        }

        return nextStates;
    }
}
