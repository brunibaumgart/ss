import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.ticker import FuncFormatter


def read_and_average_csv(file_pattern, num_files):
    data_frames = []

    # Leer los archivos CSV
    for i in range(1, num_files + 1):
        file_name = file_pattern.format(i)
        df = pd.read_csv(file_name)
        df['time'] = df['time'] / 1000
        data_frames.append(df)

    # Concatenar todos los DataFrames en uno solo
    combined_df = pd.concat(data_frames)

    # Agrupar por tiempo y calcular el promedio de MSD
    averaged_df = combined_df.groupby('time').agg({'msd': 'mean'}).reset_index()

    return averaged_df


def scientific_formatter(x, pos):
    """Format function to show scaled values."""
    return f'{x * 1e3:.1f}'  # Escalar los valores por 10^-3 y mostrar con un decimal


def plot_scatter(df, output_image):
    plt.figure(figsize=(10, 6))
    plt.scatter(df['time'], df['msd'], color='blue', marker='o')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('DCM (x10^-3)')
    plt.grid(True)

    # Ajustar los ejes para mostrar valores escalados
    plt.gca().xaxis.set_major_formatter(FuncFormatter(scientific_formatter))
    plt.gca().yaxis.set_major_formatter(FuncFormatter(scientific_formatter))

    plt.savefig(output_image)  # Guardar el gráfico como imagen
    plt.show()


def save_csv(df, output_csv):
    df.to_csv(output_csv, index=False)  # Guardar el DataFrame como archivo CSV


# Definir el patrón de nombre del archivo y el número de archivos
file_pattern = 'msd_evolution_{}.csv'
num_files = 3  # Cambia esto al número de archivos que tienes
output_image = 'average_msd_plot.png'  # Nombre del archivo de imagen
output_csv = 'average_msd.csv'  # Nombre del archivo CSV

# Leer los archivos, calcular el promedio, guardar el CSV y generar el gráfico
averaged_df = read_and_average_csv(file_pattern, num_files)
save_csv(averaged_df, output_csv)
plot_scatter(averaged_df, output_image)
