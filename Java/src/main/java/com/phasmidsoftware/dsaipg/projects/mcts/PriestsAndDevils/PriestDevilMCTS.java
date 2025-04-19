package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

/**
 * Monte Carlo Tree Search implementation for the Priests & Devils game.
 */
public class PriestDevilMCTS {

    public Node<PriestDevilGame> root;
    private static final int SIMULATIONS = 10000;
    private final int simulations;

    public PriestDevilMCTS(Node<PriestDevilGame> root) {
        this(root, 10000);
    }
    public PriestDevilMCTS(Node<PriestDevilGame> root, int simulations) {
        this.root = root;
        this.simulations = simulations;
    }


    public State<PriestDevilGame> runMCTS(State<PriestDevilGame> rootState) {
        Node<PriestDevilGame> rootNode = this.root;

        GameState currentState = ((MCTPriestDevilState) rootState).unwrap();
        if (currentState.isGoal()) {
            //System.out.println("Already at goal. No search needed.");
            return rootState;
        }

        for (int i = 0; i < simulations; i++) {
            Node<PriestDevilGame> selected = select(rootNode);
            Node<PriestDevilGame> expanded = expand(selected);

            GameState expandedState = ((MCTPriestDevilState) expanded.state()).unwrap();
            if (expandedState.isGoal()) {
                //System.out.println("Found goal during expansion. Returning early.");
                root = expanded;
                return expanded.state();
            }

            double result = simulate(expanded);
            backpropagate(expanded, result);
        }
        //System.out.println("Number of children of root: " + rootNode.children().size());
        // Check all children of root for GOAL state
        //System.out.println("Checking for GOAL state among children...");
        for (Node<PriestDevilGame> child : rootNode.children()) {
            GameState gs = ((MCTPriestDevilState) child.state()).unwrap();
            //System.out.println("  Child: " + gs + " | Goal? " + gs.isGoal() + " | Hash: " + gs.hashCode());
            if (gs.isGoal()) {
                //System.out.println("MCTS found GOAL state as child! Returning it to main.");
                root = child;                      // Set correct root
                return child.state();              // Return that state to main
            }
        }



        // 🛠 Fallback: Best child by win rate
        List<Node<PriestDevilGame>> children = new ArrayList<>(rootNode.children());
        if (children.isEmpty()) {
            //throw new RuntimeException("No children to choose from.");
        }

        Node<PriestDevilGame> best = children.stream()
                .filter(c -> !((MCTPriestDevilState)c.state()).unwrap().equals(GameState.initialState()))
                .max(Comparator.comparingDouble(n ->
                        n.playouts() == 0 ? 0.0 : (double) n.wins() / n.playouts()))
                .orElse(children.get(0)); // fallback even if all are initial state


        GameState bestState = ((MCTPriestDevilState) best.state()).unwrap();
        //System.out.println("Fallback best child: " + bestState);
        //if (bestState.isGoal()) {
            //System.out.println("Fallback best child IS the goal.");
        //}

        root = new PriestDevilNode(best.state()); // ensure a fresh node
        return root.state();                      // root and returned state are always in sync
    }





    private Node<PriestDevilGame> select(Node<PriestDevilGame> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = bestUCT(node);
        }
        return node;
    }

    private Node<PriestDevilGame> expand(Node<PriestDevilGame> node) {
        Collection<Move<PriestDevilGame>> possibleMoves = node.state().moves(node.state().player());
        if (possibleMoves.isEmpty()) {
            //System.out.println("No moves found for this state during expansion!");
            return node;
        }
        List<Move<PriestDevilGame>> moveList = new ArrayList<>(possibleMoves);
        Collections.shuffle(moveList);  // 💡 shuffle to avoid picking same move every time
        Set<GameState> visited = new HashSet<>();
        for (Move<PriestDevilGame> move : moveList) {
            System.out.println("\nTrying move: " + move);
            State<PriestDevilGame> newState = node.state().next(move);

            if (newState == null) {
                //System.out.println("Move resulted in null state: " + move);
                continue;
            }

            GameState raw = ((MCTPriestDevilState) newState).unwrap();

            if (!raw.isValid() || visited.contains(raw)) {
                //System.out.println("Invalid or already visited state: " + raw);
                continue;
            }

            visited.add(raw); // only mark visited if it's valid

            if (raw.isGoal()) {
                PriestDevilNode child = new PriestDevilNode(newState);
                node.children().add(child);
                //System.out.println(" Expanded GOAL state: " + raw);
                raw.render();
                return child;  // Early return if goal is found
            }

            boolean exists = node.children().stream().anyMatch(c -> c.state().equals(newState));
            if (!exists) {
                PriestDevilNode child = new PriestDevilNode(newState);
                node.children().add(child);

                //  Extra log if this is a goal state
                //if (raw.isGoal()) {
                    //System.out.println("GOAL state added to children: " + raw);
                //}

                //System.out.println("Expanded with move: " + move);
                raw.render();

                // Return first valid child as before
                return child;
            }
        }

        return node; // No expansion happened, return same node
    }





    private double simulate(Node<PriestDevilGame> node) {
        State<PriestDevilGame> state = node.state();
        int initialPlayer = state.player();
        int currentPlayer = initialPlayer;

        Set<GameState> visitedStates = new HashSet<>();
        int maxSteps = 50;
        int steps = 0;

        while (!state.isTerminal() && steps++ < maxSteps) {
            GameState current = ((MCTPriestDevilState) state).unwrap();
            if (current.isGoal()) return 1.0;

            // Detect loops
            if (visitedStates.contains(current)) {
                //System.out.println("Detected loop during simulation.");
                break;
            }
            visitedStates.add(current);

            List<Move<PriestDevilGame>> moves = new ArrayList<>(state.moves(currentPlayer));
            if (moves.isEmpty()) break;

            Collections.shuffle(moves);
            Move<PriestDevilGame> move = moves.get(0);
            state = state.next(move);
            currentPlayer = 1 - currentPlayer;
        }

        Optional<Integer> winner = state.winner();
        return winner.map(w -> w == initialPlayer ? 1.0 : 0.0).orElse(0.5);
    }

    private void backpropagate(Node<PriestDevilGame> node, double result) {
        if (((MCTPriestDevilState) node.state()).unwrap().isGoal()) {
            result = 1.0;
        }
        if (node instanceof PriestDevilNode) {
            ((PriestDevilNode) node).backPropagate(result);
        }
    }

    private Node<PriestDevilGame> bestUCT(Node<PriestDevilGame> node) {
        Random rand = new Random();
        List<Node<PriestDevilGame>> children = new ArrayList<>(node.children());
        Collections.shuffle(children); // helps break ties randomly

        return children.stream().max(Comparator.comparingDouble(
                c -> (c.wins() / (double) (c.playouts() + 1e-6)) +
                        Math.sqrt(2 * Math.log(node.playouts() + 1) / (c.playouts() + 1e-6))
        )).orElseThrow();
    }


    private Node<PriestDevilGame> bestChild(Node<PriestDevilGame> node) {
        return node.children().stream()
                .max(Comparator.comparingDouble(n -> n.playouts() == 0 ? 0 : n.wins() / n.playouts()))
                .orElseThrow(() -> new RuntimeException("MCTS could not find any valid children. Game may be stuck or state has no moves."));
    }

}
