import os
import re
import numpy as np
import matplotlib.pyplot as plt

from src.constants import FilePaths

plt.rcParams.update({'font.size': 14})


def extract_values_from_filename(filename):
    # Extract A and B from the filename using regular expressions
    match = re.match(r"max_position_(\d+,\d+)_(\d+,\d+).txt", filename)
    if match:
        A = float(match.group(1).replace(',', '.'))
        B = float(match.group(2).replace(',', '.'))
        return A, B
    return None, None

def calculate_percentage(filename):
    try:
        with open(filename, 'r') as file:
            distances = [float(line.strip()) for line in file]

        # Calculate the percentage of distances >= 100
        n_total = len(distances)
        n_above_100 = sum(1 for distance in distances if distance >= 100)
        return (n_above_100 / n_total) if n_total > 0 else 0

    except (FileNotFoundError, ValueError):
        return None

def create_heatmap_data(directory):
    data = {}

    # Gather data from files
    for filename in os.listdir(directory):
        if filename.startswith("max_position_") and filename.endswith(".txt"):
            A, B = extract_values_from_filename(filename)
            if A is not None and B is not None:
                filepath = os.path.join(directory, filename)
                percentage = calculate_percentage(filepath)
                if A not in data:
                    data[A] = {}
                data[A][B] = percentage

    # Get unique sorted values of A and B for axis labels
    A_values = sorted(data.keys())
    B_values = sorted(set(B for a_data in data.values() for B in a_data.keys()))

    # Initialize a matrix to hold the percentage data for the heatmap
    heatmap_matrix = np.full((len(A_values), len(B_values)), np.nan)

    # Fill the matrix with the percentage data
    for i, A in enumerate(A_values):
        for j, B in enumerate(B_values):
            if B in data[A]:
                heatmap_matrix[i, j] = data[A][B]

    return A_values, B_values, heatmap_matrix

def plot_heatmap(A_values, B_values, heatmap_matrix):
    plt.figure(figsize=(8, 6))
    plt.imshow(heatmap_matrix, aspect='auto', origin='lower', cmap='viridis',
               extent=[min(B_values), max(B_values), min(A_values), max(A_values)])

    # Add color bar
    plt.colorbar(label=r"$\varphi_t$")

    # Label axes
    plt.xlabel(r"$B_p$ (m)")
    plt.ylabel(r"$A_p$")
    plt.show()

# Example usage
directory = FilePaths.SIMULATIONS_DIR + "max_distance_heatmap/"
A_values, B_values, heatmap_matrix = create_heatmap_data(directory)
plot_heatmap(A_values, B_values, heatmap_matrix)
