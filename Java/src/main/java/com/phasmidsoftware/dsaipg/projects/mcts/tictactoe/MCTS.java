/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        //Node<TicTacToe> root = mcts.root;
        //State<TicTacToe> bestState = mcts.runMCTS(root.state());
        //System.out.println("Best state found:\n" + bestState);
        State<TicTacToe> bestState = mcts.runMCTS();  // now uses the internal root
        System.out.println("Best state found:\n" + bestState);
    }

    public MCTS(Node<TicTacToe> root) {
        //this.root = root;
    }
    public MCTS(State<TicTacToe> rootState) {
        this.root = new TicTacToeNode(rootState);
    }

    private Node<TicTacToe> root;
    private static final int SIMULATIONS = 100000;

    public State<TicTacToe> runMCTS(State<TicTacToe> rootState){
        Node<TicTacToe> rootNode = new TicTacToeNode(rootState);
        for(int i=0; i<SIMULATIONS; i++){
            Node<TicTacToe> selected = select(rootNode);
            Node<TicTacToe> expanded = expand(selected);
            double result = simulate(expanded);
            backpropagate(expanded, result);
        }
        return bestChild(rootNode).state();
    }
    public State<TicTacToe> runMCTS() {
        for (int i = 0; i < SIMULATIONS; i++) {
            Node<TicTacToe> selected = select(root);
            Node<TicTacToe> expanded = expand(selected);
            double result = simulate(expanded);
            backpropagate(expanded, result);
        }
        Node<TicTacToe> best = bestChild(root);
        root = best; // update root to best child
        return best.state();
    }

    // === UPDATE ROOT AFTER PLAYER MOVE ===
    public void updateRoot(State<TicTacToe> newState) {
        for (Node<TicTacToe> child : root.children()) {
            if (child.state().equals(newState)) {
                root = child;
                return;
            }
        }
        // If no match, rebuild from scratch
        root = new TicTacToeNode(newState);
    }

    private Node<TicTacToe> select(Node<TicTacToe> node){
        while (!node.isLeaf() && !node.children().isEmpty()){
            node = bestUCT(node);
        }
        return node;
    }
    private Node<TicTacToe> expand(Node<TicTacToe> node){
        if(node.isLeaf())
            return node;
        for(Move<TicTacToe> move : node.state().moves(node.state().player())){
            State<TicTacToe> newState = node.state().next(move);
            //TicTacToeNode newNode = new TicTacToeNode(newState);
            boolean alreadyExists = node.children().stream()
                    .anyMatch(child -> child.state().equals(newState));
            if (!alreadyExists) {
                //TicTacToeNode newNode = new TicTacToeNode(newState);
                return ((TicTacToeNode) node).addChildAndReturn(newState);// adds and returns the same node
            }
        }
        return node;
    }
    private double simulate(Node<TicTacToe> node){
        State<TicTacToe> state = node.state();
        final int intialPlayer = state.player();
        int currentPlayer = intialPlayer;
        while(!state.isTerminal()){
            List<Move<TicTacToe>> moves = new ArrayList<>(state.moves(currentPlayer));
            if(moves.isEmpty()) break;
            //Move<TicTacToe> move = moves.iterator().next();
            Collections.shuffle(moves);
            Move<TicTacToe> move = moves.get(0);
            state = state.next(move);
            currentPlayer = 1- currentPlayer;

        }
        Optional<Integer> winner = state.winner();
        return winner.map(w->w == intialPlayer ? 1.0 : 0.0). orElse(0.5);
    }
    private void backpropagate(Node<TicTacToe> node, double result){
        /*while(node!=null){
            node.backPropagate();
            break;
        }*/
        if (node instanceof TicTacToeNode tNode) {
            tNode.backPropagate(result);
        }
    }
    private Node<TicTacToe> bestUCT(Node<TicTacToe> node){
        /*return node.children().stream().max(Comparator.comparingDouble(
                child->(child.wins()/(double) child.playouts())+Math.sqrt(2*Math.log(node.playouts()+1)/(child.playouts()+1))
        )).orElseThrow();*/
        TicTacToeNode parent = (TicTacToeNode) node;
        return node.children().stream().max(Comparator.comparingDouble(
                child -> {
                    TicTacToeNode c = (TicTacToeNode) child;
                    double exploitation = (c.wins() / (double) (c.getVisits() + 1e-6));
                    double exploration = Math.sqrt(2 * Math.log(parent.getVisits() + 1) / (c.getVisits() + 1e-6));
                    return exploitation + exploration;
                }
        )).orElseThrow();
    }
    private Node<TicTacToe> bestChild(Node<TicTacToe> node){
        return node.children().stream().max(Comparator.comparingInt(Node::wins)).orElseThrow();

    }
}