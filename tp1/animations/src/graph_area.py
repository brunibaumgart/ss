import matplotlib.pyplot as plt
import numpy as np

def read_data_file(file_path):
    particles = []
    with open(file_path, 'r') as file:
        next(file)
        next(file)

        for line in file:
            particle_id, x, y, neighbours = line.strip().split()

            cleaned_str = neighbours.strip('[]').split()
            neighbours_array = list(map(int, cleaned_str))

            x = x.replace(',', '.')
            y = y.replace(',', '.')
            particles.append((int(particle_id), float(x), float(y), neighbours_array))
    return particles


def visualize_particles(particles, L, M, selected_particle_id):

    neighbours = particles[selected_particle_id][3]

    fig = plt.figure()
    ax = fig.add_subplot(1, 1, 1)

    major_ticks = np.arange(0, L + 1, L/M)

    ax.set_xticks(major_ticks)
    ax.set_yticks(major_ticks)

    x_values = [p[1] for p in particles]
    y_values = [p[2] for p in particles]
    ids = [p[0] for p in particles]

    ax.set(xlim=(0, L+1), ylim=(0, L+1))

    ax.set_xlabel('x')
    ax.set_ylabel('y')
    ax.set_title('Partículas')

    for i, id in enumerate(ids):
        ax.text(x_values[i], y_values[i], str(id), fontsize=9, ha='right')
        if id == selected_particle_id:
            ax.scatter(x_values[i], y_values[i], c="green")
            #ax.scatter(x_values[i], y_values[i], edgecolor='black', facecolor='none', s=100)
        elif id in neighbours:
            ax.scatter(x_values[i], y_values[i], c="red")
        else:
            ax.scatter(x_values[i], y_values[i], c="blue")

    ax.grid(True)

    plt.show()

file_path = '../../simulations/src/main/resources/positions.txt'

particles = read_data_file(file_path)

L = 20
M = 5

visualize_particles(particles, L, M, 9)