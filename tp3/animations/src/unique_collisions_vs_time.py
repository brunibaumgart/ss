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


# Plot Collisions vs. Time
def plot_collisions_vs_time(times, collision_count):
    plt.figure(figsize=(10, 6))
    plt.plot(times, collision_count, linestyle='-')

    # Set font sizes
    plt.xlabel('Tiempo (s)', fontsize=14)
    plt.ylabel('Cant. de Colisiones Únicas con el Obstáculo', fontsize=14)
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    plt.grid(True)
    plt.show()


# File path
file_path = FilePaths.SIMULATIONS_DIR + "unique_obstacle_collision_times.txt"

# Read times from file
times = read_times(file_path)

# Generate cumulative collision count
collision_count = count_collisions(times)

# Plot
plot_collisions_vs_time(times, collision_count)

