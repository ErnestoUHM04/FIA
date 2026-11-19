% ---------- Red Semantica de la guia ---------

is_a(human_being, mammal).
is_a(cattle, mammal).

needs(mammal, food).

can(mammal, move).

are(cattle, herbivore).

is_an_instance_of(daisy, cattle).
is_an_instance_of(fred_jones, human_being).
is_an_instance_of(john_smith, human_being).

has_a(daisy, calf).
has_a(human_being, head).

hair_color(fred_jones, blonde).
hair_color(john_smith, black).


% ---------- En Frames ---------

frame(mammal, [needs(food), can(move)]).

frame(human_being, [is_a(mammal), has_a(head)]).

frame(cattle, [is_a(mammal), are(herbivore), has_a(calf)]).

frame(daisy, [is_an_instance_of(cattle), has_a(calf)]).

frame(fred_jones, [is_an_instance_of(human_being), hair_color(blonde)]).

frame(john_smith, [is_an_instance_of(human_being), hair_color(black)]).

