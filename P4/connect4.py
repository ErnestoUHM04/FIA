import sys
import numpy as np
import pygame
import math
import time

# Dimensiones del tablero
ROWS = 6
COLUMNS = 7

# Colores (RGB)
BLUE = (0, 0, 255)
BLACK = (0, 0, 0)
RED = (255, 0, 0)
YELLOW = (255, 255, 0)
WHITE = (255, 255, 255)

# Dimensiones de la ventana
SQUARESIZE = 100
width = COLUMNS * SQUARESIZE
height = (ROWS+1) * SQUARESIZE  # Fila extra para mostrar el turno
size = (width, height)

RADIUS = int(SQUARESIZE/2 - 5)

pygame.init()
screen = pygame.display.set_mode(size)
myfont = pygame.font.SysFont("monospace", 75)

def create_board():
    board = np.zeros((ROWS, COLUMNS))
    return board

def drop_piece(board, row, col, piece):
    board[row][col] = piece

def is_valid_location(board, col):
    return board[0][col] == 0

def get_next_open_row(board, col):
    for r in range(ROWS-1, -1, -1):
        if board[r][col] == 0:
            return r
    return None

def print_board(board):
    print(np.flip(board, 0))

def winning_move(board, piece):
    # Horizontal
    for c in range(COLUMNS-3):
        for r in range(ROWS):
            if board[r][c] == piece and board[r][c+1] == piece and board[r][c+2] == piece and board[r][c+3] == piece:
                return True

    # Vertical
    for c in range(COLUMNS):
        for r in range(ROWS-3):
            if board[r][c] == piece and board[r+1][c] == piece and board[r+2][c] == piece and board[r+3][c] == piece:
                return True

    # Diagonal (ascendente)
    for c in range(COLUMNS-3):
        for r in range(3, ROWS):
            if board[r][c] == piece and board[r-1][c+1] == piece and board[r-2][c+2] == piece and board[r-3][c+3] == piece:
                return True

    # Diagonal (descendente)
    for c in range(COLUMNS-3):
        for r in range(ROWS-3):
            if board[r][c] == piece and board[r+1][c+1] == piece and board[r+2][c+2] == piece and board[r+3][c+3] == piece:
                return True

def draw_board(board):
    for c in range(COLUMNS):
        for r in range(ROWS):
            # Dibujar el tablero (cuadrados azules)
            pygame.draw.rect(screen, BLUE, (c*SQUARESIZE, r*SQUARESIZE+SQUARESIZE, SQUARESIZE, SQUARESIZE))
            # Dibujar los circulos (espacios vacíos negros)
            pygame.draw.circle(screen, BLACK, (int(c*SQUARESIZE+SQUARESIZE/2), int(r*SQUARESIZE+SQUARESIZE+SQUARESIZE/2)), RADIUS)
    
    for c in range(COLUMNS):
        for r in range(ROWS):
            if board[r][c] == 1:
                pygame.draw.circle(screen, RED, (int(c*SQUARESIZE+SQUARESIZE/2), height - int(r*SQUARESIZE+SQUARESIZE/2)), RADIUS)
            elif board[r][c] == 2:
                pygame.draw.circle(screen, YELLOW, (int(c*SQUARESIZE+SQUARESIZE/2), height - int(r*SQUARESIZE+SQUARESIZE/2)), RADIUS)
    pygame.display.update()

def animate_drop(col, final_row, piece):
    start_y = SQUARESIZE/2
    end_y = height - (final_row*SQUARESIZE + SQUARESIZE/2)
    x = int(col * SQUARESIZE + SQUARESIZE/2)
    steps = 20  # número de pasos de la animación
    for i in range(steps):
        y = start_y + (end_y - start_y) * (i+1) / steps
        # Redibuja el tablero con la pieza "en vuelo"
        screen.fill(BLACK)
        draw_board(board)
        color = RED if piece == 1 else YELLOW
        pygame.draw.circle(screen, color, (x, int(y)), RADIUS)
        pygame.display.update()
        pygame.time.delay(20)

board = create_board()
game_over = False
turn = 0  # 0: jugador 1, 1: jugador 2

draw_board(board)

while not game_over:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            sys.exit()

        # Mover el indicador en la parte superior
        if event.type == pygame.MOUSEMOTION:
            pygame.draw.rect(screen, BLACK, (0, 0, width, SQUARESIZE))
            posx = event.pos[0]
            if turn == 0:
                pygame.draw.circle(screen, RED, (posx, int(SQUARESIZE/2)), RADIUS)
            else:
                pygame.draw.circle(screen, YELLOW, (posx, int(SQUARESIZE/2)), RADIUS)
            pygame.display.update()

        if event.type == pygame.MOUSEBUTTONDOWN:
            pygame.draw.rect(screen, BLACK, (0, 0, width, SQUARESIZE))
            # Determinar la columna seleccionada
            posx = event.pos[0]
            col = int(math.floor(posx/SQUARESIZE))

            if is_valid_location(board, col):
                row = get_next_open_row(board, col)
                # Animar la caída de la pieza
                animate_drop(col, row, turn+1)
                drop_piece(board, row, col, turn+1)

                if winning_move(board, turn+1):
                    label = myfont.render("Jugador {} gana!".format(turn+1), 1, RED if turn == 0 else YELLOW)
                    screen.blit(label, (40,10))
                    game_over = True

                print_board(board)
                draw_board(board)

                # Cambiar de turno
                turn = (turn+1) % 2

                if game_over:
                    pygame.time.wait(3000)