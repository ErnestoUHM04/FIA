% ----------------- Suma -----------------

suma([], 0).

suma([Head|Body], Suma) :-
    suma(Body, SumaBody),
    Suma is Head + SumaBody.

% Como en Haskell