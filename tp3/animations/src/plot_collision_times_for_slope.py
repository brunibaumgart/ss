import matplotlib.pyplot as plt
import pandas as pd

# Leer el archivo CSV con los datos de pendientes
csv_file = "collision_slopes_circle.csv"  # El archivo CSV que ya generaste
data = pd.read_csv(csv_file)

# Calcular el promedio y desvío estándar de la pendiente para cada temperatura
grouped_data = data.groupby('temp').agg(
    mean_slope=('slope', 'mean'),
    std_slope=('slope', 'std')
).reset_index()

# Graficar promedio y desvío estándar
plt.figure(figsize=(10, 6))

# Gráfico con barras de error
plt.errorbar(grouped_data['temp'], grouped_data['mean_slope'], yerr=grouped_data['std_slope'],
             fmt='o', capsize=5, capthick=2, label='Pendiente promedio')

# Configurar etiquetas y título
plt.xlabel('Temperatura (U.A.)', fontsize=14)
plt.ylabel(r'Pendiente promedio', fontsize=14)  # Usando LaTeX para la t con barra

# Mostrar solo los valores de temperatura en el eje X
plt.xticks(grouped_data['temp'])

# Aumentar el tamaño de la fuente
plt.tick_params(axis='both', which='major', labelsize=12)

# Mostrar la cuadrícula
plt.grid(True)

# Mostrar gráfico
plt.tight_layout()
plt.show()
