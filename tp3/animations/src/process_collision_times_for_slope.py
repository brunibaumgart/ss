import os
import numpy as np
import pandas as pd
from scipy.stats import linregress

from src.constants import FilePaths


# Función para leer los tiempos de colisión desde el archivo
def read_collision_times(file_path):
    times = []
    with open(file_path, 'r') as f:
        for line in f:
            if line.startswith("Time"):
                _, time_str = line.split()
                times.append(float(time_str))
    return times


# Función para calcular la pendiente de la recta
def calculate_slope(times):
    # Crear la lista de colisiones (conteo acumulado)
    collisions = list(range(1, len(times) + 1))

    # Ajustar una regresión lineal para obtener la pendiente
    slope, intercept, r_value, p_value, std_err = linregress(times, collisions)

    return slope


# Función para procesar los archivos y guardar las pendientes en un archivo CSV
def process_collision_files(temp, n, folder_path, output_file):
    results = []

    # Iterar sobre los archivos i de 1 a n
    for i in range(1, n + 1):
        file_name = f"obstacle_collision_times_{i}.txt"
        file_path = os.path.join(folder_path, file_name)

        if os.path.exists(file_path):
            # Leer los tiempos de colisión
            times = read_collision_times(file_path)

            # Calcular la pendiente
            slope = calculate_slope(times)

            # Guardar el resultado en la lista
            results.append([temp, i, slope])
        else:
            print(f"Archivo {file_name} no encontrado.")

    # Guardar los resultados en un archivo CSV
    df = pd.DataFrame(results, columns=['temp', 'i', 'slope'])
    df.to_csv(output_file, mode='a', header=not os.path.exists(output_file), index=False)


# Variables a definir
temp = 100  # El valor de la temperatura
n = 5  # El número de archivos (i va de 1 a n)
folder_path = FilePaths.SIMULATIONS_DIR + f"obstacle_collisions/circle/temp_{temp}/mean_calc"
output_file = "collision_slopes_circle.csv"  # Archivo CSV donde se guardan los resultados

# Procesar los archivos y calcular las pendientes
process_collision_files(temp, n, folder_path, output_file)
