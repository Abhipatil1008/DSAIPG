package com.phasmidsoftware.dsaipg.projects.mcts.PriestsAndDevils;

import java.util.Objects;

public class GameState {
    public final int priestsLeft;
    public final int devilsLeft;
    public final int priestsRight;
    public final int devilsRight;
    public final boolean boatOnLeft; // true = boat on left bank

    public GameState(int priestsLeft, int devilsLeft, int priestsRight, int devilsRight, boolean boatOnLeft) {
        this.priestsLeft = priestsLeft;
        this.devilsLeft = devilsLeft;
        this.priestsRight = priestsRight;
        this.devilsRight = devilsRight;
        this.boatOnLeft = boatOnLeft;
    }


    public static GameState initialState() {
        return new GameState(3, 3, 0, 0, true);
    }


    public boolean isGoal() {
        //System.out.println("Called from: " + Thread.currentThread().getStackTrace()[2]);
        boolean goal = priestsLeft == 0 && devilsLeft == 0 && priestsRight == 3 && devilsRight == 3 && !boatOnLeft;
        if (goal) {
            System.out.println("Goal condition met!");
        } else {
            //System.out.println("Current state: "
            //        + "Left(" + priestsLeft + "P " + devilsLeft + "D), "
            //        + "Right(" + priestsRight + "P " + devilsRight + "D)");
        }
        return goal;
    }



    public boolean isValid() {
        if (priestsLeft < 0 || devilsLeft < 0 || priestsRight < 0 || devilsRight < 0) return false;
        if (priestsLeft > 0 && devilsLeft > priestsLeft) return false;
        if (priestsRight > 0 && devilsRight > priestsRight) return false;
        return true;
    }


    public GameState move(int priestsToMove, int devilsToMove) {
        if (priestsToMove + devilsToMove < 1 || priestsToMove + devilsToMove > 2) {
            return null; // Boat must carry 1 or 2 passengers
        }

        if (boatOnLeft) {
            return new GameState(
                    priestsLeft - priestsToMove,
                    devilsLeft - devilsToMove,
                    priestsRight + priestsToMove,
                    devilsRight + devilsToMove,
                    false
            );
        } else {
            return new GameState(
                    priestsLeft + priestsToMove,
                    devilsLeft + devilsToMove,
                    priestsRight - priestsToMove,
                    devilsRight - devilsToMove,
                    true
            );
        }
    }

    public void render() {
        String left = "[" + "P".repeat(priestsLeft) + "D".repeat(devilsLeft) + "]";
        String right = "[" + "P".repeat(priestsRight) + "D".repeat(devilsRight) + "]";
        String boat = boatOnLeft ? "<<< [BOAT]           " : "           [BOAT] >>>";
        System.out.println("Left Bank       Boat             Right Bank");
        System.out.printf("%-15s %s %-15s\n", left, boat, right);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GameState)) return false;
        GameState other = (GameState) o;
        return this.priestsLeft == other.priestsLeft &&
                this.devilsLeft == other.devilsLeft &&
                this.priestsRight == other.priestsRight &&
                this.devilsRight == other.devilsRight &&
                this.boatOnLeft == other.boatOnLeft;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priestsLeft, devilsLeft, priestsRight, devilsRight, boatOnLeft);
    }
    @Override
    public String toString() {
        return String.format("[L: %dP %dD | R: %dP %dD | Boat: %s]",
                priestsLeft, devilsLeft, priestsRight, devilsRight,
                boatOnLeft ? "Left" : "Right");
    }

}

