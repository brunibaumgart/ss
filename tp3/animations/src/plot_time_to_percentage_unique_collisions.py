import pandas as pd
import matplotlib.pyplot as plt

# Leer el archivo CSV
csv_file = "unique_collision_times_results.csv"
data = pd.read_csv(csv_file)

# Agrupar por el valor de temp y calcular promedio y desviación estándar
grouped_data = data.groupby('temp')['time'].agg(['mean', 'std']).reset_index()

# Crear el gráfico
plt.errorbar(grouped_data['temp'], grouped_data['mean'], yerr=grouped_data['std'], fmt='o', capsize=5)

# Etiquetas de los ejes con mayor tamaño de fuente
plt.xlabel('Temperatura (U.A.)', fontsize=14)
plt.ylabel(r'$\bar{t}_{50\%}$ (s)', fontsize=14)

# Ajustar la escala del eje X para mostrar solo los valores de temp
plt.xticks(grouped_data['temp'], fontsize=12)

# Aumentar tamaño de fuente en el eje Y
plt.yticks(fontsize=12)

# Mostrar la grilla
plt.grid(True)

# Mostrar el gráfico
plt.show()
