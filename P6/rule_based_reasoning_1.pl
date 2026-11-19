:- dynamic si/1,no/1.

/**************************************************
Implementacion de un pequeño sistema experto
***************************************************/

% Menú principal

main:-repeat,
      nl,nl,write('EXPERTO en Pokemones'),nl,
      nl,write('Menu'),nl,
      nl,write('1 Consulta al experto'),
      nl,write('2 Salir'),
      nl,nl,write('Indica tu opcion:'),
      read(Opcion),nl,
      ( (Opcion=1,motor,fail);
	(Opcion=2,!)).

% Motor de inferencia

motor:-borraResp,(deduce(Objeto,_) ->
      (nl,write('Infiero que tu pokemon es: '),write(Objeto),
       nl,writeln('\nLlegué a esa conclusión porque tu pokemon: '), findall(X, si(X), L), writeln(L));
      (nl,write('\nNo hay un pokemon con tales atributos en mi base de conocimientos'))).

satisface(Atributo,_) :-
   (si(Atributo)  -> true ;                %if   si(Atributo) then true
    (no(Atributo) -> fail ;		   %else if no(Atributo) then fail
                     pregunta(Atributo))). %else pregunta(Atributo)

pregunta(A) :-
    write('¿Tu pokemon '),
    write(A), write('?'),
    read(Resp),
    nl,
    ((Resp = s ; Resp = si)                 %if Resp = s or Resp = S then
      -> assert(si(A));                     %     assert(si(A))
         assert(no(A)), fail).		    %else assert(no(A))

borraResp:-retractall(si(_)),retractall(no(_)).

% Base de conocimiento
% Reglas del dominio

deduce(picachu,X)   :- picachu(X).
deduce(charmander,X) :- charmander(X).
deduce(bulbasaur,X)  :- bulbasaur(X).
deduce(squirtle,X) :- squirtle(X).

picachu(X) :-
    satisface(es_tipo_electrico,X),
    satisface(tiene_colita_poderosa,X),
    satisface(tiene_chapitas,X).

charmander(X):-
    satisface(es_tipo_fuego,X),
    satisface(parece_reptil,X),
    satisface(se_le_quema_la_cola,X).

bulbasaur(X):-
    satisface(es_tipo_planta,X),
    satisface(tiene_semilla_en_la_espalda,X),
    satisface(parece_batracio,X).

squirtle(X):-
    satisface(es_tipo_agua,X),
    satisface(tiene_caparazon,X),
    satisface(parece_tortuga,X).
