import pandas as pd
import joblib
from matplotlib import pyplot as plt
from sklearn.tree import DecisionTreeClassifier
from sklearn.tree import plot_tree
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, roc_auc_score, confusion_matrix, ConfusionMatrixDisplay
import os

# Obtiene el modelo salvado en previamente
r = os.path.join(os.path.dirname(__file__), "breast_cancer_random_forest_model.joblib")
model_and_features = joblib.load(r)

# Recupera el índice con los nombres de las características seleccionadas

list_features = list(model_and_features['feature_names'])

# Recupera los datos del dataset
r2 = os.path.join(os.path.dirname(__file__), "breast_cancer.csv")
dataframe = pd.read_csv(r2)

# Muestra los nombres de las características
print('\n Las características elegidas son:')
print(list_features)

# Obtiene las variables predictoras y objetivo
X = dataframe[list_features]
y = dataframe['diagnosis']

# Divide el conjunto de datos en entrenamiento (80%) y pruebas (20%)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)


#Entrenamiento
tree_model = DecisionTreeClassifier(criterion='gini',random_state=42)
tree_model.fit(X_train,y_train)

#Prueba el modelo
y_pred = tree_model.predict(X_test)

# Calcular las métricas ('y' reales vs 'y' predichas)
accuracy = accuracy_score(y_test, y_pred)
precision = precision_score(y_test, y_pred)
recall = recall_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)
conf_matrix = confusion_matrix(y_test, y_pred)
roc_auc = roc_auc_score(y_test, y_pred)

TN, FP, FN, TP = conf_matrix.ravel()


print("\nResultados de las pruebas:\n")
print("Muestra de prueba analizadas:", len(X_test))
print(f"Verdaderos negativos    (TN): {TN}")
print(f"Falsos positivos        (FP): {FP}")
print(f"Falsos negativos        (FN): {FN}")
print(f"Verdaderos positivos    (TP): {TP}")

# Imprimir las métricas
print("\nMetricas de rendimiento:\n")
print("Exactitud (Accuracy):", accuracy)
print("Precisión (Precision):", precision)
print("Sensibilidad (Recall):", recall)
print("Puntuación F1 (F1 Score):", f1)
print("ROC AUC: ",roc_auc )

# Despliega la matriz de confusión
print("Matriz de confusión")
disp = ConfusionMatrixDisplay(confusion_matrix=conf_matrix)
disp.plot(cmap="BuGn")

#Visualización

px = 1/plt.rcParams['figure.dpi']  # Pixel in pulgadas


# Ajustes de imagen para árbol de breast_cancer
fig_size = 2000
font_size = 6
dots_per_inch = 300


# Ajustes de imagen para árbol de diabetes
#fig_size = 15000
#font_size = 2
#dots_per_inch = 400

fig = plt.figure(figsize=(fig_size*px,fig_size*px))
_ = plot_tree(tree_model, feature_names=list_features, class_names=['Negative','Positive'], filled=True, fontsize=font_size, rounded=True)

# Guarda la imagen del árbol

plt.savefig("arbol_de_decision_breast_cancer.png", dpi=dots_per_inch, bbox_inches='tight') 
plt.show()

resp = input('Desea probar el modelo con sus datos? (SI/NO): ')

if resp.upper()=='SI':

    # Pone el modelo en "producción"

    # Adquirir los datos del usuario
    
    print ('\nIndique los datos que se le soliciten')
    prod_features = []

    for feat in list_features:
        value = float(input(feat+': '))
        prod_features.append(value)

    # Crear un dataframe con los datos capturados

    df_prod = pd.DataFrame([prod_features], columns=list_features)

    prediccion = tree_model.predict(df_prod)

    print('\nCon los datos')
    print('Datos originales',prod_features)

    # Muestra diagnóstico
    if prediccion[0]==1:
        print('\nEl modelo predice POSITIVO para la enfermedad :( \n')
    else:
        print('\nEl modelo predice NEGATIVO la enfermedad :) \n')

# eFin
