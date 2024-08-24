import matplotlib.pyplot as plt

from src.constants.FilePaths import VA_VS_ETHA_FILE


def read_data_file(file_path):
    etha = []
    mean_va = []
    std_va = []

    with open(file_path, 'r') as file:
        next(file)
        for line in file:
            # Split the line into parts based on whitespace
            parts = line.strip().split()
            etha.append(float(parts[0]))
            mean_va.append(float(parts[1]))
            std_va.append(float(parts[2]))

    return etha, mean_va, std_va

def plot_with_error_bars(etha, mean_va, std_va):
    plt.figure(figsize=(10, 6))
    plt.errorbar(etha, mean_va, yerr=std_va, fmt='o', capsize=5, linestyle='-', color='blue', ecolor='red')
    plt.xlabel('Etha')
    plt.ylabel('Va')
    plt.grid(True)
    plt.show()

# Replace 'data.txt' with the path to your data file
etha, mean_va, std_va = read_data_file(VA_VS_ETHA_FILE)

# Create the plot
plot_with_error_bars(etha, mean_va, std_va)