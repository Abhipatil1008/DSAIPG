package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import org.junit.Test;
import static org.junit.Assert.*;

public class PriestDevilNodeTest {

    @Test
    public void testInitialNode() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode node = new PriestDevilNode(state);
        assertEquals(state, node.state());
    }

    @Test
    public void testIsLeafInitiallyTrue() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode node = new PriestDevilNode(state);
        assertTrue(node.isLeaf());
    }

    @Test
    public void testChildrenInitiallyEmpty() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode node = new PriestDevilNode(state);
        assertEquals(0, node.children().size());
    }

    @Test
    public void testBackPropagateUpdatesStats() {
        MCTPriestDevilState state = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode node = new PriestDevilNode(state);
        node.backPropagate(1.0);
        assertEquals(1, node.playouts());
        assertEquals(1.0, node.wins(), 0.001);
    }
}
