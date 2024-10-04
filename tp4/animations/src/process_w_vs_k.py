import os
import re
import csv

from src.constants import FilePaths

# Directorio raíz donde se encuentran las subcarpetas "k_NUMBER"
root_directory = FilePaths.SIMULATIONS_DIR + "/ej2"

# Regex para extraer el valor de w de los nombres de archivos y k de los subdirectorios
subdir_pattern = r"k_(\d+(?:\.\d+)?)"
filename_pattern = r"amplitude_vs_time__k_(\d+(?:\.\d+)?)__w_(\d+(?:\.\d+)?).txt"

# Lista para almacenar los resultados
results = []

# Iterar sobre todas las subcarpetas
for subdir in os.listdir(root_directory):
    subdir_path = os.path.join(root_directory, subdir)

    # Verificar si el subdirectorio coincide con el patrón "k_NUMBER"
    subdir_match = re.match(subdir_pattern, subdir)
    if subdir_match and os.path.isdir(subdir_path):
        k_value = float(subdir_match.group(1))  # Extraer el valor de k

        max_amplitude = float('-inf')  # Inicializar el valor máximo de amplitud
        max_w_value = None  # Inicializar el valor de w para la amplitud máxima

        # Iterar sobre los archivos dentro de la subcarpeta
        for filename in os.listdir(subdir_path):
            filepath = os.path.join(subdir_path, filename)
            match = re.match(filename_pattern, filename)

            if match:
                w_value = float(match.group(2))  # Extraer el valor de w

                # Leer el archivo para encontrar el valor máximo de amplitud
                with open(filepath, 'r') as file:
                    for line in file:
                        time, position_y = map(float, line.split())
                        if position_y > max_amplitude:
                            max_amplitude = position_y
                            max_w_value = w_value  # Guardar el valor de w correspondiente

        # Guardar el resultado en la lista
        results.append([k_value, max_w_value, max_amplitude])

# Ordenar los resultados por el valor de k
results.sort(key=lambda x: x[0])

# Escribir los resultados en un archivo CSV
csv_filepath = os.path.join(".", "max_amplitudes_with_w.csv")
with open(csv_filepath, mode='w', newline='') as csv_file:
    writer = csv.writer(csv_file)
    writer.writerow(["k", "w for Max Amplitude", "Max Amplitude"])  # Encabezados
    writer.writerows(results)

print(f"CSV guardado en: {csv_filepath}")
