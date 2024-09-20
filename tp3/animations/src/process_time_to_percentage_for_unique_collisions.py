import os
import csv

from src.constants import FilePaths


# Read the file and extract times
def read_times(file_path):
    times = []
    with open(file_path, 'r') as f:
        for line in f:
            if line.startswith("Time"):
                _, time_str = line.split()
                times.append(float(time_str))
    return times


# Calculate the time to reach a certain percentage of total particles
def time_to_reach_percentage(times, percentage, total_particles):
    threshold = percentage * total_particles
    sorted_times = sorted(times)  # Sort times to find when 50% have collided
    if len(sorted_times) >= threshold:
        return sorted_times[int(threshold) - 1]  # -1 because of 0-based index
    return None  # Return None if not enough particles


# Process multiple files and store results in a CSV
def process_files_and_save_results(file_directory, temp, n_files, total_particles, percentage, output_file):
    with open(output_file, 'a', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['temp', 'i', 'time'])  # Write CSV header

        for i in range(1, n_files + 1):
            file_path = f"{file_directory}/unique_obstacle_collision_times_{i}.txt"
            if os.path.exists(file_path):
                times = read_times(file_path)
                time_at_percentage = time_to_reach_percentage(times, percentage, total_particles)

                if time_at_percentage is not None:
                    writer.writerow([temp, i, time_at_percentage])
                else:
                    print(f"No se alcanzó el {percentage * 100}% de las partículas en el archivo {file_path}.")
            else:
                print(f"Archivo {file_path} no encontrado.")


# Parámetros
temp = 100  # Cambia esta variable a mano
n_files = 5  # Cambia esto al número de archivos
total_particles = 300  # Cambia esto al número total de partículas
percentage = 0.50  # 50% de las partículas
output_file = "unique_collision_times_results_circle.csv"
file_directory = FilePaths.SIMULATIONS_DIR + f"obstacle_collisions/circle/temp_{temp}/mean_calc"

# Procesar los archivos y guardar los resultados en un CSV
process_files_and_save_results(file_directory, temp, n_files, total_particles, percentage, output_file)
