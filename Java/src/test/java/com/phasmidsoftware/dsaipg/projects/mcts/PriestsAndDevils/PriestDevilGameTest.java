package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;
import static org.junit.Assert.*;

public class PriestDevilGameTest {

    @Test
    public void testStartStateIsValid() {
        PriestDevilGame game = new PriestDevilGame();
        State<PriestDevilGame> state = game.start();
        GameState gs = ((MCTPriestDevilState) state).unwrap();
        assertTrue(gs.isValid());
    }

    @Test
    public void testStartStateIsNotGoal() {
        PriestDevilGame game = new PriestDevilGame();
        State<PriestDevilGame> state = game.start();
        GameState gs = ((MCTPriestDevilState) state).unwrap();
        assertFalse(gs.isGoal());
    }

    @Test
    public void testOpenerIsZero() {
        PriestDevilGame game = new PriestDevilGame();
        assertEquals(0, game.opener());
    }

    @Test
    public void testInitialMoveCount() {
        GameState state = GameState.initialState();
        assertTrue(MoveGenerator.generateNextStates(state).size() > 0);
    }
}

