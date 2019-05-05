# CS-362 HW4: Neural Network Nim Player
Nim is a mathematical game of strategy in which two players take turns removing objects from piles. The player that empties the last pile is declared the winner.
This neural network has been trained to play Nim. The network currently plays at 75% accuracy.

## Getting Started
* Code for the project can be found in the 'code' subdirectory.
* Logs and figures for the project can be found in the 'log' subdirectory.
* Discussion offers further insights on this project

## Usage
To run, 'cd' into the 'code' subdirectory and run 'python nim_network.py'.

The code will automatically load 'nim_network.pkl' which contains the training data.

User will be prompted to train or play a game. 
To train the network, type 'T' (to save and quit training progress 'Ctrl-C', to quit training progress 'Ctrl-Z').
To play a game, type 'P'. Then provide the starting pile sizes 'size_1, size_2, size_3'. The game will open on the computers turn and alternate thereafter.

### Author
Jacob Hurst
Jhurst@cs.unm.edu