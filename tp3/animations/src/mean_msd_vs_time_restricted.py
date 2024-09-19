import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def read_average_csv(file_path):
    # Leer el archivo CSV que ya contiene los promedios y desvíos estándar
    df = pd.read_csv(file_path)
    df['time'] = df['time']  # Convertir el tiempo a segundos si es necesario
    return df

def plot_scatter_with_regression(df, output_image, t_max):
    # Filtrar los datos para mostrar solo los valores hasta t_max
    filtered_df = df[df['time'] <= t_max]

    plt.figure(figsize=(10, 6))

    # Ajustar el desvío estándar para que no sea negativo (evitar valores por debajo de 0)
    lower_error = np.maximum(filtered_df['msd_mean'] - filtered_df['msd_std'], 0)

    # Calcular el error superior para las barras de error
    upper_error = filtered_df['msd_mean'] + filtered_df['msd_std']

    # Graficar los puntos y barras de error usando el desvío estándar ajustado
    plt.errorbar(filtered_df['time'], filtered_df['msd_mean'],
                 yerr=[filtered_df['msd_mean'] - lower_error, upper_error - filtered_df['msd_mean']],
                 fmt='o', color='blue', ecolor='blue', capsize=3)

    # Ajustar una línea de regresión forzada a pasar por (0,0)
    slope = np.sum(filtered_df['time'] * filtered_df['msd_mean']) / np.sum(filtered_df['time']**2)

    # Crear los valores de tiempo para la línea de regresión
    reg_line = slope * filtered_df['time']

    # Graficar la línea de regresión
    plt.plot(filtered_df['time'], reg_line, color='green', label=f'Pendiente = {slope:.4e}')

    # Etiquetas y formato del gráfico
    plt.xlabel('Tiempo (s)')
    plt.ylabel('DCM (m²)')
    plt.grid(True)

    # Agregar el texto de la pendiente al gráfico
    plt.text(0.05, 0.95, f'Pendiente = {slope:.4e}', transform=plt.gca().transAxes,
             fontsize=12, verticalalignment='top')

    # Eliminar notación científica del eje X
    plt.ticklabel_format(style='plain', axis='x')

    # Agregar leyenda
    plt.legend()

    plt.savefig(output_image)  # Guardar el gráfico como imagen
    plt.show()

# Definir el archivo con los promedios y desvíos estándar
file_path = 'average_msd.csv'  # Nombre del archivo CSV que ya tienes con los promedios
output_image = 'filtered_msd_plot_with_regression.png'  # Nombre del archivo de imagen
t_max = 0.6  # Define el valor máximo de tiempo (en segundos)

# Leer el archivo que ya tiene promedios y desvíos estándar
averaged_df = read_average_csv(file_path)

# Graficar los resultados con la línea de regresión lineal hasta t_max
plot_scatter_with_regression(averaged_df, output_image, t_max)
