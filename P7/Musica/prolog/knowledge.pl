%BASE DE CONOCIMIENTOS
%(Estos son solo ejemplos, no es información real)

frame(metal,subclase_de(top),propiedades([usa(guitarras_distorsionadas),usa(tapping),usa(sweep_piking)])).
frame(heavy_metal,subclase_de(metal),propiedades([usa(voces_inteligibles)])).
frame(power_metal,subclase_de(metal),propiedades([usa(teclados),usa(voces_melodicas)])).
frame(thrash_metal,subclase_de(metal), propiedades([usa(riff_graves), usa(voces_asperas)])).
frame(kawaii_metal,subclase_de(heavy_metal),propiedades([origen(japones),usa(voces_femeninas_tiernas)])).
frame(progressive_metal,subclase_de(heavy_metal),propiedades([tiene(influencia_de_jazz)])).
frame(black_metal,subclase_de(thrash_metal), propiedades([usa(beat_blast)])).
frame(extreme_power_metal,subclase_de(power_metal), propiedades([usa(velocidad_extrema),usa(letras_epicas)])).


