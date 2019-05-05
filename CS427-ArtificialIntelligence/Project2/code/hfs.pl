%%%%% operations for state records %%%%%%%
    %
    % These predicates define state records as an adt
    % A state is just a [State, Parent, G_value, H_value, F_value] tuple.
    % Note that this predicate is both a generator and
    % a destructor of records, depending on what is bound
    % precedes is required by the priority queue algorithms

:- [abstract_types]. /* consults (reconsults) file containing the
	                various ADTs (Stack, Queue, etc.) */

state_record(State, Parent, G, H, F, [State, Parent, G, H, F]).
precedes([_,_,_,_,F1], [_,_,_,_,F2]) :- F1 =< F2.   

    % go initializes Open and CLosed and calls hfs_path 
hfs(Start, Goal, Type) :- 
    switch(Type, [
        bw : consult(black_white),
        vw : consult(vamp_wolf)]),
      
    empty_set(Closed),
    empty_sort_queue(Empty_open),
    heuristic(Start, Goal, H),
    state_record(Start, nil, 0, H, H, First_record),
    insert_sort_queue(First_record, Empty_open, Open),
    hfs_path(Open, Closed, Goal).

    % hfs_path performs a best first search,
    % maintaining Open as a priority queue, and Closed as
    % a set.
    
    % Open is empty; no solution found
hfs_path(Open,_,_) :- 
    empty_sort_queue(Open),
    write("Graph searched, no solution found"), nl.

    % The next record is a goal
    % Print out the list of visited states
hfs_path(Open, Closed, Goal) :- 
    remove_sort_queue(First_record, Open, _),
    state_record(State, _, _, _, _, First_record),
    State = Goal,
    write('Solution hfs_path is: '), nl,
    hfs_print(First_record, Closed).
    
    % The next record is not equal to the goal
    % Generate its children, add to open and continue
    % Note that bagof in AAIS prolog fails if its goal fails, 
    % I needed to use the or to make it return an empty list in this case
hfs_path(Open, Closed, Goal) :- 
    remove_sort_queue(First_record, Open, Rest_of_open),
    (bagof(Child, moves(First_record, Open, Closed, Child, Goal), Children);Children = []),
    insert_list(Children, Rest_of_open, New_open),
    add_to_set(First_record, Closed, New_closed),
    hfs_path(New_open, New_closed, Goal),!.
    
    % moves generates all children of a state that are not already on
    % open or closed.  The only wierd thing here is the construction
    % of a state record, test, that has unbound variables in all positions
    % except the state.  It is used to see if the next state matches
    % something already on open or closed, irrespective of that states parent
    % or other attributes
    % Also, Ive commented out unsafe since the way Ive coded the water jugs 
    % problem I dont really need it.
moves(State_record, Open, Closed,Child, Goal) :-
    state_record(State, _, G, _,_, State_record),
    move(State, Next),
    state_record(Next, _, _, _, _, Test),
    not(member_sort_queue(Test, Open)),
    not(member_set(Test, Closed)),
    cost(State, Next, C),
    G_new is G + C,
    heuristic(Next, Goal, H),
    F is G_new + H,
    state_record(Next, State, G_new, H, F, Child).
    
    %insert_list inserts a list of states obtained from a  call to
    % bagof and  inserts them in a priotrity queue, one at a time
insert_list([], L, L).
insert_list([State | Tail], L, New_L) :-
    insert_sort_queue(State, L, L2),
    insert_list(Tail, L2, New_L).

    % hfs_print prints out the solution hfs_path by tracing
    % back through the states on closed using parent links.
hfs_print(Next_record, _):-  
    state_record(State, nil, _, _,_, Next_record),
    write(State), nl.
hfs_print(Next_record, Closed) :-
    state_record(State, Parent, _, _,_, Next_record),
    state_record(Parent, _, _, _, _, Parent_record),
    member_set(Parent_record, Closed),
    hfs_print(Parent_record, Closed),
    write(State), nl.
