%Frutas
fruta(papaya, 45).
fruta(melon, 31).

%Cereales
cereal(pan_centeno, 241).
cereal(pan_blanco, 255).

%Jugos
jugo(zumo_naranja, 42).

%Huevo
huevo(huevo_estrellado, 147).

%Carnico
carnico(chuleta_cerdo, 330).
carnico(chicharron, 601).

%Pastas
pasta(pasta_huevo, 368).
pasta(pasta_semola, 361).

%Postre
postre(flan_vainilla, 102).
postre(flan_huevo, 126).

%Lacteo
lacteo(leche_entera, 68).
lacteo(leche_semidescremada, 49).

%Colacion
colacion(donut, 456).
colacion(pastel_manzana, 311).

% Regla para armar el desayuno
desayuno(D1, D2, D3, D4, KCal) :-
    fruta(D1, K1), cereal(D2, K2), jugo(D3, K3), huevo(D4, K4),
    KCal is K1 + K2 + K3 + K4.

% Regla para armar la comida
comida(C1, C2, C3, KCal) :-
    carnico(C1, K1), pasta(C2, K2), postre(C3, K3),
    KCal is K1 + K2 + K3.

% Regla para armar la merienda
merienda(M1, M2, KCal) :-
    lacteo(M1, K1), colacion(M2, K2),
    KCal is K1 + K2.

% Regla que genera lista de menus
dietas(Gasto, ListaMenus) :-
    findall(menu([D1,D2,D3,D4],[C1,C2,C3],[M1,M2],TotalKCal), (
        desayuno(D1,D2,D3,D4,K1),
        comida(C1,C2,C3,K2),
        merienda(M1,M2,K3),
        TotalKCal is K1 + K2 + K3,
        Inferior is Gasto * 0.9,
        Superior is Gasto * 1.1,
        TotalKCal >= Inferior,
        TotalKCal =< Superior
    ), ListaMenus).
