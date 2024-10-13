import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter

from src.constants import FilePaths

# Definir los valores de k y los nombres de los archivos
sqrt_k = np.sqrt([100, 1000, 2500, 5000, 10000])
w_max_values = [9.9, 31.4, 49.7, 70.2, 99.3]

# Base del nombre del archivo
base_filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w_k_'

def mse(m, sqrt_k, w0_values):
    # Calcula w0 predicho para un m dado
    w0_pred = m * sqrt_k
    # Calcula el MSE (error cuadrático medio)
    return np.mean((w0_pred - w0_values) ** 2)

# Rango de valores de m que queremos probar
m_values = np.linspace(0.992, 0.995, 5000)
mse_values = [mse(m, sqrt_k, w_max_values) for m in m_values]

# Encontrar el m que minimiza el MSE
m_min = m_values[np.argmin(mse_values)]

# Imprimir el valor óptimo de m
print(f'El valor de m que minimiza el MSE es: {m_min:.4f}')

# Graficar con un marcador en el valor óptimo
plt.plot(m_values, mse_values, color='blue')
plt.axvline(m_min, color='red', linestyle='--', label=f'm = {m_min:.4f}')

# Ajustar el tamaño de la fuente
plt.xlabel(r"m (1/$\sqrt{\text{kg}}$)", fontsize=14)
plt.ylabel(r"Error Cuadrático Medio ($1/\text{s}^2$)", fontsize=14)
plt.legend(loc='upper left', fontsize=12)

# Ajustar los límites del gráfico para empezar en (0, 0)
plt.ylim([0, max(mse_values)*1.1])  # Dejar un poco de espacio por encima del valor máximo del MSE

# Obtener el objeto de los ejes
ax = plt.gca()

# Configurar el formato de los ejes con notación científica en ambos
formatter = ScalarFormatter(useMathText=True)
formatter.set_powerlimits((-2, 2))

# Aplicar el formato en ambos ejes
ax.xaxis.set_major_formatter(formatter)
ax.yaxis.set_major_formatter(formatter)

# Personalizar la posición del texto de notación científica en los ejes
ax.ticklabel_format(axis='x', style='sci')
ax.ticklabel_format(axis='y', style='sci')

# Asegurarse de que se muestre el multiplicador (1x10^-2) en ambos ejes
ax.xaxis.get_offset_text().set_visible(True)
ax.yaxis.get_offset_text().set_visible(True)

# Cambiar el formato de la base del exponente para que aparezca 1x10^-2
ax.yaxis.get_offset_text().set_text(r"$\times 10^{-2}$")
ax.yaxis.get_offset_text().set_fontsize(12)  # Agrandar la fuente

# Ajustar los ticks y el grid
plt.xticks(fontsize=12)
plt.yticks(fontsize=12)
plt.grid(True)
plt.tight_layout()
plt.show()
