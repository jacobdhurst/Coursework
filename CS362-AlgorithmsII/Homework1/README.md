<snippet>
  <content><![CDATA[
# ${1:Nim}
The game of Nim is a subtraction based game in which players take turns removing m marbles from a pile of size n until a player cannot make any further subtractions from the pile. 
The last player to remove marbles wins. An initial pilesize n and a set of valid moves m (1 < m <= N) are needed to play the game.

Typically, the game is played with more than one pile. This program is a variant one pile game with additional modes described below.
## Installation
To get started, download Nim.py and run 'python Nim.py'.
## Usage
There are 5 modes the user can choose from - detailed below:
1. Detect position winning status, returns True if player can win given pilesize N and the moveset
2. Generate a table of winning statuses from 0 up to pilesize N for the given moveset.
3. Play a game of Nim against the computer with the given pilesize N and the moveset.
4. Detect the periodicity of winning statuses for a set of moves and arbitrary pilesizes N.
5. Compute a subset of moves = (1, N) that maximizes period - input: moves as lower & upper bounds.
]]></content>
  <tabTrigger>readme</tabTrigger>
</snippet>
