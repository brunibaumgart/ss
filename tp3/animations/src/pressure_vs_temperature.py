import matplotlib.pyplot as plt
from matplotlib.ticker import FuncFormatter

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

# Custom formatter function
def custom_formatter(x, pos):
    return f'${x/1e6:.1f}$'  # Format as LaTeX string

# Plotting the data
def plot_data(temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds):
    plt.figure(figsize=(12, 6))  # Adjusted width to 12

    # Plot wall pressure with error bars (only points, no lines)
    plt.errorbar(temperatures, wall_means, yerr=wall_stds, fmt='o', label='Presión en las paredes', capsize=5)

    # Plot obstacle pressure with error bars (only points, no lines)
    plt.errorbar(temperatures, obstacle_means, yerr=obstacle_stds, fmt='s', label='Presión en los obstáculos', capsize=5)

    # Customize x-axis: Set the ticks to exactly match the temperature values
    plt.xticks(temperatures)

    # Labels and title with increased font size
    plt.xlabel('Temperatura (U.A.)', fontsize=14)
    plt.ylabel('Presión (Pa.m)', fontsize=14)

    # Format y-axis to display as x10^6
    ax = plt.gca()
    formatter = FuncFormatter(custom_formatter)
    ax.yaxis.set_major_formatter(formatter)
    ax.text(0.01, 1.01, r'$\times 10^6$', transform=ax.transAxes, fontsize=16, va='bottom', ha='left')

    # Increase font size for tick labels
    plt.tick_params(axis='both', which='major', labelsize=14)

    # Move legend to the top-right corner, outside the plot
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.8), fontsize=12)

    plt.grid(True)
    plt.tight_layout()
    plt.show()

# Example usage
file_name = SIMULATIONS_DIR + PRESSURE_VS_TEMPERATURE  # Replace with the path to your file
temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds = read_data(file_name)
plot_data(temperatures, wall_means, wall_stds, obstacle_means, obstacle_stds)