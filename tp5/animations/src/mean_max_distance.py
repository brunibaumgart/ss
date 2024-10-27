import os

import numpy as np
import matplotlib.pyplot as plt

from src.constants import FilePaths

# Directory containing your files
directory = FilePaths.SIMULATIONS_DIR + "max_positions/"

# Initialize a dictionary to store mean values for each N
mean_values = {}
std_values = {}

# Iterate over each file in the directory
for filename in os.listdir(directory):
    if filename.startswith("max_positions__n_"):
        # Extract the N value from the filename
        n_value = int(filename.split('_')[-1].split('.')[0])

        # Load the distances from the file, each line being a distance value
        filepath = os.path.join(directory, filename)
        with open(filepath, 'r') as f:
            distances = np.array([float(line.strip()) for line in f])

        # Calculate 100 - x for each distance and then compute the mean
        mean_x = np.mean(distances)
        std_x = np.std(distances)

        # Store the result in the dictionary
        mean_values[n_value] = mean_x
        std_values[n_value] = std_x

# Sort the N values and corresponding means for plotting
sorted_n_values = sorted(mean_values.keys())
sorted_means = [mean_values[n] for n in sorted_n_values]
sorted_std_devs = [std_values[n] for n in sorted_n_values]


# Plot the results
plt.errorbar(sorted_n_values, sorted_means, yerr=sorted_std_devs, fmt='o', linestyle='-', capsize=5)
plt.xlabel(r"$N_j$")
plt.ylabel(r"$\bar{d}$ (m)")
plt.grid(True)
plt.show()
