%Frutas
fruta(papaya, 45).
fruta(melon, 31).
fruta(manzana, 52).
fruta(pera, 57)

%Cereales
cereal(pan_centeno, 241).
cereal(pan_blanco, 255).
cereal(avena, 389).
cereal(arroz_integral, 370).

%Jugos
jugo(zumo_naranja, 42).
jugo(zumo_manzana, 46).
jugo(zumo_uva, 60).

%Huevo
huevo(huevo_estrellado, 147).
huevo(huevo_revueltos, 154).


%Carnico
carnico(chuleta_cerdo, 330).
carnico(chicharron, 601).
carnico(pechuga_pollo, 165).

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

% Regla para armar el almuerza
almuerzo(A1, A2, KCal) :-
    fruta(A1, K1), lacteo(A2, K2),
    KCal is K1 + K2.

% Regla para armar la comida
comida(C1, C2, C3, KCal) :-
    carnico(C1, K1), pasta(C2, K2), postre(C3, K3),
    KCal is K1 + K2 + K3.

% Regla para armar la merienda
merienda(M1, M2, KCal) :-
    lacteo(M1, K1), colacion(M2, K2),
    KCal is K1 + K2.

% Regla para armar la cena
cena(C1, C2, C3, KCal) :-
    lacteo(C1, K1), colacion(C2, K2), postre(C3, K3),
    KCal is K1 + K2 + K3.

% Regla que genera lista de menus
dietas(TMB, ListaMenus) :-
    findall(menu([D1,D2,D3,D4],[A1,A2],[C1,C2,C3],[M1,M2],[Ce1,Ce2,Ce3],TotalKCal), (
        desayuno(D1,D2,D3,D4,K1),
        almuerzo(A1,A2,K2),
        comida(C1,C2,C3,K3),
        merienda(M1,M2,K4),
        cena(Ce1,Ce2,Ce3,K5),
        TotalKCal is K1 + K2 + K3 + K4 + K5,
        Inferior is TMB * 0.9,
        Superior is TMB * 1.1,
        TotalKCal >= Inferior,
        TotalKCal =< Superior
    ), AllMenus),
    length(AllMenus, Length),
    (Length > 1000 -> length(ListaMenus, 1000), append(ListaMenus, _, AllMenus); ListaMenus = AllMenus).
