% Sistema Experto en Tacos Unificado

:- dynamic si/1, no/1, si_nad/1, no_nad/1.

% Menú principal
menu:- repeat,
    nl, nl, write('EXPERTO en Tacos'), nl,
    nl, write('Menu'), nl,
    nl, write('1. Piensa en un taco y el experto lo adivinará.'),
    nl, write('2. Qué taco eres según tu personalidad.'),
    nl, write('3. Salir.'),
    nl, nl, write('Indica tu opción: '),
    read(Opcion), nl,
    ( (Opcion=1, motor_clasico, fail);
      (Opcion=2, motor_personalidad, fail);
      (Opcion=3, !) ).

% ----------- Experto clásico: adivina el taco según ingredientes -----------

motor_clasico :- borraResp,
    (deduce(Objeto,_) ->
        (nl,write('Infiero que tu taco es: '),write(Objeto),
         nl,writeln('\nLlegué a esa conclusión porque tu taco: '), findall(X, si(X), L), writeln(L)
         %confirmar_respuesta(Objeto, motor_clasico)
         )
    ;
        (nl,write('\nNo hay un taco con tales atributos en mi base de conocimientos.'),
         mejor_sugerencia(MejorTacos, Porcentaje),
         Porcentaje > 0 ->
            (nl,write('Pero tu mejor opción es: '), write(MejorTacos), write(' ('), write(Porcentaje), write('% de coincidencia)'))
         ;
            nl,write('No se encontró ninguna coincidencia relevante.')
        )
    ).

satisface(Atributo,_) :-
   (si(Atributo)  -> true ;
    (no(Atributo) -> fail ;
                     pregunta(Atributo))).

pregunta(A) :-
    write('Tu taco '), write(A), write('? '),
    read(Resp), nl,
    ((Resp = s ; Resp = si; Resp = y; Resp = yes) -> assert(si(A)) ; assert(no(A)), fail).

borraResp:-retractall(si(_)),retractall(no(_)).

% --- Base de conocimiento clásico
deduce(taco_al_pastor,X):- taco_al_pastor(X).
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
    satisface(tiene_achiote,X),
    satisface(lleva_pina,X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla,X),
    satisface(tortilla_de_maiz,X),
    satisface(es_de_cerdo,X),
    satisface(se_prepara_en_trompo, X).


taco_de_canasta(X):-
    satisface(tiene_papa,X),
    satisface(tiene_frijoles,X),
    satisface(tiene_chicharron,X),
    satisface(tortilla_de_maiz,X),
    satisface(es_de_cerdo,X),
    satisface(es_pequeño, X).

taco_de_suadero(X):-
    satisface(es_de_res, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz, X),
    satisface(es_grasoso, X).

taco_de_bistec(X):-
    satisface(es_de_res, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(tortilla_de_harina, X),
    satisface(preparado_a_la_plancha, X).

taco_de_chorizo(X):-
    satisface(es_de_cerdo, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(tortilla_de_harina, X).

taco_campechano(X):-
    satisface(es_de_cerdo, X),
    satisface(es_de_res, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(tortilla_de_harina, X).

taco_de_asada(X):-
    satisface(es_de_res, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(tortilla_de_harina, X),
    satisface(preparado_a_la_parrilla, X).

taco_de_cochinita(X):-
    satisface(es_de_cerdo, X),
    satisface(lleva_cebolla_morada, X),
    satisface(tiene_achiote, X),
    satisface(lleva_habanero, X),
    satisface(tortilla_de_maiz, X).

taco_de_guisado(X):-
    satisface(lleva_arroz, X),
    satisface(lleva_frijoles, X),
    satisface(lleva_nopales, X),
    satisface(lleva_guisado, X),
    satisface(torilla_de_maiz, X).

taco_de_lengua(X):-
    satisface(es_de_res, X),
    satisface(lleva_lengua, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X).

taco_de_alambre(X):-
    satisface(lleva_pimientos, X),
    satisface(lleva_cebolla, X),
    satisface(lleva_queso, X),
    satisface(puede_ser_de_res, X),
    satisface(puede_ser_de_cerdo, X),
    satisface(puede_ser_de_pollo, X),
    satisface(tortilla_de_maiz, X),
    satisface(tortilla_de_harina, X).

taco_arabe(X):-
    satisface(es_de_cerdo, X),
    satisface(tortilla_de_harina, X),
    satisface(se_prepara_en_trompo, X).

taco_de_carnitas(X):-
    satisface(es_de_cerdo, X),
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(es_grasoso, X).

taco_de_sal(X):-
    satisface(torilla_de_maiz, X),
    satisface(lleva_sal, X).

taco_de_mixote(X):-
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(lleva_cebolla_morada, X),
    satisface(lleva_habanero, X),
    satisface(puede_ser_de_pollo, X),
    satisface(preparado_al_vapor, X).

taco_de_barbacoa(X):-
    satisface(lleva_cilantro,X),
    satisface(lleva_cebolla, X),
    satisface(tortilla_de_maiz,X),
    satisface(preparado_al_vapor, X),
    satisface(es_de_cordero, X).

% -- Todos los tacos y sus atributos

taco_atributos(taco_al_pastor, [tiene_achiote, lleva_pina, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, es_de_cerdo, se_prepara_en_trompo]).
taco_atributos(taco_de_canasta, [tiene_papa, tiene_frijoles, tiene_chicharron, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, es_de_cerdo, es_pequeño]).
taco_atributos(taco_de_suadero, [es_de_res, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, es_grasoso]).
taco_atributos(taco_de_bistec, [es_de_res, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, tortilla_de_harina, preparado_a_la_plancha]).
taco_atributos(taco_de_chorizo, [es_de_cerdo, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, tortilla_de_harina]).
taco_atributos(taco_campechano, [es_de_cerdo, es_de_res, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, tortilla_de_harina]).
taco_atributos(taco_de_asada, [es_de_res, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, tortilla_de_harina, preparado_a_la_parrilla]).
taco_atributos(taco_de_cochinita, [es_de_cerdo, lleva_cebolla_morada, tiene_achiote, lleva_habanero, tortilla_de_maiz]).
taco_atributos(taco_de_guisado, [lleva_arroz, lleva_frijoles, lleva_nopales, lleva_guisado, torilla_de_maiz]).
taco_atributos(taco_de_lengua, [es_de_res, lleva_lengua, lleva_cilantro, lleva_cebolla, tortilla_de_maiz]).
taco_atributos(taco_de_alambre, [lleva_pimientos, lleva_cebolla, lleva_queso, puede_ser_de_res, puede_ser_de_cerdo, puede_ser_de_pollo, tortilla_de_maiz, tortilla_de_harina]).
taco_atributos(taco_arabe, [es_de_cerdo, tortilla_de_harina, se_prepara_en_trompo]).
taco_atributos(taco_de_carnitas, [es_de_cerdo, lleva_cilantro, lleva_cebolla, tortilla_de_maiz, es_grasoso]).
taco_atributos(taco_de_sal, [torilla_de_maiz, lleva_sal]).
taco_atributos(taco_de_mixote, [lleva_cilantro, lleva_cebolla, tortilla_de_maiz, lleva_cebolla_morada, lleva_habanero, puede_ser_de_pollo, preparado_al_vapor]).
taco_atributos(taco_de_barbacoa, [lleva_cilantro, lleva_cebolla, tortilla_de_maiz, preparado_al_vapor, es_de_cordero]).

% -- Se cuenta el número de coincidencias y el total de atributos
count_matches(Taco, Count, Total) :-
    taco_atributos(Taco, Attrs),
    include(si, Attrs, Matched),
    length(Matched, Count),
    length(Attrs, Total).

% -- Se encuentra la mejor sugerencia
% -- Se busca el taco con el mayor porcentaje de coincidencias
mejor_sugerencia(MejorTacos, Porcentaje) :-
    findall(Taco-Count-Total, (taco_atributos(Taco, _), count_matches(Taco, Count, Total)), Results),
    maplist([Taco-C-T, Taco, P]>>(P is C/T*100), Results, Tacos, Porcentajes),
    max_list(Porcentajes, MaxP),
    findall(Taco, (nth1(I, Porcentajes, MaxP), nth1(I, Tacos, Taco)), MejorTacos),
    Porcentaje is MaxP.

% ----------- Experto personalidad: qué taco eres -----------

motor_personalidad :- borraRespNAD,
    (deduce_nad(Objeto,_) ->
        (nl,write('¡Eres el taco: '),write(Objeto),write('!'),
         nl,writeln('\nLlegué a esa conclusión porque: '), findall(X, si_nad(X), L), writeln(L))
    ;
        (nl,write('\nNo encontré un taco que se ajuste a tu personalidad.'))
    ).

satisface_nad(Atributo,_) :-
   (si_nad(Atributo)  -> true ;
    (no_nad(Atributo) -> fail ;
                     pregunta_nad(Atributo))).

pregunta_nad(A) :-
    write('¿Te describe esto? '), write(A), write('? '),
    read(Resp), nl,
    ((Resp = s ; Resp = si; Resp = y; Resp = yes) -> assert(si_nad(A)) ; assert(no_nad(A)), fail).

borraRespNAD:-retractall(si_nad(_)),retractall(no_nad(_)).

% --- Base de conocimiento personalidad
deduce_nad(taco_al_pastor,X):- taco_al_pastor_nad(X).
deduce_nad(taco_de_canasta, X):- taco_de_canasta_nad(X).
deduce_nad(taco_de_suadero, X):- taco_de_suadero_nad(X).
deduce_nad(taco_de_bistec, X):- taco_de_bistec_nad(X).
deduce_nad(taco_de_chorizo, X):- taco_de_chorizo_nad(X).
deduce_nad(taco_campechano, X):- taco_campechano_nad(X).
deduce_nad(taco_de_asada, X):- taco_de_asada_nad(X).
deduce_nad(taco_de_cochinita, X):- taco_de_cochinita_nad(X).
deduce_nad(taco_de_guisado, X):- taco_de_guisado_nad(X).
deduce_nad(taco_de_lengua, X):- taco_de_lengua_nad(X).
deduce_nad(taco_de_alambre, X):- taco_de_alambre_nad(X).
deduce_nad(taco_arabe, X):- taco_arabe_nad(X).
deduce_nad(taco_de_carnitas, X):- taco_de_carnitas_nad(X).
deduce_nad(taco_de_sal, X):- taco_de_sal_nad(X).
deduce_nad(taco_de_mixote, X):- taco_de_mixote_nad(X).
deduce_nad(taco_de_barbacoa, X):- taco_de_barbacoa_nad(X).

taco_al_pastor_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(te_gusta_la_pizza_con_piña, X),
    satisface_nad(no_perdonas_nada, X),
    satisface_nad(mala_copa, X).

taco_de_canasta_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(no_perdonas_nada, X),
    satisface_nad(estás_chaparro, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(eres_codo, X).

taco_de_suadero_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(no_perdonas_nada, X),
    satisface_nad(eres_practico, X).

taco_de_bistec_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(pilates_era, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(eres_indesiso, X).

taco_de_chorizo_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(no_perdonas_nada, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(siempre_vuelven, X).

taco_campechano_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(eres_indesiso, X).

taco_de_asada_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(estás_chaparro, X),
    satisface_nad(pilates_era, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(eres_norteño, X).

taco_de_cochinita_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(estás_chaparro, X),
    satisface_nad(eres_yucateco, X),
    satisface_nad(eres_medio_nerd, X).

taco_de_guisado_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(no_perdonas_nada, X),
    satisface_nad(pilates_era, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(nadie_te_elige, X).

taco_de_lengua_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(no_te_callas, X).

taco_de_alambre_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(estás_chaparro, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(estas_loco, X).

taco_arabe_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(pilates_era, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(eres_raro, X).

taco_de_carnitas_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(das_mas_de_lo_que_recibes, X).

taco_de_sal_nad(X):-
    satisface_nad(eres_extrovertido, X),
    satisface_nad(shine_es_tu_segundo_hogar, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(no_perdonas_nada, X),
    satisface_nad(aceptas_cualquier_plan, X),
    satisface_nad(eres_migajero, X).

taco_de_mixote_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(eres_barrio, X),
    satisface_nad(estás_chaparro, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(no_eres_guapo_pero_tampoco_feo, X).

taco_de_barbacoa_nad(X):-
    satisface_nad(eres_medio_tibio, X),
    satisface_nad(salsa_que_pica, X),
    satisface_nad(pilates_era, X),
    satisface_nad(eres_medio_nerd, X),
    satisface_nad(nunca_sales, X).

:- initialization(menu).