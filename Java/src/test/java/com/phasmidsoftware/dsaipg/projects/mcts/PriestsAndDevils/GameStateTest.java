package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void testInitialStateIsValid() {
        GameState state = new GameState(3, 3, 0, 0, true);
        assertTrue("Initial state should be valid", state.isValid());
    }

    @Test
    public void testInvalidStatePriestsEatenLeft() {
        GameState state = new GameState(1, 3, 2, 0, true);
        assertFalse("More devils than priests on left -> invalid", state.isValid());
    }

    @Test
    public void testInvalidStatePriestsEatenRight() {
        GameState state = new GameState(2, 0, 1, 3, false);
        assertFalse("More devils than priests on right -> invalid", state.isValid());
    }

    @Test
    public void testGoalState() {
        GameState goal = new GameState(0, 0, 3, 3, false);
        assertTrue("Goal state should return true", goal.isGoal());
    }

    @Test
    public void testEqualsMethod() {
        GameState a = new GameState(1, 1, 2, 2, true);
        GameState b = new GameState(1, 1, 2, 2, true);
        assertEquals("States with same values should be equal", a, b);
    }
}
