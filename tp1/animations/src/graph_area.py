import matplotlib.pyplot as plt
import numpy as np
import json

from constants.FilePaths import FILE_POSITIONS, FILE_EXAMPLE


def read_positions_file(file_path):
    particles = []
    with open(file_path, 'r') as file:
        next(file)
        next(file)

        for line in file:
            particle_id, x, y, neighbours = line.split(" ", 3)

            cleaned_str = neighbours.strip("[]\n").split()
            neighbours_array = list(map(int, cleaned_str))

            x = x.replace(',', '.')
            y = y.replace(',', '.')
            particles.append((int(particle_id), float(x), float(y), neighbours_array))
    return particles


def visualize_particles(particles, l, rc, r, m, method, selected_particle_id):
    neighbours = particles[selected_particle_id][3]

    fig, ax = plt.subplots()

    # Definir los ticks del gráfico
    major_ticks = np.arange(0, l + 1, 1)
    ax.set_xticks(major_ticks)
    ax.set_yticks(major_ticks)

    x_values = [p[1] for p in particles]
    y_values = [p[2] for p in particles]
    ids = [p[0] for p in particles]

    ax.set(xlim=(0, l), ylim=(0, l))
    ax.set_xlabel('x')
    ax.set_ylabel('y')
    ax.set_title('Partículas (Método ' + method + ')')

    for i, id in enumerate(ids):
        ax.text(x_values[i] + r, y_values[i] + r, str(id), fontsize=9, ha='left')
        if id == selected_particle_id:
            # Dibujar el círculo que representa la partícula seleccionada
            particle_circle = plt.Circle((x_values[i], y_values[i]), r, color="green", fill=True)
            ax.add_patch(particle_circle)

            # Dibujar el círculo que representa el radio de interacción (rc)
            interaction_circle = plt.Circle((x_values[i], y_values[i]), rc, edgecolor='black', facecolor='none',
                                            linestyle='--')
            ax.add_patch(interaction_circle)

            clipped_x = x_values[i]
            clipped_y = y_values[i]
            if x_values[i] - rc < 0:
                clipped_x += l
            if x_values[i] + rc > l:
                clipped_x -= l
            if y_values[i] - rc < 0:
                clipped_y += l
            if y_values[i] + rc > l:
                clipped_y -= l
            if clipped_x != x_values[i] or clipped_y != y_values[i]:
                clipped_interaction_circle = plt.Circle((clipped_x, clipped_y), rc, edgecolor='black', facecolor='none',
                                                        linestyle='--')
                ax.add_patch(clipped_interaction_circle)
        elif id in neighbours:
            # Dibujar el círculo que representa un vecino
            neighbour_circle = plt.Circle((x_values[i], y_values[i]), r, color="red", fill=True)
            ax.add_patch(neighbour_circle)
        else:
            # Dibujar el círculo que representa una partícula no vecina
            particle_circle = plt.Circle((x_values[i], y_values[i]), r, color="blue", fill=True)
            ax.add_patch(particle_circle)

    ax.grid(True)
    plt.show()


particles = read_positions_file(FILE_POSITIONS)

with open(FILE_EXAMPLE, 'r') as f:
    config = json.load(f)

visualize_particles(particles, config['l'], config['rc'], config['r'], config['m'], config['method'], 17)
