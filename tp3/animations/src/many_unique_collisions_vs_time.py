import os
import matplotlib.pyplot as plt
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


# Create the list of cumulative collisions
def count_collisions(times):
    collision_count = list(range(1, len(times) + 1))
    return collision_count


# Plot multiple curves of Collisions vs. Time
def plot_multiple_collisions(file_paths, labels):
    plt.figure(figsize=(10, 6))

    for file_path, label in zip(file_paths, labels):
        # Read times from each file
        times = read_times(file_path)

        # Generate cumulative collision count
        collision_count = count_collisions(times)

        # Plot each curve
        plt.plot(times, collision_count, label=label)

    # Set labels and font sizes
    plt.xlabel('Tiempo (s)', fontsize=14)
    plt.ylabel('Cant. de Colisiones Únicas con el Obstáculo', fontsize=14)
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    plt.grid(True)
    plt.legend(fontsize=12)  # Add legend to differentiate curves
    plt.show()


# Example usage:
# Assuming you have multiple folders with different collision times files
folders = [
    FilePaths.SIMULATIONS_DIR + "obstacle_collisions/temp_1",
    FilePaths.SIMULATIONS_DIR + "obstacle_collisions/temp_9",
    FilePaths.SIMULATIONS_DIR + "obstacle_collisions/temp_36",
    FilePaths.SIMULATIONS_DIR + "obstacle_collisions/temp_100",
    ]
files = ["unique_obstacle_collision_times.txt"] * len(folders)  # Assuming all files have the same name

# Generate full file paths
file_paths = [os.path.join(folder, file) for folder, file in zip(folders, files)]
labels = ["v = 1 m/s", "v = 3 m/s", "v = 6 m/s", "v = 10 m/s"]  # Labels for each curve

# Plot multiple curves
plot_multiple_collisions(file_paths, labels)
