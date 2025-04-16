package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils.PriestDevilMCTS;

import java.util.HashSet;
import java.util.Set;


public class PriestDevilGame implements Game<PriestDevilGame> {

    @Override
    public State<PriestDevilGame> start() {
        return new MCTPriestDevilState(GameState.initialState());
    }

    @Override
    public int opener() {
        return 0; // only one player
    }

    public static void main(String[] args) {
        MCTPriestDevilState startState = new MCTPriestDevilState(GameState.initialState());
        PriestDevilNode root = new PriestDevilNode(startState);

        PriestDevilMCTS mcts = new PriestDevilMCTS(root);
        System.out.println("Initial valid moves: " +
                MoveGenerator.generateNextStates(GameState.initialState()).size());

        System.out.println("Starting puzzle:");
        startState.unwrap().render();

        int step = 0;
        Set<GameState> visited = new HashSet<>();
        visited.add(startState.unwrap());

        while (true) {
            System.out.println("\nStep " + (++step) + ": Running MCTS...");
            State<PriestDevilGame> returnedState = mcts.runMCTS(startState);  // ✅ Use exact returned state
            MCTPriestDevilState nextState = (MCTPriestDevilState) returnedState;
            GameState unwrappedNext = nextState.unwrap();
            System.out.println("🧩 Main got state: " + unwrappedNext + " | isGoal? " + unwrappedNext.isGoal());
            System.out.println("🧩 Main loop got state: " + unwrappedNext + " | Hash: " + unwrappedNext.hashCode());

            if (unwrappedNext.isGoal()) {
                System.out.println("✅ Puzzle Solved!");
                unwrappedNext.render();
                break;
            }

            for (Node<PriestDevilGame> child : mcts.root.children()) {
                GameState childState = ((MCTPriestDevilState) child.state()).unwrap();
                System.out.println("   🔁 Child: " + childState + " | Hash: " + childState.hashCode());
                if (childState.equals(unwrappedNext)) {
                    System.out.println("🟩 Main's state == child's state: MATCH ✅");
                } else {
                    System.out.println("🟥 Main's state != child's state: ❌");
                }
            }

            int priestsMoved = Math.abs(startState.unwrap().priestsLeft - unwrappedNext.priestsLeft);
            int devilsMoved = Math.abs(startState.unwrap().devilsLeft - unwrappedNext.devilsLeft);
            System.out.println("🚣 Boat moved with: P: " + priestsMoved + ", D: " + devilsMoved);
            unwrappedNext.render();

            if (!unwrappedNext.isValid()) {
                System.out.println("❌ Priests eaten! Invalid state.");
                break;
            } else if (visited.contains(unwrappedNext)) {
                System.out.println("♻️ Loop detected. Terminating.");
                break;
            }

            visited.add(unwrappedNext);
            startState = (MCTPriestDevilState) mcts.root.state();

        }
    }






}


