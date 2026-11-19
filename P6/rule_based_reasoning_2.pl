% Ejemplo de implementación de sistema experto
% Basado en: https://sergioc2005.tripod.com/intart/Pract7SistExpertProlog06.pdf

:- dynamic conocido/1.


main:-repeat,bienvenida,
      nl,write('Menu'),nl,
      nl,write('1 Consulta al asesor'),
      nl,write('2 Salir'),
      nl,nl,write('Indica tu opcion:'),
      read(Opcion),nl,
      ((Opcion=1,consulta,fail);
       (Opcion=2,writeln('Gracias por utilizar mi programa'),!)).

bienvenida:-writeln('\n==========================================='),
            writeln('Asesoría Vocacional del Maestro Roshi'),
            writeln('==========================================='),
            writeln('\nPuedo hacer recomendaciones pero no soy'),
            writeln('un asesor de verdad, para tomar una mejor'),
            writeln('decisión acuda a un asesor autorizado.').

consulta:-limpia_memoria_de_trabajo,
          writeln("\nLe haré algunas preguntas conteste con 'si' o 'no'\n"),
          haz_recomendacion(X),
          escribe_recomendacion(X),!.

consulta:-write('No hay suficiente conocimiento para elaborar un recomendacion.').


% Busca un programa académico, lo propone como hipotesis y trata de
% probar las premisas (los Caracteristicas que caracterizan el programa

haz_recomendacion(Recomandacion):-obten_hipotesis_y_Caracteristicas(Recomandacion, ListaDeCaracteristicas),
	        prueba_presencia_de(Recomandacion, ListaDeCaracteristicas).

obten_hipotesis_y_Caracteristicas(Recomandacion, ListaDeCaracteristicas):-rules(programa(Recomandacion), caracteristicas(ListaDeCaracteristicas)).

prueba_presencia_de(_, []).
prueba_presencia_de(Recomandacion, [Head | Tail]):- prueba_verdad_de(Recomandacion, Head),prueba_presencia_de(Recomandacion, Tail).

% Verifica si la Caracteristica esta en la memoria de trabajo
prueba_verdad_de(_, Caracteristica):- conocido(Caracteristica).

% Si no esta la Caracteristica en la memoria pregunta sobre
% presencia de la Caracteristica en el aspirante
prueba_verdad_de(Recomandacion, Caracteristica):- not(conocido(es_falso_que(Caracteristica))),
	                               pregunta_sobre(Recomandacion, Caracteristica, Reply),
				       Reply = si.
% Hace la pregunta
pregunta_sobre(Recomandacion, Caracteristica, Reply):- write('Es verdad que '),
	                                    write(Caracteristica), write('? '),
					    read(Respuesta),
					    process(Recomandacion, Caracteristica, Respuesta, Reply).

% Procesa la respuesta y agrega el hecho a la
% memoria de trabajo si la respuesta es si o no
% en case de que sea 'porque' explica la razón
%
process(_, Caracteristica, si, si):- asserta(conocido(Caracteristica)).
process(_, Caracteristica, no, no):- asserta(conocido(es_falso_que(Caracteristica))).
process(Recomandacion, Caracteristica, porque, Reply):- nl,
write('Estoy investigando la hipotesis siguiente: '),
write(Recomandacion), write('.'), nl, write('Para esto necesito saber si '),
write(Caracteristica), write('.'), nl, pregunta_sobre(Recomandacion, Caracteristica, Reply).
process(Recomandacion, Caracteristica, Respuesta, Reply):- Respuesta \== no,
Respuesta \== si, Respuesta \== porque, nl,
write('Debes contestar si, no o porque.'), nl,
pregunta_sobre(Recomandacion, Caracteristica, Reply).


% Escribe el recomendacion encontrado
escribe_recomendacion(Recomandacion):- write('\nLa recomendacion es estudiar '),
write(Recomandacion),nl, writeln('\nLlegué a esa conclusión porque: '), findall(Pref, conocido(Pref), Lista),imprime_lista(Lista).

% La memoria de trabajo guarda el conocimiento adquirido
% de las respuestas del usuario

limpia_memoria_de_trabajo:- retractall(conocido(_)).
imprime_lista([]).
imprime_lista([H|T]):-writeln(H), imprime_lista(T).

% BaseConocimientos:
%
rules(programa('INGENIERÍA EN SISTEMAS COMPUTACIONALES'),
      caracteristicas([te_gusta_programar,
                te_interesan_sistemas,
                te_gusta_Yu_Gi_Oh,
                te_interesan_las_redes,
                eres_persistente])).
rules(programa('INGENIERÍA EN INTELIGENCIA ARTIFICIAL'),
      caracteristicas([te_gusta_programar,
                te_interesan_algoritmos_inteligentes,
                te_gusta_resolver_problemas_complejos,
                eres_creativo,
                tienes_pensamiento_abstracto])).
rules(programa('LICENCIATURA EN CIENCIA DE DATOS'),
      caracteristicas([te_interesa_analizar_datos,
                te_gusta_la_estadistica,
                te_gusta_descubrir_patrones,
                eres_detallista,
                tienes_habilidad_numerica])).











