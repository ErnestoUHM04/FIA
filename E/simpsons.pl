% Arbol genealocico de los Simpsons
% consult("/Users/ernestou/Desktop/ESCOM/FIA/E/simpsons.pl").
% Hecho por Ernesto Ulises Hernández Martínez

% ---------- Hombres
hombre(homer).
hombre(bart).
hombre(abraham).
hombre(herb).
hombre(clancy).

% ---------- Mujeres
mujer(marge).
mujer(lisa).
mujer(maggie).
mujer(mona).
mujer(patty).
mujer(selma).
mujer(jackie).
mujer(ling).

% --------- Progenitor
% Marge y Homer son los progenitores de Bart, Lisa y Maggie
progenitor(marge, lisa).
progenitor(marge, maggie).
progenitor(marge, bart).

progenitor(homer, lisa).
progenitor(homer, maggie).
progenitor(homer, bart).

% Abraham es progenitor de Homer y Herb
progenitor(abraham, homer).
progenitor(abraham, herb).

% Mona es progenitor de Homer
progenitor(mona, homer).

% Clancy y Jackie son progenitores de Marge, Patty y Selma
progenitor(clancy, marge).
progenitor(jackie, marge).

progenitor(clancy, patty).
progenitor(jackie, patty).

progenitor(clancy, selma).
progenitor(jackie, selma).

% Selma es progenitora de Ling
progenitor(selma, ling).

% ---------- Matrimonio
% Marge y Homer están casados
matrimonio(marge, homer).


% Jackie y Clancy están casados
matrimonio(jackie, clancy).

% ---------- Hermano
hermano(Hermano, H2):-
    hombre(Hermano),
    progenitor(P, Hermano),
    progenitor(P, H2),
    Hermano \= H2.

% ---------- Hermana
hermana(Hermana, H2):-
    mujer(Hermana),
    progenitor(P, Hermana),
    progenitor(P, H2),
    Hermana \= H2.

% ---------- Tio
% Tio es tio de S
% Un tio es el hermano del padre o de la madre
tio(Tio, S):-
    hermano(Tio, P),
    progenitor(P, S).

% ---------- Tia
% Tia es tia de S
% Una tia es la hermana del padre o de la madre
tia(Tia, S):-
    hermana(Tia, P),
    progenitor(P, S).

% ---------- Primo
% Primo es primo de P
% Un primo es el hijo del tio o de la tia
primo(Primo, P):-
    hombre(Primo),
    progenitor(Pro, Primo),
    (tio(Pro, P); tia(Pro, P)).

% ---------- Prima
% Prima es prima de P
% Una prima es la hija del tio o de la tia
prima(Prima, P):-
    mujer(Prima),
    progenitor(Pro, Prima),
    (tio(Pro, P); tia(Pro, P)).

% ---------- Yerno
% Yerno es yerno de S
% Un yerno es el marido de la hija o del hijo
yerno(Yerno, S):-
    hombre(Yerno),
    progenitor(S, H),
    matrimonio(H, Yerno).

% ---------- Nuera
% Nuera es nuera de S
% Una nuera es la mujer del hijo o de la hija
nuera(Nuera, S):-
    mujer(Nuera),
    progenitor(S, H),
    matrimonio(Nuera, H).

% ---------- Consuegro
% Consuegro es consuegro de C
% Un consuegro es el padre o la madre del yerno o de la nuera
consuegro(Consuegro, C):-
    hombre(Consuegro),
    progenitor(C, H),
    (yerno(H, Consuegro); nuera(H, Consuegro)).

% ---------- Consuegra
% Consuegra es consuegra de C
% Una consuegra es la madre o el padre del yerno o de la nuera
consuegra(Consuegra, C):-
    mujer(Consuegra),
    progenitor(C, H),
    (yerno(H, Consuegra); nuera(H, Consuegra)).

% ---------- Concuño
% Concuño es concuño de C
% Mi concuño es la pareja del hermano o hermana de mi pareja
concuño(Concuño, C):-
    hombre(Concuño),
    (matrimonio(Concuño, H); matrimonio(H, Concuño)),
    (matrimonio(P, C); matrimonio(C, P)),
    (hermano(H, P); hermana(H, P)).

% ---------- Concuña
% Concuña es concuña de C
concuña(Concuña, C):-
    mujer(Concuña),
    (matrimonio(Concuña, H); matrimonio(H, Concuña)),
    (matrimonio(P, C); matrimonio(C, P)),
    (hermano(H, P); hermana(H, P)).

