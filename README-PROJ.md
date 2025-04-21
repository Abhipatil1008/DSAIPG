This project demonstrates the implementation of Monte Carlo Tree Search(MCTS) for two classic games:
1. Tic Tac Toe(AI vs Human)
2. Priests and Devils(Single-player puzzle-solving)

Developed as part of the DSAIPG course project, this work explores how MCTS can be used to simulate decision-making uncertainty using a tree search strategy based on random sampling.

Game 1: Tic Tac Toe
-> Two-player turn-based game on 3*3 grid.
-> Human plays as X, AI plays as O.
-> Win by forming a horizontal, vertical, or diagonal line of 3 same symbols.
-> MCTS runs thousands of simulations to pick the best move each turn.

To play Tic Tac Toe execute TicTacToe.java

Game 2: Priests and Devils
-> 3 Priests and 3 Devils must cross a river using a boat.
-> At no point can devils outnumber priests on either bank.
-> The boat holds 1 or 2 characters.
-> Goal is to move all safely to the other side.
-> MCTS explores safe sequences through simulation and selection.

To play Priests and Devils execute PlayGame.java
To see how AI plays the game execute PriestDevilGame.java

What is Monte Carlo Tree Search(MCTS)?

MCTS is a search algorithm for decision processes:
1. Selection: Traverse tree using UCT(Upper Confidence Bound applied to Trees).
2. Expansion: Add a new child node from unexplored moves.
3. Simulation: Simulate random playouts from the new node.
4. Backpropagation: Propagate result back up the tree to improve estimates.

Github - https://github.com/Abhipatil1008/DSAIPG/tree/project-main
Demo Recording - https://northeastern-my.sharepoint.com/:v:/g/personal/patil_abhishek_northeastern_edu/EdmWSMAazlNFu0Ntmk2PLCgB2HHOUrkZI9Ma0UfKCtVd3w?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=DsDr0r


