import os

import cv2
import numpy as np

from src.constants.FilePaths import PARAMETERS_FILE, DEFAULT_SIMULATION_DIR, \
    OUTPUT_DEFAULT_VIDEO, VIDEO_PARAMETERS_FILE


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


def interpolate_color(angle):
    """Interpolates between predefined colors based on the angle."""
    if angle < 90:
        # Interpolar entre rojo (0°) y verde (90°)
        r = int(255 * (1 - angle / 90))
        g = int(255 * (angle / 90))
        b = 0
    elif angle < 180:
        # Interpolar entre verde (90°) y amarillo (180°)
        r = 0
        g = int(255 * (1 - (angle - 90) / 90))
        b = int(255 * ((angle - 90) / 90))
    elif angle < 270:
        # Interpolar entre amarillo (180°) y azul (270°)
        r = int(255 * ((angle - 180) / 90))
        g = 0
        b = int(255 * (1 - (angle - 180) / 90))
    else:
        # Interpolar entre azul (270°) y rojo (360°)
        r = int(255 * (1 - (angle - 270) / 90))
        g = 0
        b = int(255 * ((angle - 270) / 90))

    return r, g, b

# Step 3: Create animation video
def create_animation_video(sim_params, data, output_video):
    L = sim_params['L']
    R = sim_params['R']
    iterations = len(data)

    resolution = 1080  # Resolución para la visualización (puedes ajustar este valor)
    scale = resolution / L
    radius = int(R * scale)

    # Configurar el video writer
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Codec para .mp4
    video_writer = cv2.VideoWriter(output_video, fourcc, 10, (resolution, resolution))

    for i in range(iterations):
        frame = np.ones((resolution, resolution, 3), dtype=np.uint8) * 255  # Fondo blanco

        for particle in data[i]:
            id_, x, y, vx, vy, _ = particle  # Ignorar vecinos
            cx, cy = int(x * scale), int(y * scale)

            # Calcular el ángulo en grados
            angle = np.degrees(np.arctan2(vy, vx)) % 360

            # Obtener el color basado en el ángulo
            particle_color = interpolate_color(angle)

            # Dibujar la partícula
            cv2.circle(frame, (cx, cy), radius, particle_color, -1)

            # Dibujar el vector de velocidad
            if R == 0:
                arrow_length = scale * 0.5
            else:
                arrow_length = max(R * scale, scale * 0.5)  # Ajusta este factor para cambiar el tamaño de las flechas
            end_point = (int(cx + vx * arrow_length), int(cy + vy * arrow_length))  # Escalar para mejor visibilidad
            cv2.arrowedLine(frame, (cx, cy), end_point, particle_color, 1)

        # Escribir el frame en el video
        video_writer.write(frame)

    video_writer.release()  # Finalizar el video



# Main function to run the simulation
def main():
    sim_params = load_simulation_parameters(PARAMETERS_FILE)
    video_params = load_simulation_parameters(VIDEO_PARAMETERS_FILE)
    output_data = load_output_files(DEFAULT_SIMULATION_DIR, int(video_params['Iterations']))
    create_animation_video(sim_params, output_data, OUTPUT_DEFAULT_VIDEO)


if __name__ == "__main__":
    main()
