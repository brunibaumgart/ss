import matplotlib.pyplot as plt

from src.constants import FilePaths

# Inicializamos listas para almacenar tiempo y posición
times = []
positions = []

# Leemos el archivo y extraemos los datos
with open(FilePaths.SIMULATIONS_DIR + 'ej1.txt', 'r') as file:
    for line in file:
        time, position = map(float, line.split())
        times.append(time)
        positions.append(position)

# Graficamos posición vs tiempo
plt.plot(times, positions)
plt.xlabel('Time')
plt.ylabel('Position')
plt.grid(True)
plt.show()
