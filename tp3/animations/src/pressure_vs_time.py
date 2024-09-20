import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter

from src.constants.FilePaths import SIMULATIONS_DIR, PRESSURES

# Función para leer el archivo pressures.txt y obtener los datos
def leer_presiones(ruta_archivo):
    intervalos_tiempo = []
    presiones_pared = []
    presiones_obstaculo = []

    # Leer el archivo línea por línea
    with open(ruta_archivo, 'r') as archivo:
        for linea in archivo:
            # Separar los valores de cada línea
            valores = linea.split()
            intervalos_tiempo.append(float(valores[0]))  # Intervalo de tiempo
            presiones_pared.append(float(valores[1]))    # Presión de la pared
            presiones_obstaculo.append(float(valores[2]))  # Presión del obstáculo

    return np.array(intervalos_tiempo), np.array(presiones_pared), np.array(presiones_obstaculo)

# Función para graficar las presiones
def graficar_presiones(intervalos_tiempo, presiones_pared, presiones_obstaculo):
    plt.figure(figsize=(10, 6))

    # Configurar el tamaño de la fuente para las etiquetas y leyendas
    plt.rc('font', size=14)               # Tamaño general de la fuente
    plt.rc('axes', titlesize=16)          # Tamaño del título del gráfico
    plt.rc('axes', labelsize=16)          # Tamaño de las etiquetas de los ejes
    plt.rc('xtick', labelsize=14)         # Tamaño de las etiquetas del eje X
    plt.rc('ytick', labelsize=14)         # Tamaño de las etiquetas del eje Y
    plt.rc('legend', fontsize=14)         # Tamaño de la fuente de la leyenda

    # Graficar la presión de la pared
    plt.plot(intervalos_tiempo, presiones_pared, label='Presión en las paredes', color='blue', linewidth=2)

    # Graficar la presión del obstáculo
    plt.plot(intervalos_tiempo, presiones_obstaculo, label='Presión en el obstáculo', color='red', linewidth=2)

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (Pa)')

    # Configurar el eje Y para mostrar 10^6 una sola vez en el formato correcto
    ax = plt.gca()
    ax.yaxis.set_major_formatter(ScalarFormatter(useMathText=True))
    ax.ticklabel_format(style='sci', axis='y', scilimits=(6, 6))

    # Establecer el formato del label de escala
    ax.yaxis.get_offset_text().set_fontsize(14)  # Tamaño del texto del exponente
    ax.yaxis.get_offset_text().set_text(r'$\times 10^{6}$')  # Formato de LaTeX para mostrar "x 10^6"

    # Rotar las etiquetas del eje X para mejor legibilidad
    plt.xticks(rotation=45)

    # Agregar leyenda en la parte superior derecha
    plt.legend(loc='upper right')

    # Mostrar la gráfica
    plt.grid(True)
    plt.tight_layout()  # Ajustar el layout para evitar solapamientos
    plt.show()

# Ruta al archivo pressures.txt (coloca tu ruta correcta aquí)
ruta_archivo = SIMULATIONS_DIR + PRESSURES

# Leer los datos del archivo
intervalos_tiempo, presiones_pared, presiones_obstaculo = leer_presiones(ruta_archivo)

# Graficar los datos
graficar_presiones(intervalos_tiempo, presiones_pared, presiones_obstaculo)
