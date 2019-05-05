:- [abstract_types].
:- [bfs].
:- [dfs].
:- [hfs].

% Uses a switch defined in abstract_types to select strategy used
% then calls the selected strategy with the relevant information
solve(puzzle(Start, Goal), Strategy, Type) :- 
    switch(Strategy, [
        bfs : bfs(Start, Goal, Type),
        dfs : dfs(Start, Goal, Type),
        hfs : hfs(Start, Goal, Type)]).

% Program entry point, 'run.' will evaluate all cases.  
run :-
    BW = puzzle([b,b,b,e,w,w,w],[w,w,w,b,b,b,e]),
    VW = puzzle([3,3,0,0,east], [0,0,3,3,west]),
    
    write('BFS on Black-White Tile Puzzle:'), nl,
    solve(BW, bfs, bw), 
    nl, write('DFS on Black-White Tile Puzzle:'), nl,
    solve(BW, dfs, bw),
    nl, write('BestFS on Black-White Tile Puzzle:'), nl,
    solve(BW, hfs, bw),

    nl, write('BFS on Vamp-Wolf Puzzle:'), nl,
    solve(VW, bfs, vw),
    nl, write('DFS on Vamp-Wolf Puzzle:'), nl, 
    solve(VW, dfs, vw), 
    nl, write('BestFS on Vamp-Wolf Puzzle:'), nl,
    solve(VW, hfs, vw),
    nl, write('Done.'), nl.
