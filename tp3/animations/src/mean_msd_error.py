import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


def calculate_error(D, times, msd_mean):
    error = np.sum((msd_mean - 2 * D * times) ** 2)
    return error


def plot_error_vs_D(file_path):
    # Leer los datos del archivo CSV
    df = pd.read_csv(file_path)

    # Extraer las columnas necesarias
    times = df['time'].values
    msd_mean = df['msd_mean'].values

    # Definir un rango de valores para D
    D_values = np.linspace(-0.01, 0.01, 100)

    # Calcular el error para cada valor de D
    errors = [calculate_error(D, times, msd_mean) for D in D_values]

    # Encontrar el valor de D que minimiza el error
    optimal_D = D_values[np.argmin(errors)]

    # Graficar el error en función de D
    plt.figure(figsize=(8, 6))
    plt.plot(D_values, errors, 'b-')
    plt.xlabel('D (m²/s)')
    plt.ylabel('Error(D)')
    plt.grid(True)
    plt.plot(optimal_D, min(errors), 'ro', label=f'D óptimo = {optimal_D:.4e} m²/s')
    plt.legend()

    plt.show()


# Ruta al archivo CSV
file_path = 'average_msd.csv'  # Cambia esto a la ruta de tu archivo CSV

# Ejecutar la función para graficar
plot_error_vs_D(file_path)
