import matplotlib.pyplot as plt
import numpy as np

from src.constants.FilePaths import SIMULATIONS_DIR, PRESSURE_VS_TEMPERATURE


# Function to read data from the file
def read_data(file_name):
    temperatures = []
    wall_means = []
    wall_stds = []
    obstacle_means = []
    obstacle_stds = []

    with open(file_name, 'r') as file:
        next(file)  # Skip the first line (header)
        for line in file:
            w_mean, w_std, o_mean, o_std, t = map(float, line.split())
            wall_means.append(w_mean)
            wall_stds.append(w_std)
            obstacle_means.append(o_mean)
            obstacle_stds.append(o_std)
            temperatures.append(t)

    return temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds


# Plotting the data
def plot_data(temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds):
    plt.figure(figsize=(10, 6))

    # Plot wall pressure with error bars (only points, no lines)
    plt.errorbar(temperatures, wall_means, yerr=wall_stds, fmt='o', label='Wall Pressure', capsize=5)

    # Plot obstacle pressure with error bars (only points, no lines)
    plt.errorbar(temperatures, obstacle_means, yerr=obstacle_stds, fmt='s', label='Obstacle Pressure', capsize=5)

    # Customize x-axis: Set the ticks to exactly match the temperature values
    plt.xticks(temperatures)

    # Labels and title
    plt.xlabel('Temperature (T)')
    plt.ylabel('Pressure (P)')
    plt.title('Pressure vs Temperature')
    plt.legend()
    plt.grid(True)

    # Show the plot
    plt.show()


# Example usage
file_name = SIMULATIONS_DIR + PRESSURE_VS_TEMPERATURE  # Replace with the path to your file
temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds = read_data(file_name)
plot_data(temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds)
