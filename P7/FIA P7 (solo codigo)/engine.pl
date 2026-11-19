% -----------------------   REGLAS DE INFERENCIA  --------------------------------------

% knowledge.pl trabaja de con esta estructura
% frame(Nombre, padre(Padre), Tipo).

% Encuentra todas las especies
todas_especies(L) :- findall(X, frame(X, _, especie), L).

% Obtiene la cadena taxonómica completa de una especie
% Ejemplo de uso: taxonomia_completa(plasmodium_falciparum, T).
taxonomia_completa(Especie, Taxonomia) :-
    frame(Especie, padre(Padre), _),
    Padre \= top,
    taxonomia_completa(Padre, T1),
    append(T1, [Especie], Taxonomia).

taxonomia_completa(Especie, [Especie]) :-
    frame(Especie, padre(top), _).

% Muestra todas las especies con su taxonomía completa
% Ejemplo de uso: mostrar_todas_especies_con_taxonomia.
mostrar_todas_especies_con_taxonomia :-
    todas_especies(Especies),
    mostrar_taxonomias(Especies).

mostrar_taxonomias([]).
mostrar_taxonomias([E|R]) :-
    taxonomia_completa(E, T),
    write(E), write(': '), write(T), nl,
    mostrar_taxonomias(R).

% Encuentra la descripción de una especie
% Ejemplo de uso: descripcion_especie(plasmodium_falciparum, Descripcion).
descripcion_especie(Especie, Descripcion):- descripcion(Especie, Descripcion).

