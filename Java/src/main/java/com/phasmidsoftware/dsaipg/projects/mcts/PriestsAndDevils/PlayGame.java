package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import java.util.Scanner;

public class PlayGame {

    public static void main(String[] args) {
        GameState state = GameState.initialState();
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎮 Welcome to Priests and Devils - Human Mode");
        state.render();

        while (!state.isGoal()) {
            System.out.println("Enter number of Priests to move (0–2): ");
            int p = scanner.nextInt();

            System.out.println("Enter number of Devils to move (0–2): ");
            int d = scanner.nextInt();

            GameState newState = state.move(p, d);

            if (newState == null || !newState.isValid()) {
                System.out.println("❌ Invalid move! Try again.");
                continue;
            }

            state = newState;
            state.render();

            if (state.isGoal()) {
                System.out.println("🎉 Congrats! You solved the puzzle!");
                break;
            }
        }

        scanner.close();
    }
}

