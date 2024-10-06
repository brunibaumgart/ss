import matplotlib.pyplot as plt
import os

from src.constants import FilePaths

# Definir los valores de k y los nombres de los archivos
k_values = [100, 1000, 2500, 5000, 10000]
w_max_values = []

# Base del nombre del archivo
base_filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w_k_'

# Leer cada archivo y encontrar el w máximo
for k in k_values:
    filename = f"{base_filename}{k}.txt"
    ws = []

    with open(filename, 'r') as file:
        next(file)
        for line in file:
            _, w = map(float, line.split())  # Leer los valores, ignorar amplitud
            ws.append(w)

    # Guardar el valor máximo de w para cada archivo
    w_max_values.append(max(ws))

# Graficar w_0 en función de k, solo puntos
plt.scatter(k_values, w_max_values, marker='o')

# Etiquetas de los ejes con unidades
plt.xlabel(r'$k$ ($\mathrm{kg/s^2}$)', fontsize=16)
plt.ylabel(r'$\omega_0$ (rad/s)', fontsize=16)

# Configurar los ticks del eje x manualmente para que muestren los valores de k claramente
plt.xticks(k_values, [f"{k}" for k in k_values])

# Configuración del gráfico
plt.grid(True)
plt.tight_layout()
plt.show()
