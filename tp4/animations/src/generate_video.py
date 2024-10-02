import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation


# Step 1: Load data from file (assumed format similar to the one provided)
def load_oscillator_data(filename):
    data = []
    current_time_data = []
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if line.startswith('########'):  # Separate blocks of data by '########'
                if current_time_data:
                    data.append(current_time_data)
                    current_time_data = []
            elif ' ' in line:
                try:
                    particle_id, position = map(float, line.split())
                    current_time_data.append(position)  # Only store positions
                except ValueError as e:
                    print(f"Error parsing line '{line}': {e}")
                    continue
            else:
                if current_time_data:  # Append previous block before starting a new one
                    data.append(current_time_data)
                    current_time_data = []
    if current_time_data:
        data.append(current_time_data)
    return np.array(data)


# Step 2: Create the animation
def animate_oscillators(positions, output_file='oscillators_animation.mp4', max_duration=120):
    total_frames = positions.shape[0]
    fps = 30  # Frames per second
    max_frames = max_duration * fps  # Maximum number of frames to display in the video

    if total_frames > max_frames:
        frame_step = total_frames // max_frames
    else:
        frame_step = 1  # No skipping if total frames fit within the desired duration

    # Setup figure and axis
    fig, ax = plt.subplots()

    # Set the axis limits according to the minimum and maximum positions
    ax.set_xlim(-0.5, positions.shape[1] + 0.5)  # Give space on x-axis
    ax.set_ylim(np.min(positions), np.max(positions))  # Vertical limits based on data

    ax.axhline(0, color='black', linewidth=0.8)  # Add a horizontal line at y=0 for reference

    # Set labels and use plain formatting for numbers on Y axis
    ax.set_ylabel('Posición (m)')  # Label for Y axis in Spanish
    ax.ticklabel_format(axis='y', style='plain')  # Disable scientific notation for y-axis

    # Adjust left margin to prevent cutting off the labels
    plt.subplots_adjust(left=0.2)

    # Hide the x-axis ticks (particle IDs)
    ax.get_xaxis().set_ticks([])

    # Scatter plot representing the position of all particles (without connecting lines)
    scatter = ax.scatter([], [], s=50)  # s is the size of the points
    line, = ax.plot([], [], lw=2, color='blue')  # Create the line object connecting particles
    wall_connection_line, = ax.plot([], [], lw=2, color='blue')  # Solid line for wall connection

    # Add a dashed line to represent the wall on the right
    wall_x = [positions.shape[1], positions.shape[1]]  # Vertical line at the right end
    wall_y = [np.min(positions), np.max(positions)]  # Line spanning from min to max y
    ax.plot(wall_x, wall_y, linestyle='--', color='gray', lw=2)  # Dashed line for the wall

    # Initialization function: plot the background of each frame
    def init():
        scatter.set_offsets(np.empty((0, 2)))  # Correctly set to an empty 2D array
        line.set_data([], [])  # Initialize the line to empty
        wall_connection_line.set_data([], [])  # Initialize wall connection line to empty
        return scatter, line, wall_connection_line

    # Animation function: this will update the plot at each frame
    def animate(i):
        y = positions[i * frame_step]  # Use original positions sin escalado

        # Generar los valores x de manera que el ID más bajo esté a la derecha
        x = np.arange(positions.shape[1])[::-1]  # Invertir los índices de las partículas en el eje x

        # Actualizar las posiciones del scatter plot
        scatter.set_offsets(np.c_[x, y])

        # Actualizar las posiciones de la línea que conecta todas las partículas
        line.set_data(x, y)

        # Encontrar el índice de la partícula con el ID más bajo (en el extremo derecho)
        rightmost_x_index = 0  # La primera partícula en el array invertido tiene el ID más bajo (en la derecha)
        wall_connection_x = [x[rightmost_x_index], positions.shape[1]]  # De la partícula más a la derecha a la "pared"
        wall_connection_y = [y[rightmost_x_index], 0]  # Desde la posición y de la partícula al centro de la pared (y=0)

        # Actualizar la línea de conexión con la pared (partícula de la derecha)
        wall_connection_line.set_data(wall_connection_x, wall_connection_y)

        return scatter, line, wall_connection_line

    # Create the animation with adjusted speed
    anim = animation.FuncAnimation(fig, animate, init_func=init,
                                   frames=(total_frames // frame_step), interval=3, blit=True)

    # Save the animation as MP4
    anim.save(output_file, fps=fps, writer='ffmpeg')

    plt.show()


# Main function
def main():
    # Load the simulated data from the file
    filename = 'video.txt'  # Replace with the actual file name
    positions = load_oscillator_data(filename)

    # Create the animation with scaling and limit to 2 minutes
    animate_oscillators(positions, output_file='oscillators_animation.mp4', max_duration=120)


if __name__ == "__main__":
    main()
