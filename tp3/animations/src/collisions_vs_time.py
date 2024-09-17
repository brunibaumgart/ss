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
    plt.plot(times, collision_count, linestyle='-')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Cantidad de Colisiones con el Obstáculo')
    plt.grid(True)
    plt.show()


# File path
file_path = FilePaths.SIMULATIONS_DIR + "obstacle_collision_times.txt"

# Read times from file
times = read_times(file_path)

# Generate cumulative collision count
collision_count = count_collisions(times)

# Plot
plot_collisions_vs_time(times, collision_count)
