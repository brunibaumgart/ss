import csv
import matplotlib.pyplot as plt
import numpy as np

from src.constants.FilePaths import SIMULATIONS_DIR

# Parámetro m, lo defines manualmente
m = 0.001  # Cambia este valor según lo que desees

# Archivo CSV con los resultados
csv_filepath = "./max_amplitudes_with_w.csv"

# Listas para almacenar los valores de sqrt(k)/m y w
sqrt_k_over_m_values = []
w_values = []

# Leer el archivo CSV
with open(csv_filepath, mode='r') as csv_file:
    reader = csv.DictReader(csv_file)

    for row in reader:
        k = float(row['k'])
        w = float(row['w for Max Amplitude'])

        sqrt_k_over_m = np.sqrt(k / m)
        sqrt_k_over_m_values.append(sqrt_k_over_m)
        w_values.append(w)

# Convertir listas a arrays numpy y ordenar por sqrt(k)/m si es necesario
sqrt_k_over_m_values = np.array(sqrt_k_over_m_values)
w_values = np.array(w_values)

# Graficar sqrt(k)/m vs. w
plt.plot(sqrt_k_over_m_values, w_values, marker='o', linestyle='-', color='b')
plt.xlabel(r"$\sqrt{\frac{k}{m}}$")
plt.ylabel(r"$\omega$ (rad/s)")
plt.grid(True)

# Mostrar gráfico
plt.show()
