import matplotlib.pyplot as plt

from src.constants.FilePaths import TIME_VS_VA_FILE

# Inicializar listas para almacenar los datos
time_values = []
va_values = []

# Leer el archivo
with open(TIME_VS_VA_FILE, 'r') as file:
    # Leer el valor de etha
    first_line = file.readline().strip()
    etha_value = float(first_line.split()[1])
    # Saltar las otras dos líneas
    file.readline()
    file.readline()

    for line in file:
        time, va = line.split()
        time_values.append(int(time))
        va_values.append(float(va))

# Crear el gráfico
plt.figure(figsize=(10, 6))
plt.plot(time_values, va_values, linestyle='-', color='b')

# Etiquetas y título
plt.xlabel('Time')
plt.ylabel('Va')
plt.title(f'Curva de Tiempo vs. Va (Etha = {etha_value})')

# Mostrar la gráfica
plt.grid(True)
plt.show()
