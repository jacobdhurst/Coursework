:- [abstract_types]. /* consults (reconsults) file containing the
                        various ADTs (Stack, Queue, etc.) */

dfs(Start, Goal, Type) :-
    switch(Type, [
        bw : consult(black_white),
        vw : consult(vamp_wolf)]),
      
	empty_stack(Empty_been_list),
	stack(Start, Empty_been_list, Been_list),
	dfs_path(Start, Goal, Been_list).
	
	% dfs_path implements a depth first search in PROLOG
	
	% Current state = goal, print out been list
dfs_path(Goal, Goal, Been_list) :-
    write('Solution dfs_path is: '), nl,
	dfs_print(Been_list).
	
dfs_path(State, Goal, Been_list) :-
	move(State, Next),
	% not(unsafe(Next)),
	not(member_stack(Next, Been_list)),
	stack(Next, Been_list, New_been_list),
	dfs_path(Next, Goal, New_been_list), !.
	
dfs_print(S) :-
	empty_stack(S).
dfs_print(S) :-
	stack(E, Rest, S),
	dfs_print(Rest),
	write(E), nl.
