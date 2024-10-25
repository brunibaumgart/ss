import matplotlib.pyplot as plt
import matplotlib.animation as animation
import os

from src.constants import FilePaths

# Asegurarse de que la carpeta de salida existe
output_folder = '../output'
if not os.path.exists(output_folder):
    os.makedirs(output_folder)


# Función para leer el archivo y organizar los datos por tiempos
def leer_datos(archivo):
    tiempos = []
    posiciones = []
    with open(archivo, 'r') as f:
        tiempo_actual = None
        pos_actual = []

        for linea in f:
            linea = linea.strip()
            if linea.startswith('#'):
                # Nueva marca de tiempo
                if tiempo_actual is not None:
                    tiempos.append(tiempo_actual)
                    posiciones.append(pos_actual)
                tiempo_actual = None
                pos_actual = []
            elif tiempo_actual is None:
                # La primera línea después de "#######" es el tiempo
                tiempo_actual = float(linea)
            else:
                # Leer datos de partículas
                data = linea.split()
                id_particula = int(data[0])
                pos_x = float(data[1])
                pos_y = float(data[2])
                pos_actual.append((id_particula, pos_x, pos_y))

        # Agregar el último tiempo y sus posiciones
        if tiempo_actual is not None:
            tiempos.append(tiempo_actual)
            posiciones.append(pos_actual)

    return tiempos, posiciones


# Función para actualizar la animación en cada cuadro
def actualizar_cuadro(num, tiempos, posiciones, scatter_red, scatter_blue):
    posiciones_actuales = posiciones[num]

    # Separar las posiciones del jugador rojo y de los jugadores azules
    rojas = [(pos[1], pos[2]) for pos in posiciones_actuales if pos[0] == -1]
    azules = [(pos[1], pos[2]) for pos in posiciones_actuales if pos[0] != -1]

    # Actualizar las posiciones de las partículas
    if rojas:
        scatter_red.set_offsets(rojas)
    scatter_blue.set_offsets(azules)

    # Título con el tiempo actual
    plt.title(f"Tiempo: {tiempos[num]:.2f}s")


# Leer los datos desde el archivo
file_path = FilePaths.SIMULATIONS_DIR + 'video.txt'  # Path to your file
tiempos, posiciones = leer_datos(file_path)

# Configuración de la figura y los ejes
fig, ax = plt.subplots()
ax.set_facecolor('#02F900')  # Fondo verde claro y brillante
ax.set_xlim(0, 100)
ax.set_ylim(0, 80)

# Ajuste del radio visual de las partículas
radio_visual = 0.7  # Aumentar el radio visual en metros
escala = 100 / ax.get_window_extent().width  # Escala visual de la gráfica
tamaño_partícula = (radio_visual / escala) ** 2  # Ajuste del área

# Inicializar los puntos de los jugadores rojo y azul con el nuevo tamaño visual
scatter_red = ax.scatter([], [], color='#FF2500', s=tamaño_partícula, label='Jugador Rojo')  # Rojo brillante
scatter_blue = ax.scatter([], [], color='#0533FF', s=tamaño_partícula, label='Jugadores Azules')  # Azul brillante

# Leyenda
ax.legend()

# Crear la animación
ani = animation.FuncAnimation(
    fig, actualizar_cuadro, frames=len(tiempos),
    fargs=(tiempos, posiciones, scatter_red, scatter_blue),
    interval=100, repeat=False
)

# Guardar la animación con mayor calidad
output_path = os.path.join(output_folder, 'animacion.mp4')
ani.save(
    output_path,
    writer='ffmpeg',
    fps=10,
    dpi=200,  # Mejora la calidad del video al aumentar los puntos por pulgada
    bitrate=2000  # Aumenta el bitrate para mayor claridad
)

print(f"Animación guardada en {output_path}")
