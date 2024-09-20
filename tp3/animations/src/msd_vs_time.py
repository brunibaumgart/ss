import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import pandas as pd

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
            selected_times.append(np.floor((times[i] - delta_t / 2) / delta_t) * delta_t)  # Ajustar el tiempo al múltiplo más cercano de delta_t
            last_time = times[i]

    return msd_evolution, selected_times

# Plot MSD evolution with time on the x-axis
def plot_msd_evolution(times, msd_evolution):
    fig, ax = plt.subplots()

    # Scale the MSD values for better visualization
    msd_evolution_scaled = np.array(msd_evolution) / 1e-3

    # Plot
    ax.plot(times, msd_evolution_scaled, linestyle='-')

    # Labels
    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Desplazamiento Cuadrático Medio (DCM) (m2)')

    # Format the y-axis to reflect the scaling
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda y, _: f'{y:.1f}'))

    plt.grid(True)
    plt.show()

# Save the MSD evolution data to a CSV file
def save_msd_to_csv(times, msd_evolution, output_csv):
    data = {
        'time': times,
        'msd': msd_evolution
    }
    df = pd.DataFrame(data)
    df.to_csv(output_csv, index=False)

# File path
i = 10
file_path = FilePaths.SIMULATIONS_DIR + f"msd/circle/msd_{i}.txt"

# Set the time interval (Delta T)
delta_t = 0.04 # Ajusta este valor según tu preferencia

# Read the positions and times from the file
times, positions = read_positions(file_path)

# Calculate the MSD evolution using fixed time intervals
msd_evolution, selected_times = calculate_msd_evolution(times, positions, delta_t)

# Plot the MSD evolution with time on the x-axis
plot_msd_evolution(selected_times, msd_evolution)

# Save the MSD evolution data to a CSV file
output_csv = f"../output/circle/msd_evolution_{i}.csv"
save_msd_to_csv(selected_times, msd_evolution, output_csv)
