import os
import re
import matplotlib.pyplot as plt

from src.constants import FilePaths

plt.rcParams.update({'font.size': 16})

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

        count_above_100 = sum(1 for distance in distances if distance >= 100)
        return (count_above_100 / len(distances)) if distances else 0

    except (FileNotFoundError, ValueError):
        return None


def plot_percentages(directory):
    data = {}

    # Iterate through files in the directory
    for filename in os.listdir(directory):
        if filename.startswith("max_position_") and filename.endswith(".txt"):
            # Extract A and B values from the filename
            A, B = extract_values_from_filename(filename)
            if A is not None and B is not None:
                # Calculate percentage for the file
                filepath = os.path.join(directory, filename)
                percentage = calculate_percentage(filepath)

                # Organize data by A and B values
                if A not in data:
                    data[A] = []
                data[A].append((B, percentage))

    # Plot each curve for each unique value of A
    plt.figure(figsize=(10, 6))
    for A, values in data.items():
        # Sort by B for consistent plotting
        values.sort()
        B_values, percentages = zip(*values)

        plt.plot(B_values, percentages, marker='o', label=f"{A:.2f}")

    # Configure plot
    plt.xlabel(r"$B_p$ (m)")
    plt.ylim(0, 0.6)
    plt.ylabel(r"$\varphi_t$")
    plt.legend(title=r"$A_p$", fontsize=14)
    plt.grid(True)
    plt.show()


# Example usage
directory = FilePaths.SIMULATIONS_DIR + "max_distance_heatmap/"
plot_percentages(directory)
