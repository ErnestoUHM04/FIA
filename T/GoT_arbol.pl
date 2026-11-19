/*
    swipl
    ?- [myfile].
*/
%arbol Game Of Thrones

hombre(richard_stark).
hombre(eddard_stark).
hombre(brandon_stark).
hombre(benjen_stark).
hombre(robb_stark).
hombre(bran_stark).
hombre(rickon_stark).
hombre(jon_snow).
hombre(aerys_targaryen).
hombre(rhaeger_targaryen).
hombre(viserys_targaryen).
hombre(aegon_targaryen).

mujer(lyarra_stark).
mujer(catelyn_stark).
mujer(lyanna_stark).
mujer(sansa_stark).
mujer(arya_stark).
mujer(rhaella_targaryen).
mujer(elia_targaryen).
mujer(daenerys_targaryen).
mujer(rhaenys_targaryen).

%progenitor(padre/madre, hijo/hija).

progenitor(richard_stark, eddard_stark).
progenitor(richard_stark, brandon_stark).
progenitor(richard_stark, benjen_stark).
progenitor(richard_stark, lyanna_stark).
progenitor(lyarra_stark, eddard_stark).
progenitor(lyarra_stark, brandon_stark).
progenitor(lyarra_stark, benjen_stark).
progenitor(lyarra_stark, lyanna_stark).

progenitor(eddard_stark, robb_stark).
progenitor(eddard_stark, sansa_stark).
progenitor(eddard_stark, arya_stark).
progenitor(eddard_stark, bran_stark).
progenitor(eddard_stark, rickon_stark).
progenitor(catelyn_stark, robb_stark).
progenitor(catelyn_stark, sansa_stark).
progenitor(catelyn_stark, arya_stark).
progenitor(catelyn_stark, bran_stark).
progenitor(catelyn_stark, rickon_stark).

progenitor(aerys_targaryen, rhaeger_targaryen).
progenitor(aerys_targaryen, viserys_targaryen).
progenitor(aerys_targaryen, daenerys_targaryen).
progenitor(rhaella_targaryen, rhaeger_targaryen).
progenitor(rhaella_targaryen, viserys_targaryen).
progenitor(rhaella_targaryen, daenerys_targaryen).

progenitor(rhaeger_targaryen, rhaenys_targaryen).
progenitor(rhaeger_targaryen, aegon_targaryen).
progenitor(elia_targaryen, rhaenys_targaryen).
progenitor(elia_targaryen, aegon_targaryen).

progenitor(rhaeger_targaryen, jon_snow).
progenitor(lyanna_stark, jon_snow).

%matrimonio(esposo, esposa).

matrimonio(richard_stark, lyarra_stark).
matrimonio(eddard_stark, catelyn_stark).
matrimonio(aerys_targaryen, rhaella_targaryen).
matrimonio(rhaeger_targaryen, elia_targaryen).
matrimonio(rhaeger_targaryen, lyanna_stark).


%		Queries
/*	?- hombre(jon_snow).
 * 	?- progenitor(eddard_stark, Hijo).
 * 	?- progenitor(Padre, jon_snow).
 * 	?- progenitor(Padre, Hijo), hombre(Padre).
 * */

%padre(padre, hijo/hija).

padre(P, H) :- progenitor(P, H), hombre(P).

%madre(madre, hijo/hija).

madre(M, H) :- progenitor(M, H), mujer(M).

%abuelo(abuelo, nieto/nieta).

abuelo(A,N) :- padre(A, H), progenitor(H ,N).

%abuela(abuela, nieto/nieta).

abuela(A, N) :- madre(A, H), progenitor(H, N).

%hermano(hermano, hermano/hermana).

hermano(H1, H2) :- progenitor(P, H1), progenitor(P, H2), H1\=H2, hombre(H1).

%hermana(hermana, hermano/hermana).

hermana(H1, H2) :- progenitor(P, H1), progenitor(P, H2), H1\=H2, mujer(H1).

%esposo(esposo, pareja).

esposo(E, P) :- matrimonio(E, P), hombre(E).

%esposa(esposa, pareja).

esposa(E, P) :- matrimonio(P, E), mujer(E).


%		Queries
/*	?- hermano(bran_stark, Quien).
 * 	?- abuela(Abuela, jon_snow).
 * 	?- esposo(Esposo, lyanna_stark).
 *  ?- esposo(Esposo, elia_targaryen).
 *  ?- hermano(Hermano, jon_snow).
 * 	?- hermana(Hermana, jon_snow).
 * */