import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter

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

    # Configurar los ejes
    plt.xlabel('D (m²/s)', fontsize=14)
    plt.ylabel('Error(D)', fontsize=14)
    plt.xticks(fontsize=14)
    plt.yticks(fontsize=14)
    plt.grid(True)

    # Plotear el punto óptimo
    plt.plot(optimal_D, min(errors), 'ro')

    # Ajustar notación científica para los ejes
    ax = plt.gca()
    ax.yaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.xaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(style='sci', axis='x', scilimits=(-2, -2))
    ax.ticklabel_format(style='sci', axis='y', scilimits=(-2, -2))
    ax.yaxis.get_offset_text().set_fontsize(14)
    ax.xaxis.get_offset_text().set_fontsize(14)

    plt.tight_layout()  # Ajuste para evitar que se corte el gráfico
    plt.show()

# Ruta al archivo CSV
file_path = '../output/circle/average_msd_deltat_0_04.csv'  # Cambia esto a la ruta de tu archivo CSV

# Ejecutar la función para graficar
plot_error_vs_D(file_path)
