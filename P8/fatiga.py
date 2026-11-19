import tkinter as tk
from tkinter import ttk
import matplotlib.pyplot as plt

import numpy as np
import skfuzzy as fuzz
from skfuzzy import control as ctrl

# Se evaluará la fatiga de un trabajador en base a :
# - Horas frente a la pantalla
# - Número de interrupciones du|rante el día
# - Nivel de ruido ambiental

# Universos para entradas y salida
x_horas = np.arange(0, 10.1, 0.1)  # 0 a 10 horas
x_interrupciones = np.arange(0, 11, 1)  # 0 a 10 interrupciones
x_ruido = np.arange(0, 100.1, 0.1)  # 0 a 100 dB de ruido

x_fatiga = np.arange(0, 10.1, 0.1) # 0 a 10 de fatiga

# Variables lingüísticas de entrada
horas = ctrl.Antecedent(x_horas, 'horas')
interrupciones = ctrl.Antecedent(x_interrupciones, 'interrupciones')
ruido = ctrl.Antecedent(x_ruido, 'ruido')

# Variable linguistica de salida
fatiga = ctrl.Consequent(x_fatiga, 'fatiga')

# Funciones de membresía para horas
horas['pocas'] = fuzz.zmf(x_horas, 2, 4)
horas['moderadas'] = fuzz.trapmf(x_horas, [3, 5, 6, 7])
horas['muchas'] = fuzz.smf(x_horas, 6, 8)

# Funciones de membresía para interrupciones
interrupciones['pocas'] = fuzz.zmf(x_interrupciones, 2, 4)
interrupciones['moderadas'] = fuzz.trapmf(x_interrupciones, [3, 5, 6, 7])
interrupciones['muchas'] = fuzz.smf(x_interrupciones, 6, 8)

# Funciones de membresía para ruido
ruido['bajo'] = fuzz.zmf(x_ruido, 30, 50)
ruido['medio'] = fuzz.trapmf(x_ruido, [40, 60, 70, 80])
ruido['alto'] = fuzz.smf(x_ruido, 70, 85)

# Funciones de membresía para fatiga
fatiga['baja'] = fuzz.zmf(x_fatiga, 2, 4)
fatiga['media'] = fuzz.trapmf(x_fatiga, [3, 5, 6, 7])
fatiga['alta'] = fuzz.smf(x_fatiga, 6, 8)

rules = [
    ctrl.Rule(horas['pocas'] & interrupciones['pocas'] & ruido['bajo'],         fatiga['baja']),
    ctrl.Rule(horas['pocas'] & interrupciones['pocas'] & ruido['medio'],        fatiga['baja']),
    ctrl.Rule(horas['pocas'] & interrupciones['pocas'] & ruido['alto'],         fatiga['media']),
    ctrl.Rule(horas['pocas'] & interrupciones['moderadas'] & ruido['bajo'],     fatiga['baja']),
    ctrl.Rule(horas['pocas'] & interrupciones['moderadas'] & ruido['medio'],    fatiga['media']),
    ctrl.Rule(horas['pocas'] & interrupciones['moderadas'] & ruido['alto'],     fatiga['alta']),
    ctrl.Rule(horas['pocas'] & interrupciones['muchas'] & ruido['bajo'],        fatiga['media']),
    ctrl.Rule(horas['pocas'] & interrupciones['muchas'] & ruido['medio'],       fatiga['alta']),
    ctrl.Rule(horas['pocas'] & interrupciones['muchas'] & ruido['alto'],        fatiga['alta']),

    ctrl.Rule(horas['moderadas'] & interrupciones['pocas'] & ruido['bajo'],     fatiga['baja']),
    ctrl.Rule(horas['moderadas'] & interrupciones['pocas'] & ruido['medio'],    fatiga['media']),
    ctrl.Rule(horas['moderadas'] & interrupciones['pocas'] & ruido['alto'],     fatiga['media']),
    ctrl.Rule(horas['moderadas'] & interrupciones['moderadas'] & ruido['bajo'], fatiga['media']),
    ctrl.Rule(horas['moderadas'] & interrupciones['moderadas'] & ruido['medio'],fatiga['alta']),
    ctrl.Rule(horas['moderadas'] & interrupciones['moderadas'] & ruido['alto'], fatiga['alta']),
    ctrl.Rule(horas['moderadas'] & interrupciones['muchas'] & ruido['bajo'],    fatiga['media']),
    ctrl.Rule(horas['moderadas'] & interrupciones['muchas'] & ruido['medio'],   fatiga['alta']),
    ctrl.Rule(horas['moderadas'] & interrupciones['muchas'] & ruido['alto'],    fatiga['alta']),

    ctrl.Rule(horas['muchas'] & interrupciones['pocas'] & ruido['bajo'],        fatiga['media']),
    ctrl.Rule(horas['muchas'] & interrupciones['pocas'] & ruido['medio'],       fatiga['alta']),
    ctrl.Rule(horas['muchas'] & interrupciones['pocas'] & ruido['alto'],        fatiga['alta']),
    ctrl.Rule(horas['muchas'] & interrupciones['moderadas'] & ruido['bajo'],    fatiga['media']),
    ctrl.Rule(horas['muchas'] & interrupciones['moderadas'] & ruido['medio'],   fatiga['alta']),
    ctrl.Rule(horas['muchas'] & interrupciones['moderadas'] & ruido['alto'],    fatiga['alta']),
    ctrl.Rule(horas['muchas'] & interrupciones['muchas'] & ruido['bajo'],       fatiga['alta']),
    ctrl.Rule(horas['muchas'] & interrupciones['muchas'] & ruido['medio'],      fatiga['alta']),
    ctrl.Rule(horas['muchas'] & interrupciones['muchas'] & ruido['alto'],       fatiga['alta']),
]
# pocas, pocas, bajo
# pocas, pocas, medio
# pocas, pocas, alto
# pocas, moderadas, bajo
# pocas, moderadas, medio
# pocas, moderadas, alto
# pocas, muchas, bajo
# pocas, muchas, medio
# pocas, muchas, alto
# moderadas, pocas, bajo
# moderadas, pocas, medio
# moderadas, pocas, alto
# moderadas, moderadas, bajo
# moderadas, moderadas, medio
# moderadas, moderadas, alto
# moderadas, muchas, bajo
# moderadas, muchas, medio
# moderadas, muchas, alto
# muchas, pocas, bajo
# muchas, pocas, medio
# muchas, pocas, alto
# muchas, moderadas, bajo
# muchas, moderadas, medio
# muchas, moderadas, alto
# muchas, muchas, bajo
# muchas, muchas, medio
# muchas, muchas, alto

sistema = ctrl.ControlSystem(rules)

# Graficar las funciones de membresía
def mostrar_graficas():
    fig, axs = plt.subplots(4, 1, figsize=(10, 16))

    # Horas
    axs[0].plot(x_horas, horas['pocas'].mf, label='pocas')
    axs[0].plot(x_horas, horas['moderadas'].mf, label='moderadas')
    axs[0].plot(x_horas, horas['muchas'].mf, label='muchas')
    axs[0].set_title('Horas frente a la pantalla (0 a 10)')
    axs[0].set_xticks(np.arange(0, 11, 1))
    axs[0].legend()
    axs[0].grid(True)

    # Interrupciones
    axs[1].plot(x_interrupciones, interrupciones['pocas'].mf, label='pocas')
    axs[1].plot(x_interrupciones, interrupciones['moderadas'].mf, label='moderadas')
    axs[1].plot(x_interrupciones, interrupciones['muchas'].mf, label='muchas')
    axs[1].set_title('Interrupciones (0 a 10)')
    axs[1].set_xticks(np.arange(0, 11, 1))
    axs[1].legend()
    axs[1].grid(True)

    # Ruido
    axs[2].plot(x_ruido, ruido['bajo'].mf, label='bajo')
    axs[2].plot(x_ruido, ruido['medio'].mf, label='medio')
    axs[2].plot(x_ruido, ruido['alto'].mf, label='alto')
    axs[2].set_title('Ruido ambiental (0 a 100 dB)')
    axs[2].set_xticks(np.arange(0, 101, 10))
    axs[2].legend()
    axs[2].grid(True)

    # Fatiga
    axs[3].plot(x_fatiga, fatiga['baja'].mf, label='baja')
    axs[3].plot(x_fatiga, fatiga['media'].mf, label='media')
    axs[3].plot(x_fatiga, fatiga['alta'].mf, label='alta')
    axs[3].set_title('Nivel de fatiga (0 a 10)')
    axs[3].set_xticks(np.arange(0, 11, 1))
    axs[3].legend()
    axs[3].grid(True)

    plt.tight_layout()
    plt.show()

def calcular(event=None):
    simulador = ctrl.ControlSystemSimulation(sistema)
    # Obtener valores de los sliders
    horas_val = float(horas_slider.get())
    interrupciones_val = float(interrupciones_slider.get())
    ruido_val = float(ruido_slider.get())

    horas_valor.set(f"{horas_val:.1f}")
    interrupciones_valor.set(f"{interrupciones_val:.1f}")
    ruido_valor.set(f"{ruido_val:.1f}")

    # Ingresar valores al simulador
    simulador.input['horas'] = horas_val
    simulador.input['interrupciones'] = interrupciones_val
    simulador.input['ruido'] = ruido_val

    # Calcular la salida
    simulador.compute()

    # Actualizar el resultado en la GUI
    resultado_var.set(f"Nivel de fatiga: {simulador.output['fatiga']:.2f}")

# Crear GUI
ventana = tk.Tk()
ventana.title("Sistema Difuso de Fatiga")
ventana.geometry("500x500")

frame = tk.Frame(ventana)
frame.pack(pady=10, fill='x')

tk.Label(ventana, text="Evaluación de fatiga del trabajador", font=('Arial', 14, 'bold')).pack(pady=5)

# Horas slider
tk.Label(frame, text="Horas frente a pantalla (0 a 10):").pack(anchor='w', padx=10)
horas_slider = ttk.Scale(frame, from_=0, to=10, orient='horizontal', command=calcular)
horas_slider.pack(fill='x', padx=10)
horas_valor = tk.StringVar(value="0.0")
tk.Label(frame, textvariable=horas_valor).pack(anchor='e', padx=10)

# Interrupciones slider
tk.Label(frame, text="Número de interrupciones (0 a 10):").pack(anchor='w', padx=10)
interrupciones_slider = ttk.Scale(frame, from_=0, to=10, orient='horizontal', command=calcular)
interrupciones_slider.pack(fill='x', padx=10)
interrupciones_valor = tk.StringVar(value="0.0")
tk.Label(frame, textvariable=interrupciones_valor).pack(anchor='e', padx=10)

# Ruido slider
tk.Label(frame, text="Nivel de ruido (0 a 100 dB):").pack(anchor='w', padx=10)
ruido_slider = ttk.Scale(frame, from_=0, to=100, orient='horizontal', command=calcular)
ruido_slider.pack(fill='x', padx=10)
ruido_valor = tk.StringVar(value="0.0")
tk.Label(frame, textvariable=ruido_valor).pack(anchor='e', padx=10)

# Resultado
resultado_var = tk.StringVar()
tk.Label(ventana, textvariable=resultado_var, font=('Arial', 12), fg='white').pack(pady=10)
tk.Button(ventana, text="Ver funciones de membresía", command=mostrar_graficas).pack(pady=5)

ventana.mainloop()