%Ejemplos de funciones en PROLOG

% regresa x = 2 + 3
ejemplo_unificacion :-
    X = 2 + 3,
    writeln('X = 2 + 3 con "=" da:'),
    writeln(X).

% regresa y = 5
ejemplo_evaluacion :-
    Y is 2 + 3,
    writeln('Y is 2 + 3 con "is" da:'),
    writeln(Y).

% regresa la suma de dos números
sumar :-
    writeln('Introduce el primer número:'),
    read(X),
    number(X),
    writeln('Introduce el segundo número:'),
    read(Y),
    number(Y),
    S is X + Y,
    format('La suma de ~w y ~w es: ~2f~n', [X, Y, S]).

sumar(A, B) :-
    number(A),
    number(B),
    S is A + B,
    format('La suma de ~w y ~w es: ~2f~n', [A, B, S]).

sumar(A, B, S) :-
    number(A),
    number(B),
    S is A + B.

potencia(A, B, S) :-
    number(A),
    number(B),
    S is A ** B.

residuo(A, B, M) :-
    number(A),
    number(B),
    M is A mod B.

es_par(X) :-
    number(X),
    X mod 2 =:= 0.

es_impar(X) :-
    number(X),
    X mod 2 =\= 0.

raiz(X, R) :-
    number(X),
    R is sqrt(X).

imc(Peso, Altura, IMC) :-
    number(Peso),
    number(Altura),
    Altura > 0, Peso > 0,
    IMC is Peso / (Altura ** 2).

imc :-
    writeln('Calculo del indice de masa corporal'),
    writeln('Indique su peso en Kg:'),
    read(P),
    writeln('Indique su altura en M:'),
    read(A),
    imc(P,A,IMC),
    format('El IMC es ~2f~n', [IMC]).

%convertir de grados C° a F°
convertir(C, F) :-
    number(C),
    F is C * 9 / 5 + 32.

factorial(0,1).
factorial(N, F) :-
    integer(N), N > 0,
    N1 is N - 1,
    factorial(N1, F1),
    F is N * F1.
    