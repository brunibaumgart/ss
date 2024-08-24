import matplotlib.pyplot as plt

from src.constants.FilePaths import SIMULATIONS_DIR

# Lista de archivos
archivos = [
    SIMULATIONS_DIR + 'time_vs_va_varying_n/etha_2/time_vs_va__rho_0_5.txt',
    SIMULATIONS_DIR + 'time_vs_va_varying_n/etha_2/time_vs_va__rho_2_5.txt',
    SIMULATIONS_DIR + 'time_vs_va_varying_n/etha_2/time_vs_va__rho_5.txt',
    SIMULATIONS_DIR + 'time_vs_va_varying_n/etha_2/time_vs_va__rho_10.txt',
]
colores = ['b', 'g', 'r', 'c']  # Colores para las curvas

plt.figure(figsize=(10, 6))

for i, archivo in enumerate(archivos):
    time_values = []
    va_values = []
    rho_value = None

    # Leer cada archivo
    with open(archivo, 'r') as file:
        # Leer la primera línea para obtener el valor de etha
        first_line = file.readline().strip()
        rho_value = float(first_line.split()[1])

        file.readline()
        file.readline()

        # Leer el resto del archivo
        for line in file:
            time, va = line.split()
            time_values.append(int(time))
            va_values.append(float(va))

    # Graficar la curva para este archivo
    plt.plot(time_values, va_values, linestyle='-', color=colores[i], label=f'Rho = {rho_value}')

# Etiquetas y título
plt.xlabel('Tiempo')
plt.ylabel('Va')

# Mostrar la leyenda
plt.legend(loc='upper right')

# Mostrar la gráfica
plt.grid(True)
plt.show()
