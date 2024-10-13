import numpy as np
import matplotlib.pyplot as plt
import os

from src.constants import FilePaths

# Valor de m obtenido del ajuste (puedes cambiarlo por el valor que calculaste)
m_min = 0.9931 # ejemplo

# Definir los valores de k y los nombres de los archivos
k_values = [100, 1000, 2500, 5000, 10000]
w_max_values = []

# Base del nombre del archivo
base_filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w_k_'

# Leer cada archivo y encontrar el w máximo
for k in k_values:
    filename = f"{base_filename}{k}.txt"
    amplitudes = []
    ws = []

    with open(filename, 'r') as file:
        next(file)  # Omitir el encabezado
        for line in file:
            a, w = map(float, line.split())  # Leer amplitud y w
            amplitudes.append(a)
            ws.append(w)

    # Encontrar el índice de la amplitud máxima
    max_index = amplitudes.index(max(amplitudes))

    # Guardar el w correspondiente a la amplitud máxima
    w_max_values.append(ws[max_index])

# Calcular la curva ajustada m * sqrt(k)

# Graficar w_0 en función de k (puntos originales)
plt.plot(k_values, w_max_values, 'o')

# Graficar la curva ajustada m * sqrt(k)
x = np.linspace(k_values[0], k_values[-1], 1000)
y = [m_min * np.sqrt(k) for k in x]
plt.plot(x, y, label=r'$m * \sqrt{k}$', color='red')

# Etiquetas de los ejes con unidades
plt.xlabel(r'$k$ ($\mathrm{kg/s^2}$)', fontsize=16)
plt.ylabel(r'$\omega_0$ ($\text{s}^{-1}$)', fontsize=16)

# Configurar los ticks del eje x manualmente para que muestren los valores de k claramente
plt.xticks(k_values, [f"{k}" for k in k_values], fontsize=13)
plt.yticks(w_max_values, [f"{w:.1f}" for w in w_max_values], fontsize=12)

# Configuración del gráfico
plt.grid(True)
plt.legend()
plt.tight_layout()
plt.show()
