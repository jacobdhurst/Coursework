% All moves are safe

% Determines cost by subtracting indices of 'e'
cost(State1, State2, C) :-
    indexOf(State1, e, A),
    indexOf(State2, e, B),
	(A >= B -> C is (A-B); C is (B-A)).

% Helper predicate for cost, locates index of element
indexOf([Element|_], Element, 0).
indexOf([_|Tail], Element, Index):-
  indexOf(Tail, Element, Index1),
  Index is Index1+1.

% Blank leftmost moves
move([e,A,B,C,D,E,F], [A,e,B,C,D,E,F]).
move([e,A,B,C,D,E,F], [B,A,e,C,D,E,F]).
move([e,A,B,C,D,E,F], [C,A,B,e,D,E,F]).

% Blank leftmost+1 moves
move([A,e,B,C,D,E,F], [e,A,B,C,D,E,F]).
move([A,e,B,C,D,E,F], [A,B,e,C,D,E,F]).
move([A,e,B,C,D,E,F], [A,C,B,e,D,E,F]).
move([A,e,B,C,D,E,F], [A,D,B,C,e,E,F]).

% Blank leftmost+2 moves
move([A,B,e,C,D,E,F], [e,B,A,C,D,E,F]).
move([A,B,e,C,D,E,F], [A,e,B,C,D,E,F]).
move([A,B,e,C,D,E,F], [A,B,C,e,D,E,F]).
move([A,B,e,C,D,E,F], [A,B,D,C,e,E,F]).
move([A,B,e,C,D,E,F], [A,B,E,C,D,e,F]).

% Blank in middle moves
move([A,B,C,e,D,E,F], [e,B,C,A,D,E,F]).
move([A,B,C,e,D,E,F], [A,e,C,B,D,E,F]).
move([A,B,C,e,D,E,F], [A,B,e,C,D,E,F]).
move([A,B,C,e,D,E,F], [A,B,C,D,e,E,F]).
move([A,B,C,e,D,E,F], [A,B,C,E,D,e,F]).
move([A,B,C,e,D,E,F], [A,B,C,F,D,E,e]).

% Blank rightmost-2 moves
move([A,B,C,D,e,E,F], [A,e,C,D,B,E,F]).
move([A,B,C,D,e,E,F], [A,B,e,D,C,E,F]).
move([A,B,C,D,e,E,F], [A,B,C,e,D,E,F]).
move([A,B,C,D,e,E,F], [A,B,C,D,E,e,F]).
move([A,B,C,D,e,E,F], [A,B,C,D,F,E,e]).

% Blank rightmost-1 moves
move([A,B,C,D,E,e,F], [A,B,e,D,E,C,F]).
move([A,B,C,D,E,e,F], [A,B,C,e,E,D,F]).
move([A,B,C,D,E,e,F], [A,B,C,D,e,E,F]).
move([A,B,C,D,E,e,F], [A,B,C,D,E,F,e]).

% Blank rightmost moves
move([A,B,C,D,E,F,e], [A,B,C,e,E,F,D]).
move([A,B,C,D,E,F,e], [A,B,C,D,e,F,E]).
move([A,B,C,D,E,F,e], [A,B,C,D,E,e,F]).

% Heuristic counts the number of white tiles to the right of each black tile.
heuristic(_, [A,B,C|_], H) :-
    H is 0,
    %(condition -> then_clause; else_clause),
    (A \= w -> H is H+3; H is H),
	(B \= w -> H is H+2; H is H),
	(C \= w -> H is H+1; H is H).