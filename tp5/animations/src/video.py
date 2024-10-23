import cv2
import numpy as np

from src.constants import FilePaths

# Parameters
frame_width = 800  # Base frame width
frame_height = 800  # Base frame height
radius = 0.25  # Particle radius (adjustable)
video_name = FilePaths.OUTPUT_DIR + 'particles_simulation.mp4'
fps = 10  # Frames per second
scale_factor = 1  # Adjust this to scale the frames

# Parse the input file
def parse_file(file_path):
    with open(file_path, 'r') as f:
        lines = f.readlines()

    frames = []
    frame_data = []
    for line in lines:
        line = line.strip()
        if line.startswith("#"):
            if frame_data:
                frames.append(frame_data)
                frame_data = []
        elif len(line.split()) == 1:
            # This is the time step, can be ignored unless you want to use it for something else
            continue
        else:
            # Parse the particle data: id, x, y
            particle_data = list(map(float, line.split()))
            frame_data.append(particle_data)
    if frame_data:  # Append the last frame
        frames.append(frame_data)
    return frames

def create_frame(particles, width, height, r, scale_factor=1):
    # Apply scaling factor to the frame size
    scaled_width = int(width * scale_factor)
    scaled_height = int(height * scale_factor)

    # Create a white frame
    frame = np.full((scaled_height, scaled_width, 3), (255, 255, 255), dtype=np.uint8)

    for particle in particles:
        particle_id, x, y = particle
        # Scale positions by the scale_factor and cast to integers
        x = int(float(x) * scaled_width / 100)  # Scale and convert to integer
        y = int(float(y) * scaled_height / 100)  # Scale and convert to integer
        # Convert radius to integer after scaling and apply the scale factor
        scaled_radius = max(1, int(float(r) * scale_factor))

        # Choose color based on particle ID
        if particle_id == -1:
            color = (0, 0, 255)  # Red for particle with id == -1
        else:
            color = (255, 0, 0)  # Blue for all other particles

        # Draw the particle
        cv2.circle(frame, (x, y), scaled_radius, color, -1)

    return frame

# Main function to generate the video
def generate_video(file_path, scale_factor=1):
    frames_data = parse_file(file_path)

    # Calculate the scaled dimensions for the video writer
    scaled_width = int(frame_width * scale_factor)
    scaled_height = int(frame_height * scale_factor)

    # Initialize video writer with the scaled dimensions
    out = cv2.VideoWriter(video_name, cv2.VideoWriter_fourcc(*'mp4v'), fps, (scaled_width, scaled_height))

    for frame_particles in frames_data:
        frame = create_frame(frame_particles, frame_width, frame_height, radius, scale_factor)
        out.write(frame)

    out.release()

# Example usage
file_path = FilePaths.SIMULATIONS_DIR + 'video.txt'  # Path to your file
generate_video(file_path, scale_factor)
