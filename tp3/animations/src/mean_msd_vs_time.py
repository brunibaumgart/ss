import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter
import numpy as np

def read_and_average_csv(file_pattern, num_files):
    data_frames = []

    # Leer los archivos CSV
    for i in range(1, num_files + 1):
        file_name = file_pattern.format(i)
        df = pd.read_csv(file_name)
        df['time'] = df['time']  # Convertir el tiempo a segundos
        data_frames.append(df)

    # Concatenar todos los DataFrames en uno solo
    combined_df = pd.concat(data_frames)

    # Agrupar por tiempo y calcular el promedio y desvío estándar de MSD
    averaged_df = combined_df.groupby('time').agg(
        msd_mean=('msd', 'mean'),
        msd_std=('msd', 'std')
    ).reset_index()

    return averaged_df

def plot_scatter(df, output_image):
    plt.figure(figsize=(10, 6))

    # Ajustar el desvío estándar para que no sea negativo (evitar valores por debajo de 0)
    lower_error = np.maximum(df['msd_mean'] - df['msd_std'], 0)
    upper_error = df['msd_mean'] + df['msd_std']

    # Graficar los puntos y barras de error usando el desvío estándar
    plt.errorbar(df['time'], df['msd_mean'], yerr=[df['msd_mean'] - lower_error, upper_error - df['msd_mean']],
                 fmt='o', color='blue', ecolor='blue', capsize=3)

    # Etiquetas y formato del gráfico con un tamaño de fuente mayor
    plt.xlabel('Tiempo (s)', fontsize=16)
    plt.ylabel(r'$\text{DCM} \ (m^2)$', fontsize=16)  # Usar LaTeX para m^2 y tamaño de fuente más grande
    plt.xticks(fontsize=14)
    plt.yticks(fontsize=14)
    plt.grid(True)

    # Configurar el eje Y para mostrar 10^-3 una sola vez en el formato correcto
    ax = plt.gca()
    ax.yaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(style='sci', axis='y', scilimits=(-3, -3))  # Forzar notación científica con 10^-3

    # Establecer el formato del label de escala y tamaño del texto del exponente
    ax.yaxis.get_offset_text().set_fontsize(16)  # Tamaño del texto del exponente más grande
    ax.yaxis.get_offset_text().set_text(r'$\times 10^{-3}$')  # Formato de LaTeX para mostrar "x 10^-3"

    plt.savefig(output_image)  # Guardar el gráfico como imagen
    plt.show()

def save_csv(df, output_csv):
    df.to_csv(output_csv, index=False)  # Guardar el DataFrame como archivo CSV

# Definir el patrón de nombre del archivo y el número de archivos
file_pattern = '../output/circle/msd_evolution_{}.csv'
num_files = 10  # Cambia esto al número de archivos que tienes
output_image = 'average_msd_plot.png'  # Nombre del archivo de imagen
output_csv = 'average_msd.csv'  # Nombre del archivo CSV

# Leer los archivos, calcular el promedio y el desvío estándar, guardar el CSV y generar el gráfico
averaged_df = read_and_average_csv(file_pattern, num_files)
save_csv(averaged_df, output_csv)
plot_scatter(averaged_df, output_image)
