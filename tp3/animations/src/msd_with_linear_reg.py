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


# Plot MSD evolution with time on the x-axis and add linear regression
def plot_msd_evolution(times, msd_evolution):
    fig, ax = plt.subplots()

    # Scale the MSD values for better visualization
    msd_evolution_scaled = np.array(msd_evolution) / 1e-3

    # Plot MSD evolution
    ax.plot(times, msd_evolution_scaled, linestyle='-', label='DCM')

    # Calculate linear regression
    coeffs = np.polyfit(times, msd_evolution_scaled, 1)  # Fit a line (degree 1)
    regression_line = np.polyval(coeffs, times)

    # Plot linear regression
    ax.plot(times, regression_line, linestyle='--', color='red', label='Regresión Lineal')

    # Labels
    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Desplazamiento Cuadrático Medio (DCM) ($\\times 10^{-3}$)')

    # Format the y-axis to reflect the scaling
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda y, _: f'{y:.1f}'))

    # Add a legend
    ax.legend()

    plt.grid(True)
    plt.show()

    # Print the slope of the regression line
    slope = coeffs[0]
    print(f'Pendiente de la regresión lineal: {slope:.3f}')


# File path
file_path = FilePaths.SIMULATIONS_DIR + "msd.txt"

# Set the time interval (Delta T)
delta_t = 0.002 # Ajusta este valor según tu preferencia

# Read the positions and times from the file
times, positions = read_positions(file_path)

# Calculate the MSD evolution using fixed time intervals
msd_evolution, selected_times = calculate_msd_evolution(times, positions, delta_t)

# Plot the MSD evolution with time on the x-axis and add linear regression
plot_msd_evolution(selected_times, msd_evolution)
