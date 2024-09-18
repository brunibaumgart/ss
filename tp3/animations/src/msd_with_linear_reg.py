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
def plot_msd_evolution(times, msd_evolution, custom_slope):
    fig, ax = plt.subplots()

    # Convert times and msd_evolution to NumPy arrays (in case they are not already)
    times = np.array(times)
    msd_evolution_scaled = np.array(msd_evolution) / 1e-3

    # Plot MSD evolution as points (scatter plot)
    ax.scatter(times, msd_evolution_scaled, label='DCM', color='blue')

    # Create the linear regression line with a custom slope
    # y = m * x + b, where m = custom_slope
    # We calculate b using the average of the differences (y - m * x)
    custom_intercept = np.mean(msd_evolution_scaled - custom_slope * times)
    regression_line = custom_slope * times + custom_intercept

    # Plot linear regression as a line
    ax.plot(times, regression_line, linestyle='--', color='red', label=f'Regresión Lineal (Pendiente = {custom_slope})')

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
    print(f'Pendiente de la regresión lineal: {custom_slope:.3f}')


# File path
file_path = FilePaths.SIMULATIONS_DIR + "msd.txt"

# Set the time interval (Delta T)
delta_t = 0.01  # Ajusta este valor según tu preferencia

# Read the positions and times from the file
times, positions = read_positions(file_path)

# Calculate the MSD evolution using fixed time intervals
msd_evolution, selected_times = calculate_msd_evolution(times, positions, delta_t)

# Define the custom slope for the linear regression (you can adjust this value)
custom_slope = -1.3  # Cambia este valor según lo que desees

# Plot the MSD evolution with time on the x-axis and add linear regression
plot_msd_evolution(selected_times, msd_evolution, custom_slope)
