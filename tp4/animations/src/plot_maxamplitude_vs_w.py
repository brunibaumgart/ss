import os
import re
import matplotlib.pyplot as plt
import numpy as np

from src.constants import FilePaths

# Directory where your files are stored
directory = FilePaths.SIMULATIONS_DIR + "/ej2/k_100"

# Regex to extract k and w values from the file name
filename_pattern = r"amplitude_vs_time__k_(\d+(?:\.\d+)?)__w_(\d+(?:\.\d+)?).txt"

# Lists to store W and corresponding max position_y values
w_values = []
max_positions = []

# Iterate over all files in the directory
for filename in os.listdir(directory):
    match = re.match(filename_pattern, filename)
    if match:
        # Extract the W value from the filename
        w_value = float(match.group(2))  # second captured group is W
        w_values.append(w_value)

        # Initialize max_position_y
        max_position_y = float('-inf')

        # Read the file and find the max position_y
        filepath = os.path.join(directory, filename)
        with open(filepath, 'r') as file:
            for line in file:
                time, position_y = map(float, line.split())
                if position_y > max_position_y:
                    max_position_y = position_y

        # Append the max position to the list
        max_positions.append(max_position_y)

# Convert lists to numpy arrays and sort by w_values
w_values = np.array(w_values)
max_positions = np.array(max_positions)

# Sort by w_values
sorted_indices = np.argsort(w_values)
w_values = w_values[sorted_indices]
max_positions = max_positions[sorted_indices]

# Increase font size
plt.rcParams.update({'font.size': 14})  # Set global font size

# Plot Max Position vs. W
plt.plot(w_values, max_positions, marker='o', linestyle='-', color='b')
plt.xlabel(r"$\omega$ (rad/s)", fontsize=14)  # Larger font size for the x-label
plt.ylabel('Amplitud máxima (m)', fontsize=14)  # Larger font size for the y-label
plt.grid(True)
plt.tight_layout()
plt.show()

