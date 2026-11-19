# -*- coding: utf-8 -*-
"""
Created on 

@author: 
"""


import tkinter as tk
import matplotlib.pyplot as plt
import pandas as pd
from tkinter import simpledialog
from sklearn.cluster import KMeans

#dataframe = pd.read_csv('Mall_Customers.csv')
dataframe = pd.read_csv('Clusters_spi_global_rankings.csv', encoding = 'iso-8859-1')

#Obtiene las caracteristicas de interés
X = dataframe[['off', 'def']]


# Se Crear la ventana raíz
root = tk.Tk()
root.withdraw() 

# Pide el valor de K
k = simpledialog.askinteger("Valor de K", "Introduce el valor de K:")

# Mostrar el número capturado
if k is not None:
    print(f"\nEl número capturado es: {k}")
else:
    print("\nNo se introdujo ningún número, se asignará k=4")
    k=4
    
#Inicialización para algoritmo de clustering con el parámetro K indicado por el usuario
kmeansModel = KMeans(n_clusters=k,random_state=42, n_init=10)

#Carga los datos
kmeansModel.fit(X)

#Obtiene los centroides
centroides = kmeansModel.cluster_centers_


#Obtiene una lista con las etiquetas de los datos
etiqueta = kmeansModel.predict(X)   

# Agrega al frame de datos una columna para las etiquetas de clasificación
# al dataframe original
dataframe['Class'] = etiqueta


# Crear una figura para gráfica 3D
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

# Colores para las diferentes clases
colors = ['magenta', 'lime', 'cyan','red','royalblue','chocolate','gold','darkviolet','forestgreen','gray']

# Graficar los datos en una gráfica 3D
for i in range(k):
    # Seleccionar puntos de la clase i
    xs = dataframe[dataframe['Class'] == i]['off']
    ys = dataframe[dataframe['Class'] == i]['def']
    ax.scatter(xs, ys, c=colors[i], label=f'Class {i}')

# Etiquetas de los ejes
ax.set_xlabel('off')
ax.set_ylabel('def')

# Título y leyenda
ax.set_title('Gráfica de dispersión')
ax.legend()
plt.show()

print("\nMétricas\n")


dataframe=dataframe.sort_values(by='Class',ascending=True)

dataframe.to_csv('Clusters_spi_global_rankings_Clusters.csv', encoding='ISO-8859-1', index=False)


