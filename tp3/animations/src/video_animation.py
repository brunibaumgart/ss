import os
import json
import cv2
import numpy as np
import random

from src.constants import FilePaths


# Step 1: Load simulation parameters from JSON
def load_simulation_parameters_from_json(filename):
    with open(filename, 'r') as f:
        params = json.load(f)
    return params


# Step 2: Load particle data from output files
def generate_color_for_id(id_):
    # Use a hash function or any method to ensure the color is consistent for each ID
    random.seed(id_)  # Ensure the color is consistent for the same ID
    return tuple(random.randint(0, 255) for _ in range(3))


def load_output_files(output_dir, iterations: int):
    data = []
    for i in range(0, iterations):  # Start from 1 to iterations
        filename = os.path.join(output_dir, f"frame_{i}.txt")
        with open(filename, 'r') as f:
            particles = []
            for line in f:
                # Skip header line
                if line.strip() == "" or line.startswith("id"):
                    continue
                values = line.split()
                if len(values) < 5:
                    print(f"Warning: Not enough data in line: {line.strip()} in file: {filename}")
                    continue  # Skip lines that don't have enough data
                try:
                    id_ = int(values[0])
                    x, y, vx, vy = map(float, values[1:5])
                    color = generate_color_for_id(id_)  # Assign a consistent color based on ID
                    particles.append((id_, x, y, vx, vy, color))
                except ValueError as e:
                    print(f"Error parsing line '{line.strip()}' in file '{filename}': {e}")
                    continue  # Skip lines with parsing errors
            data.append(particles)
    return data


# Step 3: Create animation video
def create_animation_video(sim_params, data, output_video):
    L = sim_params['l']
    R = sim_params['rp']
    iterations = len(data)

    resolution = 1080  # Adjust the resolution as needed
    scale = resolution / L
    radius = int(R * scale * 0.5)  # Reduce particle size

    # Video writer setup
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Codec for .mp4
    video_writer = cv2.VideoWriter(output_video, fourcc, 10, (resolution, resolution))

    for i in range(iterations):
        frame = np.ones((resolution, resolution, 3), dtype=np.uint8) * 255  # White background

        for particle in data[i]:
            id_, x, y, vx, vy, particle_color = particle
            cx, cy = int(x * scale), int(y * scale)

            # Draw the particle with reduced size
            if id_ == -1:
                # TODO: change radius of brownian particle dynamically
                cv2.circle(frame, (cx, cy), int(0.005 * scale * 0.5), particle_color, -1)
            else:
                cv2.circle(frame, (cx, cy), radius, particle_color, -1)

        # Write frame to video
        video_writer.write(frame)

    video_writer.release()  # Finalize the video


# Main function to run the simulation
def main():
    # Load parameters from config.json
    sim_params = load_simulation_parameters_from_json(FilePaths.SIMULATIONS_DIR + 'config.json')
    iterations = int(sim_params['iterations'])
    output_video = FilePaths.OUTPUT_DEFAULT_VIDEO
    frames_dir = FilePaths.VIDEO_FRAMES_DIR

    # Load output files
    output_data = load_output_files(frames_dir, iterations)

    # Create the animation video
    create_animation_video(sim_params, output_data, output_video)


if __name__ == "__main__":
    main()
