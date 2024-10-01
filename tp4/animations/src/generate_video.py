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
def animate_oscillators(positions, output_file='oscillators_animation.mp4', scale_factor=1e4):
    # Setup figure and axis
    fig, ax = plt.subplots()
    ax.set_xlim(0, positions.shape[1])  # Number of particles on the x-axis
    ax.set_ylim(-np.max(positions) * scale_factor - 0.001, np.max(positions) * scale_factor + 0.001)  # Vertical limits centered around 0
    ax.axhline(0, color='black', linewidth=0.8)  # Add a horizontal line at y=0 for reference

    # Scatter plot representing the position of all particles (without connecting lines)
    scatter = ax.scatter([], [], s=50)  # s is the size of the points

    # Initialization function: plot the background of each frame
    def init():
        scatter.set_offsets(np.empty((0, 2)))  # Correctly set to an empty 2D array
        return scatter,

    # Animation function: this will update the plot at each frame
    def animate(i):
        x = np.arange(positions.shape[1])  # Particle indices on x-axis
        y = positions[i] * scale_factor  # Scale the vertical positions of the particles
        scatter.set_offsets(np.c_[x, y])  # Update the scatter points
        return scatter,

    # Create the animation
    anim = animation.FuncAnimation(fig, animate, init_func=init,
                                   frames=positions.shape[0], interval=50, blit=True)

    # Save the animation as MP4
    anim.save(output_file, fps=30, writer='ffmpeg')

    plt.show()

# Main function
def main():
    # Load the simulated data from the file
    filename = 'video.txt'  # Replace with the actual file name
    positions = load_oscillator_data(filename)

    # Create the animation with scaling
    animate_oscillators(positions, output_file='oscillators_animation.mp4', scale_factor=1e4)

if __name__ == "__main__":
    main()
