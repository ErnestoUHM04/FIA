# El dataset tiene que ser preprocesado para que sea compatible con el modelo de lenguaje.
#   Conversión de texto a minúsculas.
#   Eliminación de caracteres especiales y signos de puntuación.
#   Eliminación de stopwords (palabras muy frecuentes sin significado relevante).
#   Tokenización (dividir el texto en palabras).
#   Opcionalmente, lematización o stemming para reducir palabras a su raíz.
import pandas as pd
import re
import string
import nltk

#nltk.download('punkt')
#nltk.download('punkt_tab')
#nltk.download('stopwords')
#nltk.download('wordnet')

from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import WordNetLemmatizer

stop_words = set(stopwords.words('english'))
lemmatizer = WordNetLemmatizer()

# Cargar el dataset
df = pd.read_csv('Proyecto Final/spam_ham_dataset.csv')

def preprocess_text(text):
    # Minúsculas
    text = text.lower()

    # Eliminar "subject" si aparece al inicio del texto
    # Eliminar encabezados comunes
    text = re.sub(r'^(subject|re|fwd)\s*[:\-]*\s*', '', text)        # Al inicio
    text = re.sub(r'\b(subject|re|fwd|from|to|cc|bcc|date)\b\s*[:\-]*', '', text)  # En cualquier parte

    # Eliminar caracteres especiales y signos de puntuación
    text = re.sub(f"[{re.escape(string.punctuation)}]", " ", text)

    # Eliminar números
    text = re.sub(r'\d+', ' ', text)

    # Eliminar espacios extra
    text = re.sub(r'\s+', ' ', text).strip()

    # Tokenizar
    tokens = word_tokenize(text)

    # Eliminar stopwords y lematizar
    cleaned = [lemmatizer.lemmatize(word) for word in tokens if word not in stop_words]

    return ' '.join(cleaned)

# Aplicar a la columna de texto
df['clean_text'] = df['text'].apply(preprocess_text)

# Eliminar columna original de texto
df = df.drop(columns=['text'])

# Eliminar filas donde el clean_text quedó vacío
df = df[df['clean_text'].str.strip().astype(bool)]

# Guardar dataset limpio
df.to_csv('Proyecto Final/spam_ham_clean.csv', index=False)
