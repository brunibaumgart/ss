import os
import json
import cv2
import numpy as np
import random

from src.constants import FilePaths

# Step 1: Load simulation parameters from JSON
def load_simulation_parameters(filename):
    with open(filename, 'r') as f:
        return json.load(f)

# Step 2: Load particle data from the combined output file
def generate_color_for_id(id_):
    random.seed(id_)  # Ensure color consistency for the same ID
    return tuple(random.randint(0, 255) for _ in range(3))

def load_output_file(filename):
    data = []
    particles = []
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith(('id', 'Time')):
                continue
            if line.startswith('#'):
                if particles:
                    data.append(particles)
                    particles = []
            else:
                values = line.split()
                if len(values) < 5:
                    print(f"Warning: Not enough data in line: {line} in file: {filename}")
                    continue
                try:
                    id_ = int(values[0])
                    x, y, vx, vy = map(float, values[1:5])
                    color = generate_color_for_id(id_)
                    particles.append((id_, x, y, vx, vy, color))
                except ValueError as e:
                    print(f"Error parsing line '{line}' in file '{filename}': {e}")
                    continue
        if particles:  # Append the last batch of particles if the file does not end with ###
            data.append(particles)
    return data

# Step 3: Create animation video
def create_animation_video(sim_params, data, output_video):
    L = sim_params['l']
    R = sim_params['rp']
    resolution = 1080  # Adjust the resolution as needed
    scale = resolution / L
    radius = int(R * scale)

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Codec for .mp4
    video_writer = cv2.VideoWriter(output_video, fourcc, 10, (resolution, resolution))

    for i, particles in enumerate(data):
        frame = np.ones((resolution, resolution, 3), dtype=np.uint8) * 255  # White background
        iteration_text = f"Iteration: {i}"
        cv2.putText(frame, iteration_text, (50, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 2, cv2.LINE_AA)

        for id_, x, y, vx, vy, particle_color in particles:
            cx, cy = int(x * scale), int(y * scale)
            particle_radius = int(0.005 * scale) if id_ == -1 else radius
            cv2.circle(frame, (cx, cy), particle_radius, particle_color, -1)
            text_position = (cx + radius, cy - radius)
            cv2.putText(frame, str(id_), text_position, cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1, cv2.LINE_AA)

        video_writer.write(frame)

    video_writer.release()  # Finalize the video

# Main function to run the simulation
def main():
    sim_params = load_simulation_parameters(FilePaths.SIMULATIONS_DIR + 'config.json')
    output_video = FilePaths.OUTPUT_DEFAULT_VIDEO
    combined_frames_file = FilePaths.SIMULATIONS_DIR + 'video.txt'

    output_data = load_output_file(combined_frames_file)
    create_animation_video(sim_params, output_data, output_video)

if __name__ == "__main__":
    main()
