package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

public class MCTPriestDevilState implements State<PriestDevilGame> {
    private final GameState state;

    public MCTPriestDevilState(GameState state) {
        this.state = state;
    }

    @Override
    public PriestDevilGame game() {
        return new PriestDevilGame(); // return G not Game<G>
    }

    @Override
    public int player() {
        return 0;
    }

    @Override
    public boolean isTerminal() {
        return state.isGoal() || !state.isValid();
    }

    @Override
    public Optional<Integer> winner() {
        if (state.isGoal()) return Optional.of(0);      // Win
        if (!state.isValid()) return Optional.of(1);    // Loss
        return Optional.empty();
    }

    @Override
    public Collection<Move<PriestDevilGame>> moves(int player) {
        List<Move<PriestDevilGame>> list = new ArrayList<>();
        for (GameState nextState : MoveGenerator.generateNextStates(state)) {
            int pMoved = Math.abs((state.boatOnLeft ? state.priestsLeft - nextState.priestsLeft : nextState.priestsLeft - state.priestsLeft));
            int dMoved = Math.abs((state.boatOnLeft ? state.devilsLeft - nextState.devilsLeft : nextState.devilsLeft - state.devilsLeft));
            list.add(new BoatMove(pMoved, dMoved));
        }
        return list;
    }

    @Override
    public State<PriestDevilGame> next(Move<PriestDevilGame> move) {
        BoatMove bm = (BoatMove) move;
        GameState newState = state.move(bm.priests, bm.devils);
        if (newState == null) return null;
        return new MCTPriestDevilState(newState);
    }

    public GameState unwrap() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MCTPriestDevilState s && this.state.equals(s.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public Random random() {
        return new Random(); // or keep a static one if preferred
    }

}

