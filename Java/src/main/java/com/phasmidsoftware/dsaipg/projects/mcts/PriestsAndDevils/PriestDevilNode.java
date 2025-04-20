package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;

public class PriestDevilNode implements Node<PriestDevilGame> {

    private final State<PriestDevilGame> state;
    private final Collection<Node<PriestDevilGame>> children = new ArrayList<>();

    private int wins = 0;
    private int playouts = 0;

    public PriestDevilNode(State<PriestDevilGame> state) {
        this.state = state;
    }

    @Override
    public State<PriestDevilGame> state() {
        return state;
    }

    @Override
    public boolean white() {
        return true; // optional; can be ignored in a single-player game
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public Collection<Node<PriestDevilGame>> children() {
        return children;
    }

    @Override
    public void addChild(State<PriestDevilGame> state) {
        children.add(new PriestDevilNode(state));
    }

    @Override
    public void backPropagate() {
        backPropagate(0.5);
    }

    public void backPropagate(double result) {
        playouts++;
        wins += result;
    }


    @Override
    public int wins() {
        return wins;
    }

    @Override
    public int playouts() {
        return playouts;
    }
}
