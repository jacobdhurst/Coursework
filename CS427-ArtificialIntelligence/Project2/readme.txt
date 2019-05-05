Author:
    Jacob Hurst
    Jhurst@cs.unm.edu

Description:
    The following is a general search-based problem solver written in prolog. The code uses bfs, dfs, and
    hfs methods by George Luger to search state spaces of the black & white tile problem & the vampires
    & werewolves problem.
    
    Search algorithms are contained in dfs.pl, bfs.pl, hfs.pl
    Data Structures are contained in abstract_types.pl (I've added a switch structure to Lugers code).
    The program driver is contained in project2.pl, this simplifies running and testing the program.
    The state representations for the specified problems are contained in black_white.pl and vamp_wolf.pl
    In each of these, I've enumerated the possible moves, defined cost functions, heuristic functions and various helper functions.

Usage: 
    Navigate to code folder.
    Start prolog.
    Load module 'project2.pl' by typing '[project2]' in the prolog interpreter.
    Run all 6 (Puzzle, Strategy) variants by typing 'run.' in the prolog interpreter.
