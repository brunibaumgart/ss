import os
import matplotlib.pyplot as plt

from src.constants import FilePaths

# Directory containing the "max_positions" and "try_seed" files
max_positions_dir = FilePaths.SIMULATIONS_DIR + "max_positions/"
try_seed_dir = FilePaths.SIMULATIONS_DIR + "max_positions/"

# Dictionary to store line counts for max_positions files
max_positions_counts = {}

# Process "max_positions" files to get line counts
for filename in os.listdir(max_positions_dir):
    if filename.startswith("max_positions__n_"):
        n_value = int(filename.split('_')[-1].split('.')[0])

        # Count the lines in the max_positions file
        filepath = os.path.join(max_positions_dir, filename)
        with open(filepath, 'r') as f:
            line_count = sum(1 for line in f)

        # Store the line count in the dictionary
        max_positions_counts[n_value] = line_count

# Dictionary to store adjusted line counts for try_seed files
line_counts = {}

# Process "try_seed" files for line count
for filename in os.listdir(try_seed_dir):
    if filename.startswith("try_seed__n_"):
        # Extract the N value from the filename
        n_value = int(filename.split('_')[-1].split('.')[0])

        # Count the lines in the try_seed file
        filepath = os.path.join(try_seed_dir, filename)
        with open(filepath, 'r') as f:
            line_count = sum(1 for line in f)

        # Divide by the line count from the corresponding max_positions file
        divisor = max_positions_counts.get(n_value, 1)  # Default to 1 to avoid division by zero
        line_counts[n_value] = line_count / divisor

# Sort N values and corresponding line counts for plotting
sorted_n_values = sorted(line_counts.keys())
sorted_line_counts = [line_counts[n] for n in sorted_n_values]

# Plot the line counts divided by the corresponding max_positions line count for each N
plt.plot(sorted_n_values, sorted_line_counts, marker='o', linestyle='-')
plt.xlabel(r"$N_j$")
plt.ylabel(r"$\varphi_t / \varphi_r$")
plt.grid(True)
plt.show()
