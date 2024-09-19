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

def generate_color_for_id(id_):
    if id_ == -1:
        return 0, 255, 0
    return 255, 0, 0

def load_output_file(filename):
    data = []
    particles = []
    current_time = 0
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith(('id', 'Time')):
                if line.startswith('Time'):
                    # Extract time from the 'Time' line
                    _, time_value = line.split(' ')
                    current_time = float(time_value.strip())
                continue
            if line.startswith('#'):
                if particles:
                    data.append((current_time, particles))
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
            data.append((current_time, particles))
    return data

# Step 3: Create animation video with delta_t filtering
def create_animation_video(sim_params, data, output_video, delta_t):
    L = sim_params['l']
    R = sim_params['rp']
    resolution = 1080  # Adjust the resolution as needed
    scale = resolution / L
    radius = int(R * scale)

    fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Codec for .mp4
    video_writer = cv2.VideoWriter(output_video, fourcc, 10, (resolution, resolution))

    last_time = -delta_t  # To ensure first frame is written
    for i, (current_time, particles) in enumerate(data):
        if current_time - last_time >= delta_t:
            last_time = current_time  # Update last time a frame was written
            frame = np.ones((resolution, resolution, 3), dtype=np.uint8) * 255  # White background
            iteration_text = f"Time: {current_time:.2f}s"
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
    combined_frames_file = FilePaths.SIMULATIONS_DIR + 'obstacle_collisions/temp_9/' + 'video.txt'
    delta_t = 0.1  # Define the time interval for frames

    output_data = load_output_file(combined_frames_file)
    create_animation_video(sim_params, output_data, output_video, delta_t)

if __name__ == "__main__":
    main()
