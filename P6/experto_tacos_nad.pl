% Sistema Experto en tacos
% Indicará al usuario qué tipo de taco es, de acuerdo a sus preferencias

:- dynamic si/1,no/1.

% Menu
menu:-repeat,
      nl,nl,write('EXPERTO en Tacos'),nl,
      nl,write('Menu'),nl,
      nl,write('1 Consulta al experto'),
      nl,write('2 Salir'),
      nl,nl,write('Indica tu opción:'),
      read(Opcion),nl,
      ( (Opcion=1,motor,fail);
	(Opcion=2,!)).

% Motor de inferencia
motor:- borraResp,(deduce(Objeto,_) ->
      (nl,write('Infiero que tu taco es: '),write(Objeto),
       nl,writeln('\nLlegué a esa conclusión porque tu taco: '), findall(X, si(X), L), writeln(L));
      (nl,write('\nNo hay un taco con tales atributos en mi base de conocimientos'))).

satisface(Atributo,_) :-
   (si(Atributo)  -> true ;                %if   si(Atributo) then true
    (no(Atributo) -> fail ;		   %else if no(Atributo) then fail
                     pregunta(Atributo))). %else pregunta(Atributo)

pregunta(A) :-
    write('Tu taco '),
    write(A), write('?'),
    read(Resp),
    nl,
    ((Resp = s ; Resp = si; Resp = y; Resp = yes)                 %if Resp = s or Resp = S then
      -> assert(si(A));                     %     assert(si(A))
         assert(no(A)), fail).		    %else assert(no(A))

borraResp:-retractall(si(_)),retractall(no(_)).

% Base de conocimiento
% Reglas del dominio

deduce(taco_al_pastor, X):- taco_al_pastor(X).
deduce(taco_de_canasta, X):- taco_de_canasta(X).
deduce(taco_de_suadero, X):- taco_de_suadero(X).
deduce(taco_de_bistec, X):- taco_de_bistec(X).
deduce(taco_de_chorizo, X):- taco_de_chorizo(X).
deduce(taco_campechano, X):- taco_campechano(X).
deduce(taco_de_asada, X):- taco_de_asada(X).
deduce(taco_de_cochinita, X):- taco_de_cochinita(X).
deduce(taco_de_guisado, X):- taco_de_guisado(X).
deduce(taco_de_lengua, X):- taco_de_lengua(X).
deduce(taco_de_alambre, X):- taco_de_alambre(X).
deduce(taco_arabe, X):- taco_arabe(X).
deduce(taco_de_carnitas, X):- taco_de_carnitas(X).
deduce(taco_de_sal, X):- taco_de_sal(X).
deduce(taco_de_mixote, X):- taco_de_mixote(X).
deduce(taco_de_barbacoa, X):- taco_de_barbacoa(X).


taco_al_pastor(X):-
    satisface(eres_extrovertido, X),
    satisface(salsa_que_pica, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(te_gusta_la_pizza_con_piña, X),
    satisface(no_perdonas_nada, X),
    satisface(mala_copa, X).

taco_de_canasta(X):-
    satisface(eres_extrovertido, X),
    satisface(salsa_que_pica, X),
    satisface(eres_barrio, X),
    satisface(no_perdonas_nada, X),
    satisface(estás_chaparro, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(eres_codo, X).

taco_de_suadero(X):-
    satisface(eres_medio_tibio, X),
    satisface(salsa_que_pica, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(eres_barrio, X),
    satisface(no_perdonas_nada, X),
    satisface(eres_practico, X).

taco_de_bistec(X):-
    satisface(eres_medio_tibio, X),
    satisface(eres_barrio, X),
    satisface(pilates_era, X),
    satisface(eres_medio_nerd, X),
    satisface(eres_indesiso, X).

taco_de_chorizo(X):-
    satisface(eres_extrovertido, X),
    satisface(salsa_que_pica, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(no_perdonas_nada, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(siempre_vuelven, X).

taco_campechano(X):-
    satisface(eres_extrovertido, X),
    satisface(salsa_que_pica, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(eres_barrio, X),
    satisface(eres_medio_nerd, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(eres_indesiso, X).

taco_de_asada(X):-
    satisface(eres_medio_tibio, X),
    satisface(estás_chaparro, X),
    satisface(pilates_era, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(eres_medio_nerd, X),
    satisface(eres_norteño, X).

taco_de_cochinita(X):-
    satisface(eres_medio_tibio, X),
    satisface(estás_chaparro, X),
    satisface(eres_yucateco, X),
    satisface(eres_medio_nerd, X).

taco_de_guisado(X):-
    satisface(eres_medio_tibio, X),
    satisface(eres_barrio, X),
    satisface(no_perdonas_nada, X),
    satisface(pilates_era, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(nadie_te_elige, X).

taco_de_lengua(X):-
    satisface(eres_extrovertido, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(salsa_que_pica, X),
    satisface(eres_barrio, X),
    satisface(no_te_callas, X).

taco_de_alambre(X):-
    satisface(eres_medio_tibio, X),
    satisface(salsa_que_pica, X),
    satisface(eres_barrio, X),
    satisface(estás_chaparro, X),
    satisface(eres_medio_nerd, X),
    satisface(estas_loco, X).

taco_arabe(X):-
    satisface(eres_medio_tibio, X),
    satisface(pilates_era, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(eres_medio_nerd, X),
    satisface(eres_raro, X).

taco_de_carnitas(X):-
    satisface(eres_extrovertido, X),
    satisface(salsa_que_pica, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(eres_barrio, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(das_mas_de_lo_que_recibes, X).

taco_de_sal(X):-
    satisface(eres_extrovertido, X),
    satisface(shine_es_tu_segundo_hogar, X),
    satisface(eres_barrio, X),
    satisface(no_perdonas_nada, X),
    satisface(aceptas_cualquier_plan, X),
    satisface(eres_migajero, X).

taco_de_mixote(X):-
    satisface(eres_medio_tibio, X),
    satisface(eres_barrio, X),
    satisface(estás_chaparro, X),
    satisface(eres_medio_nerd, X),
    satisface(no_eres_guapo_pero_tampoco_feo, X).

taco_de_barbacoa(X):-
    satisface(eres_medio_tibio, X),
    satisface(salsa_que_pica, X),
    satisface(pilates_era, X),
    satisface(eres_medio_nerd, X),
    satisface(nunca_sales, X).


:- initialization(menu).

