% -----------------------  BASE DE CONOCIMIENTOS ----------------------------------------

% Dominio  ----- top
% Reino
% Subreino (op.)
% Filo
% Subfilo (op.)
% Grupo (op.)
% Subgrupo (op.)
% Clase
% Subclase (op.)
% Orden
% Suborden (op.)
% Familia
% Genero
% Especie ----- bottom

% ---------------------------------------------------------------------------------------

frame(eukaryota, padre(top), dominio).
frame(protista, padre(eukaryota), reino).

frame(excavata, padre(protista), subreino).
frame(sarcomastigophora, padre(excavata), filo).

frame(metamonada, padre(sarcomastigophora), subfilo).
frame(mastigophora, padre(metamonada), grupo). % <--

frame(archamoebae, padre(mastigophora), clase). % <--
frame(tritrichomonadida, padre(archamoebae), orden).
frame(dientamoebidae, padre(tritrichomonadida), familia).
frame(dientamoeba, padre(dientamoebidae), genero).
frame(dientamoeba_fragilis, padre(dientamoeba), especie).

frame(parabasalia, padre(mastigophora), subgrupo).
frame(trichomonadea, padre(parabasalia), clase).
frame(trichomonadida, padre(trichomonadea), orden).
frame(trichomonadidae, padre(trichomonadida), familia).
frame(trichomonas, padre(trichomonadidae), genero).
frame(trichomonas_vaginalis, padre(trichomonas), especie).

frame(fornicata, padre(mastigophora), subgrupo).
frame(diplomonadida, padre(fornicata), orden).
frame(hexamitidae, padre(diplomonadida), familia).
frame(giardia, padre(hexamitidae), genero).
frame(giardia_lamblia, padre(giardia), especie).

frame(euglenoza, padre(sarcomastigophora), subfilo).
frame(mastigophora, padre(eugelenoza), grupo). % mastigophora se repite
frame(kinetoplastea, padre(mastigophora), clase).
frame(trypanosomatida, padre(kinetoplastea), orden).
frame(trypanosomatidae, padre(trypanosomatida), familia).

frame(leishmania, padre(trypanosomatidae), genero).
frame(leishmania_donovani, padre(leishmania), especie).
frame(leishmania_mexicana, padre(leishmania), especie).
frame(leishmania_braziliensis, padre(leishmania), especie).

frame(trypanosoma, padre(trypanosomatidae), genero).
frame(trypanosoma_cruzi, padre(trypanosoma), especie).
frame(trypanosoma_brucei_gambiense, padre(trypanosoma), especie).

frame(percolozoa, padre(protista), filo).
frame(heterolobosea, padre(percolozoa), clase).
frame(schizopyrenida, padre(heterolobosea), orden).
frame(vahlkampfiidae, padre(schizopyrenida), familia).
frame(naegleria, padre(vahlkampfiidae), genero).
frame(naegleria_fowleri, padre(naegleria), especie).

frame(amoebozoa, padre(protista), filo).

frame(discosea, padre(amoebozoa), clase).
frame(centramoebida, padre(discosea), orden).

frame(balamuthiidae, padre(centramoebida), familia).
frame(balamuthia, padre(balamuthiidae), genero).
frame(balamuthia_mandrillaris, padre(balamuthia), especie).

frame(acanthamoebidae, padre(centramoebida), familia).
frame(acanthamoeba, padre(acanthamoebidae), genero).
frame(acanthamoeba_castellanii, padre(acanthamoeba), especie).
frame(acanthamoeba_polyphaga, padre(acanthamoeba), especie).

frame(archamoebae, padre(amoebozoa), clase). % archamoebae se repite
frame(amoebida, padre(archamoebae), orden).
frame(entamoebidae, padre(amoebida), familia).
frame(entamoeba, padre(entamoebidae), genero).
frame(entamoeba_histolytica, padre(entamoeba), especie).

frame(ciliophora, padre(protista), filo).
frame(litostomatea, padre(ciliophora), clase).
frame(vestibuliferida, padre(litostomatea), orden).
frame(balantidiidae, padre(vestibuliferida), familia).
frame(balantidium, padre(balantidiidae), genero).
frame(balantidium_coli, padre(balantidium), especie).

frame(alveolata, padre(protista), subreino).
frame(apicomplexa, padre(alveolata), filo).

frame(aconoidasida, padre(apicomplexa), clase).

frame(piroplasmida, padre(aconoidasida), orden).
frame(babesiidae, padre(piroplasmida), familia).
frame(babesia, padre(babesiidae), genero).
frame(babesia_spp, padre(babesia), especie). % hay mas de una babesia_spp

frame(haemosporida, padre(aconoidasida), orden).
frame(plasmodiidae, padre(haemosporida), familia).
frame(plasmodium, padre(plasmodiidae), genero).
frame(plasmodium_falciparum, padre(plasmodium), especie).
frame(plasmodium_vivax, padre(plasmodium), especie).
frame(plasmodium_ovale, padre(plasmodium), especie).
frame(plasmodium_malariae, padre(plasmodium), especie).
frame(plasmodium_knowlesi, padre(plasmodium), especie).

frame(conoidasida, padre(apicomplexa), clase).
frame(coccidiasina, padre(conoidasida), subclase).
frame(eucoccidiorida, padre(coccidiasina), orden).
frame(eimeriorina, padre(eucoccidiorida), suborden).

frame(eimeriidae, padre(eimeriorina), familia).
frame(cyclospora, padre(eimeriidae), genero).
frame(cyclospora_cayetanensis, padre(cyclospora), especie).

frame(cryptosporidiidae, padre(eimeriorina), familia).
frame(cryptosporidium, padre(cryptosporidiidae), genero).
frame(cryptosporidium_parvum, padre(cryptosporidium), especie).
frame(cryptosporidium_hominis, padre(cryptosporidium), especie).

frame(sarcocystidae, padre(eimeriorina), familia).

frame(sarcocystis, padre(sarcocystidae), genero).
frame(sarcocystis_spp, padre(sarcocystis), especie). % hay mas de una sarcocystis_spp

frame(taxoplasma, padre(sarcocystidae), genero).
frame(taxoplasma_gondii, padre(taxoplasma), especie).

frame(cystoisospora, padre(sarcocystidae), genero).
frame(cystoisospora_belli, padre(cystoisospora), especie).

% ----------------------- Descripciones de las especies -----------------------
% Las descripciones de las especies se pueden agregar aquí como hechos adicionales.

descripcion(dientamoeba_fragilis, 'Causa Diarrea, dolor abdominal (dientamebiasis intestinal), Confundido con síntomas con Giardia.
     Infección intestinal sintomática: Sintomas digestivos leves.
     Cuadro crónico: en pacientes inmunocomprometidos, se asocia con dolor abdominal persistente, diarrea intermitente y fatiga. A veces se confunde con cólon irritable.').

descripcion(trichomonas_vaginalis, ' Vive en el tracto urogenital de mujeres y hombres, Causa una ETS o tricomoniasis que causa inflamación vaginal, flujo espumoso y prurito (comezón).
Se reproduce por fisión binaria en su forma de trofozoito y se localizan en el epitelio urogenital.  ').

descripcion(giardia_lamblia, 'Causa diarrea fulminante y hasta deshidratación.
Se reproduce por fisión binaria (se parte en dos)
Se adhiere el epitelio intestinal. ').

descripcion(leishmania_donovani, 'La leishmania se instala dentro de los macrófagos. Causan  leishmaniasis visceral, mucocutánea y cutánea.
    Cutánea: ulceras indoloras en piel, es la más común.
    Mucocutánea: afecta mucosas (nariz, boca, garganta) y destruye los tejidos y es desfigurante.
    Visceral: afecta bazo, hígado y médula ósea, causa: fiebre, pérdida de peso, anemia y hepatoesplenomegalia.').

descripcion(leishmania_mexicana, 'La leishmania se instala dentro de los macrófagos. Causan  leishmaniasis visceral, mucocutánea y cutánea.
    Cutánea: ulceras indoloras en piel, es la más común.
    Mucocutánea: afecta mucosas (nariz, boca, garganta) y destruye los tejidos y es desfigurante.
    Visceral: afecta bazo, hígado y médula ósea, causa: fiebre, pérdida de peso, anemia y hepatoesplenomegalia.').

descripcion(leishmania_braziliensis, 'La leishmania se instala dentro de los macrófagos. Causan  leishmaniasis visceral, mucocutánea y cutánea.
    Cutánea: ulceras indoloras en piel, es la más común.
    Mucocutánea: afecta mucosas (nariz, boca, garganta) y destruye los tejidos y es desfigurante.
    Visceral: afecta bazo, hígado y médula ósea, causa: fiebre, pérdida de peso, anemia y hepatoesplenomegalia.').

descripcion(trypanosoma_cruzi, 'Es el género que causa de la enfermedad de Chagas.
Enfermedad de Chagas: Se transmite por la picadura y heces de la chinche triatomina o chinche besucona. Causa fiebre, inflamación en el sitio de infección, edema en parpados, y finalmente afectaciones cardiacas, y digestivas.
Se instala dentro del tejido muscular y endotelio y viaja en sangre, linfa y LCR. ').

descripcion(trypanosoma_brucei_gambiense, 'Es el género que causa de la enfermedad del sueño.
Enfermedad del sueño: por la picadura de la mosca tse-tsé, causa fiebre, dolor de cabeza, inflamación de ganglios y finalmente daño en meninges y cerebro, lo que lleva a alucinaciones o sueño. Finalmente la perdida de la conciencia (coma) y muerte.
Se instala dentro del tejido muscular y endotelio y viaja en sangre, linfa y LCR. ').

descripcion(naegleria_fowleri, 'Causa Naegleriasis que es encontrada ampliamente en cuerpos de agua dulce como: lagos, lagunas, estanques, piscinas y canales de riego. Se nutren de bacterias y tiene metabolismo aeróbico. El contacto es principalmente por la mucosa nasal donde invade rápidamente los bulbos olfatorios, y atraviesa la lámina cribosa, del etmoides, llegando a sistema nervioso central y a las meninges. además puede invadir faringe, nasofaringe. Es la causa de Meningoencefalitis amebiana primaria (MAP) con todos los síntomas; La muerte es causada por edema pulmonar o paro cardiorrespiratorio 72 horas después de los síntomas 50% por miocarditis. ').

descripcion(balamuthia_mandrillaris, 'Entra al organismo a través de piel lesionada o por la mucosa nasal. Viaja por el torrente sanguíneo. Llega al sistema nervioso central (SNC), donde invade el cerebro.Y provoca una respuesta inflamatoria crónica: formación de granulomas (cúmulos de células inmunitarias que intentan contener la infección). Se presenta con cefalea, fiebre, confusión, naúsea, vómito, rigidez del cuello, convulsiones, hemiparesia y coma.
Puede causar:
     Infección cutánea: causa lesiones como placas o úlceras que no sanas.
     Encefalitis amebiana granulomatosa (EAG): Produce inflamación, necrosis y daño en el sistema nervioso central (SNC)
     Encefalitis diseminada: en inmunocomprometidos').

descripcion(acanthamoeba_castellanii, 'Puede presentarse en cuerpos humano en 3 formas clínicas:
     Queratitis amebiana: infección en la córnea usual por el uso de lentes de contacto.
     Encefalitis amebiana granulomatosa (EAG): es una infección cerebral crónica que se presenta más en pacientes inmunodeprimidos.
     Infecciones cutáneas y desiminadas: En pacientes inmunodeprimidos. ').

descripcion(acanthamoeba_polyphaga, 'Puede presentarse en cuerpos humano en 3 formas clínicas:
     Queratitis amebiana: infección en la córnea usual por el uso de lentes de contacto.
     Encefalitis amebiana granulomatosa (EAG): es una infección cerebral crónica que se presenta más en pacientes inmunodeprimidos.
     Infecciones cutáneas y desiminadas: En pacientes inmunodeprimidos. ').

descripcion(entamoeba_histolytica, 'Causan una amebiasis o amebiosis que puede ser:
     Amebiasis intestinal: causa disentería (diarrea con moco y sangre), dolor intestinal, pujo y tenesmo (sensación de evacuar).
     Amebiasis extraintestinal: cuando se disemina por sangre, principalmente en el hígado causa un abceso hepático, que se manifiesra con fiebre, dolor y hepatomegalia.
     Amebiasis cutánea: (rara) Lesiones ulcerativas, que suelen ser perianales. Ulceras en botón de camisa.
Esto lo hace adhiriendose a la mucosa del cólon, y destruye el tejido, y produce enzimas que rompen proteínas, se ingiere como quistes, en el intestino se forman trofozoítos, estos se multiplican e invaden el tejido.').

descripcion(balantidium_coli, 'Es el único ciliado que infecta humanos. Causa balantidiasis, que causa desde diarrea leve hasta disentería grave. Y se transmite por alimentos contaminados con quistes
    Asintomática: portación crónica con eliminación de quistes.
    Forma leve o aguda: diarrea intermitente o persistente, dolor cólico, naúseas y distensión abdominal
    Disentería: diarrea con moco y sangre, fiebre, tenesmo, y deshidratación.
    Complicaciones graves: perforación intestinal, perionitis, infección extraintestinal. ').

descripcion(plasmodium_falciparum, 'Un protozoo que causa la malaria, una enfermedad transmitida por mosquitos que afecta la sangre y puede ser mortal.').
descripcion(plasmodium_vivax, 'Un protozoo que causa la malaria, menos grave que P. falciparum pero puede causar recaídas.').
descripcion(plasmodium_ovale, 'Un protozoo que causa la malaria, menos común y menos grave que P. falciparum y P. vivax.').
descripcion(plasmodium_malariae, 'Un protozoo que causa la malaria, menos común y generalmente menos grave.').
descripcion(plasmodium_knowlesi, 'Un protozoo que causa malaria en monos y puede infectar a humanos, especialmente en el sudeste asiático.').
descripcion(cyclospora_cayetanensis, 'Un protozoo que causa la infección intestinal conocida como ciclospora, que provoca diarrea y malestar gastrointestinal.').
descripcion(cryptosporidium_parvum, 'Un protozoo que causa criptosporidiosis, una infección intestinal que provoca diarrea acuosa.').
descripcion(cryptosporidium_hominis, 'Un protozoo que causa criptosporidiosis, una infección intestinal que provoca diarrea acuosa.').
descripcion(sarcocystis_spp, 'Un protozoo que puede causar sarcocistosis, una infección que afecta los músculos y otros tejidos.').
descripcion(taxoplasma_gondii, 'Un protozoo que causa toxoplasmosis, una infección común que puede ser grave en personas inmunocomprometidas y durante el embarazo.').
descripcion(cystoisospora_belli, 'Un protozoo que causa isosporiasis, una infección intestinal que provoca diarrea y malestar gastrointestinal.').

