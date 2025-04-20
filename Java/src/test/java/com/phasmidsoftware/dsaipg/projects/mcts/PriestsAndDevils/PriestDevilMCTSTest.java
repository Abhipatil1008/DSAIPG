package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;
import static org.junit.Assert.*;

public class PriestDevilMCTSTest {

    @Test
    public void testRunMCTSReturnsState() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode root = new PriestDevilNode(state);
        PriestDevilMCTS mcts = new PriestDevilMCTS(root, 100); // 100 simulations

        State<PriestDevilGame> result = mcts.runMCTS(state);
        assertNotNull(result);
    }

    @Test
    public void testRunMCTSDoesNotReturnNullOnValidStart() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode root = new PriestDevilNode(state);
        PriestDevilMCTS mcts = new PriestDevilMCTS(root, 100);

        State<PriestDevilGame> result = mcts.runMCTS(state);
        GameState gs = ((MCTPriestDevilState) result).unwrap();
        assertTrue(gs.isValid());
    }

    @Test
    public void testExpandReturnsNode() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode root = new PriestDevilNode(state);
        PriestDevilMCTS mcts = new PriestDevilMCTS(root, 100);
        assertNotNull(mcts.runMCTS(state));
    }



}

