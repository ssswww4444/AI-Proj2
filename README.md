# AI-Proj2
COMP30024 Artificial Intelligence Project 2
(Wenqiang Kuang & Pei-Yun Sun)

In our design, minimax decision rule is used as the basic strategy, which always yields the player his best worst-case expectation. Alpha-beta pruning is used as well, to reduce the number of child nodes to explore in executing minimax algorithm. 

When minimax search tree reaches the bottom (depth limit or end of the game), an evaluation function is adopted to calculate board score for the player according to up-to-date board conditions. 

We are looking 4 features during the calculation of the score:

1. Number of steps that each piece of the player moved towards the destination
2. Whether or not the player's pieces are blocked by any barriers (Blocks or pieces)
3. How well is the player blocking the opponent’s pieces (the closer the better)
4. How many pieces have arrived at the destination (out of board)

Each of them is given a weight, which is initialised in a text file beforehand. The weights would be read from a corresponding file (e.g. d6h.txt for board with dimension 6 and player H, d5v for board with dimension 5 and player V) and used in evaluation.

To train our game-playing agent, we've applied machine learning as well. Temporal difference learning method, more precisely, TD leaf is adopted. The weights for all the features that affect the board score would be updated by the Class TDLeaf. At each state, bestScore and the value of the features for the next move of the player would be passed into TDLeaf. At the end of the game, these scores and features would serve to calculate the difference between estimated weights and pre-defined weights, and the weights in the file will be updated if our agent won. 

Temporal difference learning is a machine learning method applied to two-player game with minimax strategy. It would adjust weights for a more reliable prediction when subsequently predicting the probability of winning a game during the sequence of moves from the initial state until the end. Equations used in TDLeaf.java are taken from the lecture slides.

During our design, Simple.java and Naive.java are used as opponent agents to train our agent. 
