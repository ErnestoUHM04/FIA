import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.neighbors import KNeighborsClassifier
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.naive_bayes import MultinomialNB
from sklearn.cluster import KMeans
from sklearn.metrics import (
    accuracy_score, precision_score, recall_score, f1_score,
    confusion_matrix, silhouette_score, ConfusionMatrixDisplay
)
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA

import re, string
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer

# 1. Carga y preprocesamiento de datos
# df = pd.read_csv('spam_ham_dataset.csv')
df = pd.read_csv('Proyecto Final/spam_ham_clean.csv')
df = df.dropna().drop_duplicates()
X_text = df['clean_text']
y = df['label_num']  # 0=ham, 1=spam

# Vectorización de texto
vectorizer = CountVectorizer(stop_words='english')
X_counts = vectorizer.fit_transform(X_text)
tfidf = TfidfTransformer()
X_feats = tfidf.fit_transform(X_counts)

# División entrenamiento / prueba
X_train, X_test, y_train, y_test = train_test_split(
    X_feats, y, test_size=0.2, random_state=42
)

# 2. Modelos supervisados definidos
models = {
    "K-Nearest Neighbors": KNeighborsClassifier(n_neighbors=5),
    "Árbol de Decisión": DecisionTreeClassifier(random_state=42),
    "Naive Bayes": MultinomialNB()
}

# 3. Entrenamiento, evaluación y matrices de confusión gráficas
for name, model in models.items():
    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)

    # Métricas de desempeño
    acc = accuracy_score(y_test, y_pred)
    prec = precision_score(y_test, y_pred, zero_division=0)
    rec = recall_score(y_test, y_pred, zero_division=0)
    f1 = f1_score(y_test, y_pred, zero_division=0)
    print(f"\n{name}:")
    print(f"  Accuracy: {acc:.3f} | Precisión: {prec:.3f} | Recall: {rec:.3f} | F1-score: {f1:.3f}")

    # Matriz de confusión gráfica
    cm = confusion_matrix(y_test, y_pred)
    disp = ConfusionMatrixDisplay(confusion_matrix=cm, display_labels=['ham', 'spam'])
    disp.plot(cmap='Blues', xticks_rotation='horizontal')
    plt.title(f'Matriz de Confusión - {name}')
    plt.show()

# 4. Visualización del árbol de decisión
plt.figure(figsize=(12,8))
plot_tree(
    models["Árbol de Decisión"],
    feature_names=vectorizer.get_feature_names_out(),
    class_names=['ham','spam'],
    filled=True, max_depth=3
)
plt.title("Árbol de Decisión (primeros niveles)")
plt.show()

# 5. Clustering con K-Means y coeficiente de silueta
kmeans = KMeans(n_clusters=2, random_state=42)
cluster_labels = kmeans.fit_predict(X_feats)
silhouette_avg = silhouette_score(X_feats, cluster_labels)
print(f"\nK-Means: coeficiente de silueta = {silhouette_avg:.3f}")

# 6. Evaluación post-hoc de K-Means vs etiquetas verdaderas
#    Aunque K-Means es no supervisado, podemos comparar sus clusters con las etiquetas reales:
#    - Asignamos cada cluster a la etiqueta más frecuente dentro de él.
#label_map = {}
#for cl in sorted(set(cluster_labels)):
#    # Seleccionar todas las etiquetas verdaderas de muestras en el cluster 'cl'
#    true_labels = y[cluster_labels == cl]
#    # Etiqueta predominante en este cluster
#    majority = true_labels.mode()[0]
#    label_map[cl] = majority
#    print(f"Cluster {cl} asignado a etiqueta {'spam' if majority==1 else 'ham'}")

# Contar cantidad de spam en cada cluster
spam_counts = {}
for cl in sorted(set(cluster_labels)):
    true_labels = y[cluster_labels == cl]
    spam_count = sum(true_labels)
    spam_counts[cl] = spam_count

# El cluster con más spam será etiquetado como spam (1), el otro como ham (0)
sorted_clusters = sorted(spam_counts.items(), key=lambda x: x[1], reverse=True)
label_map = {
    sorted_clusters[0][0]: 1,  # más spam
    sorted_clusters[1][0]: 0   # menos spam
}

# Imprimir asignaciones
for cl in sorted(label_map.keys()):
    print(f"Cluster {cl} asignado a etiqueta {'spam' if label_map[cl]==1 else 'ham'}")

# Predicción de etiqueta usando la asignación de clusters
y_km_pred = [label_map[cl] for cl in cluster_labels]

# Métricas para K-Means comparado con etiquetas reales
acc_km = accuracy_score(y, y_km_pred)
prec_km = precision_score(y, y_km_pred, zero_division=0)
rec_km = recall_score(y, y_km_pred, zero_division=0)
f1_km = f1_score(y, y_km_pred, zero_division=0)
print(f"\nK-Means post-hoc contra etiquetas reales:")
print(f"  Accuracy: {acc_km:.3f} | Precisión: {prec_km:.3f} | Recall: {rec_km:.3f} | F1-score: {f1_km:.3f}")

# Matriz de confusión gráfica para K-Means
cm_km = confusion_matrix(y, y_km_pred)
disp_km = ConfusionMatrixDisplay(confusion_matrix=cm_km, display_labels=['ham', 'spam'])
disp_km.plot(cmap='Blues')
plt.title('Matriz de Confusión - K-Means post-hoc')
plt.show()

# 7. Visualización de clusters (PCA 2D con centroides)
pca = PCA(n_components=2)
coords = pca.fit_transform(X_feats.toarray())
plt.figure(figsize=(6,6))
plt.scatter(coords[:,0], coords[:,1], c=cluster_labels, alpha=0.5, s=10)
cent_coords = pca.transform(kmeans.cluster_centers_)
plt.scatter(cent_coords[:,0], cent_coords[:,1], marker='X', s=200, label='Centroides')
plt.legend()
plt.title("K-Means: Visualización de clusters (PCA 2D)")
plt.show()

# Preprocesar como en el dataset limpio
def preprocess_user_text(text):
    stop_words = set(stopwords.words('english'))
    lemmatizer = WordNetLemmatizer()

    text = text.lower()
    text = re.sub(r'^(subject|re|fwd)\s*[:\-]*\s*', '', text)
    text = re.sub(r'\b(subject|re|fwd|from|to|cc|bcc|date)\b\s*[:\-]*', '', text)
    text = re.sub(f"[{re.escape(string.punctuation)}]", " ", text)
    text = re.sub(r'\d+', ' ', text)
    text = re.sub(r'\s+', ' ', text).strip()

    tokens = word_tokenize(text)
    cleaned = [lemmatizer.lemmatize(word) for word in tokens if word not in stop_words]
    return ' '.join(cleaned)

print("📬 Clasificador de Correos Spam/Ham v1.0")

while True:
    print("MENU:")
    print("1. Probar con dataset")
    print("2. Probar con un correo específico")
    print("3. Salir")
    sw = input()

    if sw == '1':
        # 8. Predicción interactiva para un correo dado por ID
        while True:
            entrada = input(f"\nID correo (0-{len(df)-1}) o 'salir': ")
            if entrada.lower() in ['salir','exit']:
                break
            try:
                i = int(entrada)
                if i < 0 or i >= len(df):
                    raise ValueError
            except ValueError:
                print("ID inválido. Intente nuevamente.")
                continue

            texto = [df.loc[i,'clean_text']]
            X_new = tfidf.transform(vectorizer.transform(texto))
            print(f"\nPredicciones para ID={i}:")
            for name, model in models.items():
                pred = model.predict(X_new)[0]
                etiqueta = 'spam' if pred==1 else 'ham'
                print(f"  {name}: {etiqueta}")

            # K-Means predictivo
            cl_new = kmeans.predict(X_new)[0]
            etiqueta_cl = 'spam' if label_map[cl_new]==1 else 'ham'
            print(f"  K-Means cluster={cl_new} (etiqueta asignada: {etiqueta_cl})")
    elif sw == '2':
        print("\n🧪 Prueba personalizada: escribe tu propio correo para clasificarlo.")

        while True:
            entrada = input("\nEscribe el contenido del correo (o 'salir'): ")
            if entrada.lower() in ['salir', 'exit']:
                break

            # Preprocesar como en el dataset limpio
            print("→ Preprocesando...")
            correo_limpio = preprocess_user_text(entrada)

            # Vectorizar el texto
            print("→ Vectorizando...")
            X_new = tfidf.transform(vectorizer.transform([correo_limpio]))

            print("→ Clasificando...")
            print("\n📍 Clasificación del correo ingresado:")
            for name, model in models.items():
                pred = model.predict(X_new)[0]
                etiqueta = '📩 ham' if pred == 0 else '🚨 spam'
                print(f"  {name}: {etiqueta}")

            # Clasificación con K-Means
            cl_new = kmeans.predict(X_new)[0]
            etiqueta_km = '📩 ham' if label_map[cl_new] == 0 else '🚨 spam'
            print(f"  K-Means (no supervisado): cluster={cl_new} → {etiqueta_km}")
    elif sw == '3':
        break
    else:
        print("Opción no válida. Intente nuevamente.")
        continue


