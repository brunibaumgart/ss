import matplotlib.pyplot as plt
import pandas as pd

from constants import FilePaths

# Datos de ejemplo
N = [10, 50, 100, 500, 1000]  # Número de partículas
time = [0.5, 1.5, 3.0, 15.0, 30.0]  # Tiempo en segundos

# Read the data from the space-separated file
data = pd.read_csv(FilePaths.FILE_QTY_PARTICLES_VS_TIME, delimiter=' ', header=None, names=['N', 'time'])

# Crear la figura y el eje
fig, ax = plt.subplots()

# Graficar los datos
ax.plot(N, time, marker='o', linestyle='-', color='b')

# Configurar etiquetas
ax.set_xlabel('Número de partículas (N)')
ax.set_ylabel('Tiempo (segundos)')
ax.set_title('Tiempo vs Número de partículas')

# Mostrar la cuadrícula
ax.grid(True)

# Mostrar la gráfica
plt.show()
