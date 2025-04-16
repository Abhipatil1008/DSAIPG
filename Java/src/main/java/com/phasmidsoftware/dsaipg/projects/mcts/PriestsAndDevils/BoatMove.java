package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;

public class BoatMove implements Move<PriestDevilGame> {
    public final int priests;
    public final int devils;

    public BoatMove(int priests, int devils) {
        this.priests = priests;
        this.devils = devils;
    }

    @Override
    public int player() {
        return 0; // single-player puzzle
    }

    @Override
    public String toString() {
        return "[P: " + priests + ", D: " + devils + "]";
    }
}


