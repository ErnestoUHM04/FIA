# -*- coding: utf-8 -*-
"""
Created on Thu Jun  8 11:42:18 2023

@author: 
"""

import pandas as pd
from matplotlib import pyplot as plt
from sklearn.tree import DecisionTreeClassifier
from sklearn.tree import plot_tree
from sklearn.preprocessing import LabelEncoder

#Adquiere los datos desde un archivo csv usando biblioteca PANDAS
dataframe = pd.read_csv('volleyball.csv', encoding='ISO-8859-1')

#Preprocesamiento (Transformar valores categóricos a numéricos)
outlook=LabelEncoder()
temp=LabelEncoder()
humidity=LabelEncoder()
windy=LabelEncoder()
play=LabelEncoder()

dataframe['outlook']=outlook.fit_transform(dataframe['outlook'])
dataframe['temp']=temp.fit_transform(dataframe['temp'])
dataframe['humidity']=humidity.fit_transform(dataframe['humidity'])
dataframe['windy']=windy.fit_transform(dataframe['windy'])
dataframe['play']=play.fit_transform(dataframe['play'])

#Prepara los datos
features_cols=['outlook','temp','humidity','windy']
X=dataframe[features_cols]
y=dataframe.play

#Entrenamiento
tree = DecisionTreeClassifier(criterion='gini',random_state=42)
tree.fit(X,y)

#Visualización
dots_per_inch = 300
px = 1/plt.rcParams['figure.dpi']  # Pixel in pulgadas
fig = plt.figure(figsize=(1000*px,1000*px)) 
_ = plot_tree(tree, feature_names=features_cols, class_names=['No','Yes'], filled=True)

# Guarda la imagen del árbol

plt.savefig("arbol_de_decision_volleyball.png", dpi=dots_per_inch, bbox_inches='tight') 
plt.show()

resp = input('Desea probar el modelo con sus datos? (SI/NO): ')

# Probar el Modelo

if resp.upper()=='SI':

    dfprueba = pd.DataFrame()
    dfprueba[ 'outlook'] = [1] # rain
    dfprueba[ 'temp'] = [0] # cool
    dfprueba[ 'humidity'] = [0] # high
    dfprueba[ 'windy'] =[0] # strong

    prediccion = tree.predict(dfprueba)

    print('Resultado de la prueba')
    print('**********************')
    print('Con los datos')
    print(dfprueba)
    print('\nSe recomienda:')
    if(prediccion[0]==0):
        print('NO hacer deporte')
    else:
        print('SI hacer deporte')
    print("**********************")    

# efin
