package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MCTSTest {
    @Test
    public void testMCTSDoesNotCrashOnInitialRun() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        MCTS mcts = new MCTS(state);
        State<TicTacToe> best = mcts.runMCTS();
        assertNotNull(best);
    }


}