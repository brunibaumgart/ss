import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

from src.constants import FilePaths


# Read the file and extract time and positions (x, y)
def read_positions(file_path):
    times = []
    positions = []
    with open(file_path, 'r') as f:
        next(f)  # Skip the header if there is one
        for line in f:
            time_str, x_str, y_str = line.split()
            times.append(float(time_str))
            positions.append((float(x_str), float(y_str)))
    return np.array(times), np.array(positions)


# Calculate the MSD evolution over time, using fixed time intervals (Delta T)
def calculate_msd_evolution(times, positions, delta_t):
    initial_position = positions[0]
    msd_evolution = []
    selected_times = []
    last_time = times[0]

    for i in range(1, len(positions)):
        if times[i] >= last_time + delta_t:
            squared_displacements = np.sum((positions[i] - initial_position) ** 2)
            msd_evolution.append(squared_displacements)
            selected_times.append(times[i])  # Guardar el tiempo correspondiente al DCM calculado
            last_time = times[i]

    return msd_evolution, selected_times


# Calculate the average MSD across all files
def calculate_average_msd(num_files, delta_t):
    all_msd_evolutions = []
    common_times = None

    for i in range(1, num_files + 1):
        file_path = FilePaths.SIMULATIONS_DIR + f"msd_{i}.txt"
        times, positions = read_positions(file_path)

        msd_evolution, selected_times = calculate_msd_evolution(times, positions, delta_t)
        all_msd_evolutions.append(msd_evolution)

        # Si es la primera ejecución, fijamos los tiempos comunes
        if common_times is None:
            common_times = selected_times

    # Asegurarse que todas las ejecuciones tienen el mismo número de instantes de tiempo
    all_msd_evolutions = [np.interp(common_times, selected_times, msd) for msd, selected_times in
                          zip(all_msd_evolutions, all_msd_evolutions)]

    # Calcular el promedio del DCM en cada instante de tiempo
    average_msd = np.mean(all_msd_evolutions, axis=0)

    return average_msd, common_times


# Plot MSD evolution with time on the x-axis
def plot_msd_evolution(times, msd_evolution):
    fig, ax = plt.subplots()

    # Scale the MSD values for better visualization
    msd_evolution_scaled = np.array(msd_evolution) / 1e-3

    # Plot
    ax.plot(times, msd_evolution_scaled, linestyle='-')

    # Labels
    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Desplazamiento Cuadrático Medio (DCM) ($\\times 10^{-3}$)')

    # Format the y-axis to reflect the scaling
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda y, _: f'{y:.1f}'))

    plt.grid(True)
    plt.show()


# Variables
num_files = 3  # Número de archivos a leer (ajustar según tu caso)
delta_t = 0.002  # Ajusta este valor según tu preferencia

# Calculate the average MSD evolution using fixed time intervals
average_msd_evolution, common_times = calculate_average_msd(num_files, delta_t)

# Plot the average MSD evolution with time on the x-axis
plot_msd_evolution(common_times, average_msd_evolution)
