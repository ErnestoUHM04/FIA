from flask import Flask, request, jsonify, render_template
import joblib
import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier

app = Flask(__name__)

# Cargar modelo y datos
model_and_features = joblib.load("vgsales_game_quality_model.joblib")
dataframe = pd.read_csv("Video_Games.csv")

# Preparar datos
list_features = list(model_and_features['feature_names'])
sales_features = ['NA_Sales', 'EU_Sales', 'JP_Sales', 'Other_Sales', 'Global_Sales']
list_features.extend(sales_features)

# Asegurar columnas necesarias
if 'Game_Quality_Encoded' not in dataframe.columns:
    required_columns = ['User_Score', 'Critic_Count'] + sales_features
    for col in required_columns:
        if col not in dataframe.columns:
            raise KeyError(f"La columna requerida '{col}' no está en el DataFrame.")

    dataframe['User_Score'] = pd.to_numeric(dataframe['User_Score'], errors='coerce')
    dataframe['User_Score'] = dataframe['User_Score'].fillna(dataframe['User_Score'].median())
    dataframe['Critic_Count'] = dataframe['Critic_Count'].fillna(dataframe['Critic_Count'].median())
    for col in sales_features:
        dataframe[col] = dataframe[col].fillna(dataframe[col].median())

    def classify_game_quality(row):
        if row['Critic_Count'] > 50 and row['User_Score'] >= 8.0:
            return 'Excelente'
        elif row['Critic_Count'] > 20 and row['User_Score'] >= 6.0:
            return 'Bueno'
        elif row['Critic_Count'] > 10 and row['User_Score'] >= 4.0:
            return 'Regular'
        else:
            return 'Malo'

    dataframe['Game_Quality'] = dataframe.apply(classify_game_quality, axis=1)

    if 'label_encoder_quality' not in model_and_features:
        raise KeyError("El modelo cargado no contiene el transformador 'label_encoder_quality'.")
    le_quality = model_and_features['label_encoder_quality']
    dataframe['Game_Quality_Encoded'] = le_quality.transform(dataframe['Game_Quality'])

# Escalar y filtrar características
scaler = StandardScaler()
numeric_columns = dataframe.select_dtypes(include=[np.number]).columns
dataframe[numeric_columns] = dataframe[numeric_columns].fillna(dataframe[numeric_columns].median())
non_numeric_columns = dataframe.select_dtypes(exclude=[np.number]).columns
dataframe[non_numeric_columns] = dataframe[non_numeric_columns].fillna("Unknown")
list_features = [col for col in list_features if col in numeric_columns]
X = dataframe[list_features]
X = scaler.fit_transform(X)

# Entrenar modelo si es necesario
k = int(np.sqrt(X.shape[0]))
k = k + 1 if k % 2 == 0 else k
k_nn_model = KNeighborsClassifier(n_neighbors=k)
k_nn_model.fit(X, dataframe['Game_Quality_Encoded'])

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/search', methods=['POST'])
def search():
    query = request.json.get('query', '').lower()
    results = [game for game in dataframe['Name'].dropna().unique().tolist() if query in game.lower()]
    return jsonify(results=results[:10])

@app.route('/recommend', methods=['POST'])
def recommend():
    # Solicitar 5 juegos seleccionados por el usuario
    selected_games = request.json.get('games', [])
    if len(selected_games) != 5:
        return jsonify(
            error="Debe seleccionar exactamente 5 juegos.",
            selected=selected_games,
            required=5 - len(selected_games),
        ), 400

    # Calcular las medias de los valores de los juegos seleccionados
    selected_data = dataframe[dataframe['Name'].isin(selected_games)][list_features]
    if selected_data.empty:
        return jsonify(error="No se encontraron datos para los juegos seleccionados."), 400

    averages = selected_data.mean()
    user_input = pd.DataFrame([averages], columns=list_features)
    user_input = scaler.transform(user_input)

    # Realizar predicción basada en vecinos cercanos
    distances, indices = k_nn_model.kneighbors(user_input, n_neighbors=10)
    recommendations = dataframe.iloc[indices[0]]['Name'].values.tolist()

    return jsonify(recommendations=recommendations)

if __name__ == '__main__':
    app.run(debug=True)
