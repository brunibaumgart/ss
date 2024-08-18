import os

import cv2
import numpy as np

from src.constants.FilePaths import PARAMETERS_FILE, DEFAULT_SIMULATION_DIR, \
    OUTPUT_DEFAULT_VIDEO


# Step 1: Load simulation parameters
def load_simulation_parameters(filename):
    params = {}
    with open(filename, 'r') as f:
        for line in f:
            key, value = line.split()
            params[key] = float(value)
    return params


# Step 2: Load output files
def load_output_files(output_dir, iterations):
    data = []
    for i in range(iterations):
        filename = os.path.join(output_dir, f"output_{i}.txt")
        with open(filename, 'r') as f:
            particles = []
            for line in f:
                # Skip header line
                if line.strip() == "" or line.startswith("id"):
                    continue
                values = line.split()
                id_ = int(values[0])
                x, y, vx, vy = map(float, values[1:5])
                neighbors = list(map(int, values[5].strip('[]').split()))  # Convert neighbors to a list of ints
                particles.append((id_, x, y, vx, vy, neighbors))
            data.append(particles)
    return data


# Step 3: Create animation video
def create_animation_video(sim_params, data, output_video):
    L = sim_params['L']
    R = sim_params['R']
    V = sim_params['V']
    N = int(sim_params['N'])
    iterations = len(data)

    scale = 500 / L  # Scaling factor for visualization
    radius = int(R * scale)

    # Define colors
    particle_color = (255, 0, 0)  # Red

    # Set up video writer
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Codec for .mp4
    video_writer = cv2.VideoWriter(output_video, fourcc, 10, (500, 500))

    for i in range(iterations):
        frame = np.ones((500, 500, 3), dtype=np.uint8) * 255  # White background

        for particle in data[i]:
            id_, x, y, vx, vy, _ = particle  # Ignore neighbors
            cx, cy = int(x * scale), int(y * scale)

            # Draw particle
            cv2.circle(frame, (cx, cy), radius, particle_color, -1)

            # Draw velocity vector
            end_point = (int(cx + vx * scale), int(cy + vy * scale))
            cv2.arrowedLine(frame, (cx, cy), end_point, particle_color, 1)

        # Write frame to video
        video_writer.write(frame)

    video_writer.release()  # Finalize the video


# Main function to run the simulation
def main():
    sim_params = load_simulation_parameters(PARAMETERS_FILE)
    output_data = load_output_files(DEFAULT_SIMULATION_DIR, int(sim_params['Iterations']))
    create_animation_video(sim_params, output_data, OUTPUT_DEFAULT_VIDEO)


if __name__ == "__main__":
    main()
