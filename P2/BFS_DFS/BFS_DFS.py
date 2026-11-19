import tkinter as tk
from tkinter import messagebox
import pygame
import subprocess
import time
import json

def run_solver(algorithm):
    start_time = time.time()
    result = subprocess.run(["./fifteen_puzzle", algorithm], capture_output=True, text=True)
    end_time = time.time()
    
    if result.returncode != 0:
        messagebox.showerror("Error", "Failed to execute C program")
        return
    
    try:
        output_data = json.loads(result.stdout)
        moves = output_data["moves"]
        path = output_data["path"]
    except json.JSONDecodeError:
        messagebox.showerror("Error", "Invalid output format from C program")
        return
    
    execution_time = end_time - start_time
    messagebox.showinfo("Solution Info", f"Moves: {len(moves)}\nTime: {execution_time:.4f} seconds")
    visualize_solution(path)

def visualize_solution(path):
    pygame.init()
    screen = pygame.display.set_mode((400, 400))
    pygame.display.set_caption("15-Puzzle Solution")
    font = pygame.font.Font(None, 36)
    
    for state in path:
        screen.fill((255, 255, 255))
        for i in range(4):
            for j in range(4):
                value = state[i][j]
                if value != 0:
                    pygame.draw.rect(screen, (0, 0, 255), (j * 100, i * 100, 100, 100))
                    text = font.render(str(value), True, (255, 255, 255))
                    screen.blit(text, (j * 100 + 40, i * 100 + 40))
        pygame.display.flip()
        pygame.time.delay(500)
    
    pygame.quit()

def main():
    root = tk.Tk()
    root.title("15-Puzzle Solver")
    
    tk.Label(root, text="Select Algorithm:").pack()
    tk.Button(root, text="DFS", command=lambda: run_solver("DFS")).pack()
    tk.Button(root, text="BFS", command=lambda: run_solver("BFS")).pack()
    
    root.mainloop()

if __name__ == "__main__":
    main()
