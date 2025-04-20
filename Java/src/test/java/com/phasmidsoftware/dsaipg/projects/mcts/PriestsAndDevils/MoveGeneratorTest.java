package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MoveGeneratorTest {

    @Test
    public void testGenerateInitialMoves() {
        GameState initial = GameState.initialState();
        List<GameState> nextStates = MoveGenerator.generateNextStates(initial);
        assertFalse("Initial state should generate moves", nextStates.isEmpty());
    }

    @Test
    public void testGenerateMovesFromMidState() {
        GameState mid = new GameState(1, 1, 2, 2, false);
        List<GameState> nextStates = MoveGenerator.generateNextStates(mid);
        assertFalse("Mid-state should generate moves", nextStates.isEmpty());
    }

    @Test
    public void testNoMovesFromGoalState() {
        GameState goal = new GameState(0, 0, 3, 3, false);

        assertTrue("Should detect goal state", goal.isGoal());

        List<GameState> nextStates = MoveGenerator.generateNextStates(goal);

        if (!nextStates.isEmpty()) {
            System.out.println("⚠️ Moves generated from goal state:");
            for (GameState state : nextStates) {
                state.render();
            }
        }

        assertTrue("Goal state should not generate further moves", nextStates.isEmpty());
    }


    @Test
    public void testAllGeneratedStatesAreValid() {
        GameState start = new GameState(3, 3, 0, 0, true);
        List<GameState> nextStates = MoveGenerator.generateNextStates(start);

        for (GameState state : nextStates) {
            assertTrue("Generated state must be valid", state.isValid());
        }
    }

    @Test
    public void testBoatSwitchesSides() {
        GameState start = new GameState(3, 3, 0, 0, true);
        List<GameState> nextStates = MoveGenerator.generateNextStates(start);

        for (GameState state : nextStates) {
            assertFalse("Boat should move to opposite bank", state.boatOnLeft);
        }
    }
}

