package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveGenerator {

    private static final int[][] MOVES = {
            {1, 0},
            {2, 0},
            {0, 1},
            {0, 2},
            {1, 1}
    };

    public static List<GameState> generateNextStates(GameState state) {
        List<GameState> nextStates = new ArrayList<>();

        if (state.isGoal()) {
            return Collections.emptyList();
        }

        for (int[] move : MOVES) {
            int priestsToMove = move[0];
            int devilsToMove = move[1];

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
