package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

public class TicTacToe implements Game<TicTacToe> {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;
        while (playAgain) {
            TicTacToe game = new TicTacToe();
            State<TicTacToe> state = game.start();
            MCTS mcts = new MCTS(state);
            int player = game.opener();

            while (!state.isTerminal()) {
                TicTacToe.TicTacToeState currentState = (TicTacToe.TicTacToeState) state;
                System.out.println("Current board:\n" + currentState.position().render());

                if (player == X) {
                    System.out.println("Your move (row and column (enter as two numbers with space, e.g., 0 1):): ");
                    String[] input = scanner.nextLine().trim().split("\\s+");
                    try {
                        int row = Integer.parseInt(input[0]);
                        int col = Integer.parseInt(input[1]);
                        state = state.next(new TicTacToeMove(player, row, col));
                        mcts.updateRoot(state);
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                        continue;
                    }
                } else {
                    // ✅ 1. Try to win if possible
                    State<TicTacToe> winningMove = TicTacToe.findImmediateWin(state);
                    if (winningMove != null) {
                        System.out.println("AI played winning move!");
                        state = winningMove;
                        mcts.updateRoot(state);
                        player = 1 - player;
                        continue;
                    }

                    // ✅ 2. Try to block
                    State<TicTacToe> blockingMove = TicTacToe.blockImmediateWin(state);
                    if (blockingMove != null) {
                        System.out.println("AI blocked your winning move!");
                        state = blockingMove;
                        mcts.updateRoot(state);
                        player = 1 - player;
                        continue;
                    }

                    // ✅ 3. Fallback to MCTS
                    state = mcts.runMCTS();
                    System.out.println("AI played:");
                }

                player = 1 - player;
            }

            System.out.println("Final board:\n" + ((TicTacToe.TicTacToeState) state).position().render());
            if (state.winner().isPresent())
                System.out.println("Winner is: " + (state.winner().get() == X ? "You (X)" : "AI (O)"));
            else
                System.out.println("It's a draw!");

            System.out.print("Do you want to play another game? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            playAgain = answer.equals("y");
        }

        System.out.println("Thanks for playing!");
    }

    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;

    static Position startingPosition() {
        return Position.parsePosition(". . .\n. . .\n. . .", blank);
    }

    State<TicTacToe> runGame() {
        State<TicTacToe> state = start();
        int player = opener();
        while (!state.isTerminal()) {
            state = state.next(state.chooseMove(player));
            player = 1 - player;
        }
        return state;
    }

    public int opener() {
        return X;
    }

    public State<TicTacToe> start() {
        return new TicTacToeState();
    }

    public TicTacToe(Random random) {
        this.random = random;
    }

    public TicTacToe(long seed) {
        this(new Random(seed));
    }

    public TicTacToe() {
        this(System.currentTimeMillis());
    }

    private final Random random;

    static class TicTacToeMove implements Move<TicTacToe> {
        public int player() {
            return player;
        }

        public TicTacToeMove(int player, int i, int j) {
            this.player = player;
            this.i = i;
            this.j = j;
        }

        public int[] move() {
            return new int[]{i, j};
        }

        private final int player;
        private final int i;
        private final int j;
    }

    class TicTacToeState implements State<TicTacToe> {
        public TicTacToe game() {
            return TicTacToe.this;
        }

        public int player() {
            return switch (position.last) {
                case 0, -1 -> X;
                case 1 -> O;
                default -> blank;
            };
        }

        public Position position() {
            return this.position;
        }

        public Optional<Integer> winner() {
            return position.winner();
        }

        public Random random() {
            return random;
        }

        public Collection<Move<TicTacToe>> moves(int player) {
            if (player == position.last) throw new RuntimeException("consecutive moves by same player: " + player);
            List<int[]> moves = position.moves(player);
            ArrayList<Move<TicTacToe>> list = new ArrayList<>();
            for (int[] coordinates : moves)
                list.add(new TicTacToeMove(player, coordinates[0], coordinates[1]));
            return list;
        }

        public State<TicTacToe> next(Move<TicTacToe> move) {
            TicTacToeMove ticTacToeMove = (TicTacToeMove) move;
            int[] ints = ticTacToeMove.move();
            return new TicTacToeState(position.move(move.player(), ints[0], ints[1]));
        }

        public boolean isTerminal() {
            return position.full() || position.winner().isPresent();
        }

        @Override
        public String toString() {
            return "TicTacToe{\n" + position + "\n}";
        }

        public TicTacToeState(Position position) {
            this.position = position;
        }

        public TicTacToeState() {
            this(startingPosition());
        }

        private final Position position;
    }

    // ✅ AI tries to block human's winning move
    public static State<TicTacToe> blockImmediateWin(State<TicTacToe> state) {
        int aiPlayer = state.player();
        int humanPlayer = 1 - aiPlayer;

        Position currentPos = ((TicTacToeState) state).position();
        Position simulated = new Position(currentPos.toMatrix(), currentPos.count(), aiPlayer);
        TicTacToeState fakeState = new TicTacToe().new TicTacToeState(simulated);

        Collection<Move<TicTacToe>> humanMoves = fakeState.moves(humanPlayer);
        Set<State<TicTacToe>> winningStates = new HashSet<>();
        for (Move<TicTacToe> move : humanMoves) {
            State<TicTacToe> result = fakeState.next(move);
            if (result.winner().isPresent() && result.winner().get() == humanPlayer)
                winningStates.add(result);
        }

        if (winningStates.isEmpty()) return null;

        for (Move<TicTacToe> aiMove : state.moves(aiPlayer)) {
            State<TicTacToe> result = state.next(aiMove);
            for (State<TicTacToe> danger : winningStates) {
                if (result.equals(danger)) return result;
            }
        }

        return null;
    }

    // ✅ AI tries to win immediately
    public static State<TicTacToe> findImmediateWin(State<TicTacToe> state) {
        int aiPlayer = state.player();
        for (Move<TicTacToe> move : state.moves(aiPlayer)) {
            State<TicTacToe> result = state.next(move);
            if (result.winner().isPresent() && result.winner().get() == aiPlayer) {
                return result;
            }
        }
        return null;
    }
}
