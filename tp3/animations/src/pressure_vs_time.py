import numpy as np
import matplotlib.pyplot as plt

from src.constants.FilePaths import SIMULATIONS_DIR, PRESSURES


# Función para leer el archivo pressures.txt y obtener los datos
def read_pressures(file_path):
    time_intervals = []
    wall_pressures = []
    obstacle_pressures = []

    # Leer el archivo línea por línea
    with open(file_path, 'r') as file:
        for line in file:
            # Separar los valores de cada línea
            values = line.split()
            time_intervals.append(float(values[0]))  # Intervalo de tiempo
            wall_pressures.append(float(values[1]))  # Wall pressure
            obstacle_pressures.append(float(values[2]))  # Obstacle pressure

    return np.array(time_intervals), np.array(wall_pressures), np.array(obstacle_pressures)

# Función para graficar las presiones
def plot_pressures(time_intervals, wall_pressures, obstacle_pressures):
    plt.figure(figsize=(10, 6))

    # Graficar la presión de la pared
    plt.plot(time_intervals, wall_pressures, label='Wall Pressure', color='blue', linewidth=2)

    # Graficar la presión del obstáculo
    plt.plot(time_intervals, obstacle_pressures, label='Obstacle Pressure', color='red', linewidth=2)

    # Eliminar el título del gráfico
    # plt.title() no se incluye

    # Etiquetas de los ejes
    plt.xlabel('Time (s)')
    plt.ylabel('Pressure (Pa)')

    # Permitir que Matplotlib ajuste automáticamente las etiquetas del eje X
    plt.xticks(rotation=45)  # Rotar las etiquetas para una mejor legibilidad

    # Agregar leyenda
    plt.legend()

    # Mostrar la gráfica
    plt.grid(True)
    plt.tight_layout()  # Ajustar el layout para evitar solapamientos
    plt.show()

# Ruta al archivo pressures.txt
file_path = SIMULATIONS_DIR + PRESSURES

# Leer los datos del archivo
time_intervals, wall_pressures, obstacle_pressures = read_pressures(file_path)

# Graficar los datos
plot_pressures(time_intervals, wall_pressures, obstacle_pressures)
