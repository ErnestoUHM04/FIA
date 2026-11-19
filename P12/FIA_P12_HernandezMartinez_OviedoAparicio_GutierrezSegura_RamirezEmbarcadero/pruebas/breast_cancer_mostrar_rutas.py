import pandas as pd
import joblib
from matplotlib import pyplot as plt
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.model_selection import train_test_split
from sklearn.metrics import (
    accuracy_score, precision_score, recall_score, f1_score,
    roc_auc_score, confusion_matrix, ConfusionMatrixDisplay
)
import os
import copy

# --- Carga y entrenamiento del modelo ---
model_path = os.path.join(os.path.dirname(__file__), "breast_cancer_random_forest_model.joblib")
model_and_features = joblib.load(model_path)
list_features = model_and_features['feature_names']

df = pd.read_csv(os.path.join(os.path.dirname(__file__), "breast_cancer.csv"))
X = df[list_features]
y = df['diagnosis']  # 0 = Negativo, 1 = Positivo

# División del dataset
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# Entrenamiento del árbol de decisión
clf = DecisionTreeClassifier(criterion='gini', random_state=42)
clf.fit(X_train, y_train)

# Evaluación general
y_pred = clf.predict(X_test)
cm = confusion_matrix(y_test, y_pred)
disp = ConfusionMatrixDisplay(confusion_matrix=cm)
disp.plot(cmap="BuGn")
plt.show()

# Solicitar índice de fila
while(True):
    if input('¿Mostrar ruta como imagen para una fila? (SI/NO): ').strip().upper()=='SI':
        idx = int(input(f"Ingrese índice de fila (0 a {len(df)-1}): "))
        row = X.iloc[[idx]]
        real = y.iloc[idx]
        pred = clf.predict(row)[0]

        # Obtener ruta de nodos
        node_indicator = clf.decision_path(row)
        node_index = node_indicator.indices[node_indicator.indptr[0]:node_indicator.indptr[1]]

        # Hacer copia del modelo para podar
        pruned = copy.deepcopy(clf)
        tree = pruned.tree_
        # Para cada nodo no en ruta, marcar como hoja
        for node_id in range(tree.node_count):
            if node_id not in node_index:
                tree.children_left[node_id] = -1
                tree.children_right[node_id] = -1

        # Dibujar solo la subruta
        plt.figure(figsize=(8, 8))
        plot_tree(
            pruned,
            feature_names=list_features,
            class_names=['Negativo', 'Positivo'],
            filled=True,
            rounded=True,
            fontsize=10
        )
        plt.title(f'Ruta árbol fila {idx} (pred={pred}, real={real})')
        plt.axis('off')
        plt.show()
    else:
        print('Fin del programa.')
        quit()
