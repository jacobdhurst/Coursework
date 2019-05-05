% Move is safe if vampires are not outnumbered
safe(WE,VE,WW,VW) :-
	VE>=0, WE>=0, VW>=0, WW>=0,
	(VE>=WE ; VE=0),
	(VW>=WW ; VW=0).

% Cost is always 1, back and forth with boat
cost(_, _, C) :-
    C is 1.

% Moves include; moving 1 vampire east/west, moving 1 werewolf east/west,
% moving 2 vampires east/west, moving 2 werewolves east/west. and moving 1 vampire and 1 werewolf east/west.
move([WE,VE,WW,VW,east],[WE,VE2,WW,VW2,west]):-
	VW2 is VW+2,
	VE2 is VE-2,
	safe(WE,VE2,WW,VW2).
move([WE,VE,WW,VW,east],[WE2,VE,WW2,VW,west]):-
	WW2 is WW+2,
	WE2 is WE-2,
	safe(WE2,VE,WW2,VW).
move([WE,VE,WW,VW,east],[WE2,VE2,WW2,VW2,west]):-
	WW2 is WW+1,
	WE2 is WE-1,
	VW2 is VW+1,
	VE2 is VE-1,
	safe(WE2,VE2,WW2,VW2).
move([WE,VE,WW,VW,east],[WE,VE2,WW,VW2,west]):-
	VW2 is VW+1,
	VE2 is VE-1,
	safe(WE,VE2,WW,VW2).
move([WE,VE,WW,VW,east],[WE2,VE,WW2,VW,west]):-
	WW2 is WW+1,
	WE2 is WE-1,
	safe(WE2,VE,WW2,VW).
move([WE,VE,WW,VW,west],[WE,VE2,WW,VW2,east]):-
	VW2 is VW-2,
	VE2 is VE+2,
	safe(WE,VE2,WW,VW2).
move([WE,VE,WW,VW,west],[WE2,VE,WW2,VW,east]):-
	WW2 is WW-2,
	WE2 is WE+2,
	safe(WE2,VE,WW2,VW).
move([WE,VE,WW,VW,west],[WE2,VE2,WW2,VW2,east]):-
	WW2 is WW-1,
	WE2 is WE+1,
	VW2 is VW-1,
	VE2 is VE+1,
	safe(WE2,VE2,WW2,VW2).
move([WE,VE,WW,VW,west],[WE,VE2,WW,VW2,east]):-
	VW2 is VW-1,
	VE2 is VE+1,
	safe(WE,VE2,WW,VW2).
move([WE,VE,WW,VW,west],[WE2,VE,WW2,VW,east]):-
	WW2 is WW-1,
	WE2 is WE+1,
	safe(WE2,VE,WW2,VW).

% Heuristic observes final counts of werewolves and vampires on the east by capacity of boat.
heuristic(_, [WE,VE|_], H) :-
    H is (WE+VE)/2.