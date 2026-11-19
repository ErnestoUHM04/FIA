import sys
import numpy as np
import pygame
import math

# Configuración del tablero
ROW_COUNT = 6
COLUMN_COUNT = 7

# Colores (en formato RGB)
BLUE = (0, 0, 255)
BLACK = (0, 0, 0)
RED = (255, 0, 0)
YELLOW = (255, 255, 0)

def create_board():
    board = np.zeros((ROW_COUNT, COLUMN_COUNT))
    return board

def drop_piece(board, row, col, piece):
    board[row][col] = piece

def is_valid_location(board, col):
    return board[0][col] == 0

def get_next_open_row(board, col):
    for r in range(ROW_COUNT-1, -1, -1):
        if board[r][col] == 0:
            return r

def winning_move(board, piece):
    # Verificar horizontalmente
    for c in range(COLUMN_COUNT - 3):
        for r in range(ROW_COUNT):
            if board[r][c] == piece and board[r][c+1] == piece and board[r][c+2] == piece and board[r][c+3] == piece:
                return True

    # Verificar verticalmente
    for c in range(COLUMN_COUNT):
        for r in range(ROW_COUNT - 3):
            if board[r][c] == piece and board[r+1][c] == piece and board[r+2][c] == piece and board[r+3][c] == piece:
                return True

    # Verificar diagonal (positiva)
    for c in range(COLUMN_COUNT - 3):
        for r in range(ROW_COUNT - 3):
            if board[r][c] == piece and board[r+1][c+1] == piece and board[r+2][c+2] == piece and board[r+3][c+3] == piece:
                return True

    # Verificar diagonal (negativa)
    for c in range(COLUMN_COUNT - 3):
        for r in range(3, ROW_COUNT):
            if board[r][c] == piece and board[r-1][c+1] == piece and board[r-2][c+2] == piece and board[r-3][c+3] == piece:
                return True

    return False

def draw_board(board, screen, square_size, radius):
    for c in range(COLUMN_COUNT):
        for r in range(ROW_COUNT):
            pygame.draw.rect(screen, BLUE, (c * square_size, r * square_size + square_size, square_size, square_size))
            pygame.draw.circle(screen, BLACK, (int(c * square_size + square_size/2), int(r * square_size + square_size + square_size/2)), radius)
    
    for c in range(COLUMN_COUNT):
        for r in range(ROW_COUNT):
            if board[r][c] == 1:
                pygame.draw.circle(screen, RED, (int(c * square_size + square_size/2), int(r * square_size + square_size + square_size/2)), radius)
            elif board[r][c] == 2:
                pygame.draw.circle(screen, YELLOW, (int(c * square_size + square_size/2), int(r * square_size + square_size + square_size/2)), radius)
    pygame.display.update()

def main():
    pygame.init()

    square_size = 100
    width = COLUMN_COUNT * square_size
    height = (ROW_COUNT + 1) * square_size  # Se reserva una fila para mostrar la ficha en movimiento
    size = (width, height)

    radius = int(square_size / 2 - 5)

    screen = pygame.display.set_mode(size)
    board = create_board()
    game_over = False
    turn = 0  # 0: jugador 1 (rojo), 1: jugador 2 (amarillo)

    draw_board(board, screen, square_size, radius)
    pygame.display.update()

    clock = pygame.time.Clock()

    # Variables para la animación de la ficha cayendo
    falling = False
    falling_piece = None
    falling_col = None
    falling_row_target = None
    falling_y = 0
    fall_speed = 20

    while not game_over:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                sys.exit()

            if event.type == pygame.MOUSEMOTION:
                pygame.draw.rect(screen, BLACK, (0, 0, width, square_size))
                posx = event.pos[0]
                if turn == 0:
                    pygame.draw.circle(screen, RED, (posx, int(square_size/2)), radius)
                else:
                    pygame.draw.circle(screen, YELLOW, (posx, int(square_size/2)), radius)
                pygame.display.update()

            if event.type == pygame.MOUSEBUTTONDOWN:
                pygame.draw.rect(screen, BLACK, (0, 0, width, square_size))
                posx = event.pos[0]
                col = int(math.floor(posx / square_size))

                if is_valid_location(board, col) and not falling:
                    row = get_next_open_row(board, col)
                    falling = True
                    falling_piece = 1 if turn == 0 else 2
                    falling_col = col
                    falling_row_target = row
                    falling_y = 0

        if falling:
            if falling_y < falling_row_target * square_size:
                falling_y += fall_speed
            else:
                drop_piece(board, falling_row_target, falling_col, falling_piece)
                # Se verifica si el jugador ganó
                if winning_move(board, falling_piece):
                    draw_board(board, screen, square_size, radius)
                    font = pygame.font.SysFont("monospace", 75)
                    label = font.render("Ganador!", True, RED if falling_piece == 1 else YELLOW)
                    screen.blit(label, (40,10))
                    pygame.display.update()
                    pygame.time.wait(3000)
                    game_over = True
                    continue  # Sale del ciclo principal

                falling = False
                turn = (turn + 1) % 2
                draw_board(board, screen, square_size, radius)

            # Redibujar el tablero y la ficha en caída
            draw_board(board, screen, square_size, radius)
            x_pos = int(falling_col * square_size + square_size / 2)
            color = RED if falling_piece == 1 else YELLOW
            pygame.draw.circle(screen, color, (x_pos, falling_y + square_size + radius), radius)
            pygame.display.update()

        clock.tick(60)

if __name__ == "__main__":
    main()
