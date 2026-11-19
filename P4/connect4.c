#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <SDL.h> 

/*              For compiling
gcc connect4.c -o connect4 -I/opt/homebrew/include/SDL2 -D_THREAD_SAFE -L/opt/homebrew/lib -lSDL2 -lm    
./connect4
*/

#define ROWS 6
#define COLS 7
#define CELL_SIZE 100
#define SCREEN_WIDTH (COLS * CELL_SIZE)
#define SCREEN_HEIGHT ((ROWS + 1) * CELL_SIZE)

#define EMPTY 0
#define RED 1
#define YELLOW 2

#define INT_MAX 2147483647
#define INT_MIN -2147483647

// Colors
SDL_Color BLUE = {0, 0, 255, 255};
SDL_Color BLACK = {0, 0, 0, 255};
SDL_Color RED_COLOR = {255, 0, 0, 255};
SDL_Color YELLOW_COLOR = {255, 255, 0, 255};

// Board matrix 
int board[ROWS][COLS] = {0};

/*  CONNECT 4
     1  2  3  4  5  6  7
    [0][0][0][0][0][0][0] 1
    [0][0][0][0][0][0][0] 2
    [0][0][0][0][0][0][0] 3
    [0][0][0][0][0][0][0] 4
    [0][0][0][0][0][0][0] 5
    [0][0][0][0][0][0][0] 6

    [0] = Empty space
    [1] = Red disc      (Player) - (Player 1)
    [2] = Yellow disc   (CPU)    - (Player 2)
*/

// Function declarations
void drawBoard(SDL_Renderer* renderer);
void dropPiece(int col, int player);
int getAvailableRow(int col);
void SDL_RenderFillCircles(SDL_Renderer *renderer, int cx, int cy, int radius);
void printBoardState();
int checkWinner();

int cpu_move(int diff);
void CopyBoard(int ** boardcopy);
void PrintCopiedBoard(int** boardcopy);

int MinMax(int diff, int depth, int** boardcopy, int maximizingPlayer);
int IsValidMove(int** boardcopy, int col);
void MakeMove(int** boardcopy, int col, int player);
void UndoMove(int** boardcopy, int col);
int heuristic(int ** boardcopy);

int** CreateMatrix();    
void FreeMatrix(int*** matrix, int rows);

int main(){
    int i=6, j=7;

    printBoardState();

    //lets play connect four
    SDL_Init(SDL_INIT_VIDEO);
    SDL_Window* window = SDL_CreateWindow("Connect 4", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, SCREEN_WIDTH, SCREEN_HEIGHT, SDL_WINDOW_SHOWN);
    SDL_Renderer* renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
    
    int running = 1;
    int turn = RED;
    //Here he ask the player the game mode
    char mode[5];
    printf("\n\tChoose game mode: PvP (two players) or PvCPU (against computer): \n");
    scanf("%s", mode);

    while (strcmp(mode, "PvP") != 0 && strcmp(mode, "PvCPU") != 0) {
        printf("Invalid choice. Type 'PvP' or 'PvCPU': ");
        scanf("%s", mode);
    }
    // the difficulty will indicate us how deep to go in the Min-Max algortihm
    int diff = 0;
    if (strcmp(mode, "PvCPU") == 0){
        do{
            printf("\nChoose your difficulty from a number from 1 to 10:\n");
            scanf("%d", &diff);
        }while(diff <= 0 );
    }
    
    while (running) {
        SDL_Event event;
        while (SDL_PollEvent(&event)) {
            if (event.type == SDL_QUIT) {
                running = 0;
            } else if (event.type == SDL_MOUSEBUTTONDOWN) {
                int col;
                if (turn == RED || strcmp(mode, "PvP") == 0) {
                    col = event.button.x / CELL_SIZE;
                } else {
                    col = cpu_move(diff);
                }
                dropPiece(col, turn);
                int winner = checkWinner();
                if (winner) {
                    printf("Player %d wins!\n", winner);
                    running = 0;
                }
                turn = (turn == RED) ? YELLOW : RED;
            }
        }

        // Render graphics
        SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
        SDL_RenderClear(renderer);
        drawBoard(renderer);
        SDL_RenderPresent(renderer);
    }

    if (checkWinner()) {
        SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
        SDL_RenderClear(renderer);
        SDL_Color winColor = (checkWinner() == RED) ? RED_COLOR : YELLOW_COLOR;
        SDL_SetRenderDrawColor(renderer, winColor.r, winColor.g, winColor.b, winColor.a);
        SDL_Rect winRect = {SCREEN_WIDTH / 4, SCREEN_HEIGHT / 3, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 3};
        SDL_RenderFillRect(renderer, &winRect);
        SDL_RenderPresent(renderer);
        SDL_Delay(3000);
    }

    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_Quit();

    //FreeMatrix(&board, i, j);
    return 0;
}

void printBoardState() {
    printf("\nCurrent Board State:\n");
    for (int r = 0; r < ROWS; r++) {
        for (int c = 0; c < COLS; c++) {
            printf("%d ", board[r][c]);
        }
        printf("\n");
    }
    printf("\n");
}

int checkWinner() {
    for (int r = 0; r < ROWS; r++) {
        for (int c = 0; c < COLS; c++) {
            int player = board[r][c];
            if (player == EMPTY) continue;
                        
            // Check horizontal
            if (c + 3 < COLS && player == board[r][c + 1] && player == board[r][c + 2] && player == board[r][c + 3])
                return player;
        
            // Check vertical
            if (r + 3 < ROWS && player == board[r + 1][c] && player == board[r + 2][c] && player == board[r + 3][c])
                return player;
        
            // Check diagonal (down-right)
            if (r + 3 < ROWS && c + 3 < COLS && player == board[r + 1][c + 1] && player == board[r + 2][c + 2] && player == board[r + 3][c + 3])
                return player;
        
            // Check diagonal (down-left)
            if (r + 3 < ROWS && c - 3 >= 0 && player == board[r + 1][c - 1] && player == board[r + 2][c - 2] && player == board[r + 3][c - 3])
                return player;
        }
    }
    return 0; // No winner
}
        
// Function to drop a piece
void dropPiece(int col, int player) {
    int row = getAvailableRow(col);
        if (row != -1) {
            board[row][col] = player;
            printBoardState();
        }            
}
        
// Function to find the lowest available row in a column        
int getAvailableRow(int col) {
    for (int r = ROWS - 1; r >= 0; r--)
        if (board[r][col] == EMPTY) return r;
    return -1;
}   

int** CreateMatrix(){
    int i=0, j=0;
    int** matrix = NULL; 
    matrix = (int **) malloc ((ROWS)* sizeof(int*));
    if(matrix == NULL){
        printf("\n\tError allocating memory\n");
        return NULL;
    }

    for (i=0; i < COLS; i++){
        matrix [i] = (int *) malloc ((COLS)* sizeof(int)); 
        if(matrix[i] == NULL){
            printf("\n\tError allocating memory\n");
            for (int j = 0; j < i; j++) {
                free(matrix[j]);
            }
            free(matrix);
            return NULL;
        }
    }
    
    for (i=0; i < ROWS; i++){
        for (j=0; j < COLS; j++)
            matrix[i][j] = 0;
    }

    return matrix;
}

//this function frees the memory of the matrix
void FreeMatrix(int*** matrix, int rows){
    if (matrix == NULL || *matrix == NULL) return;

    int** aux = NULL;
    aux = *matrix;
    int k=0;
    
    for (k=0; k < rows; k++)
        free (aux[k]);
    free(aux);
    *matrix = NULL;
}

// Function to draw the board
void drawBoard(SDL_Renderer* renderer) {
    // Draw background
    SDL_SetRenderDrawColor(renderer, BLUE.r, BLUE.g, BLUE.b, BLUE.a);
    SDL_Rect boardRect = {0, CELL_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT - CELL_SIZE};
    SDL_RenderFillRect(renderer, &boardRect);

    // Draw cells
    for (int r = 0; r < ROWS; r++) {
        for (int c = 0; c < COLS; c++) {
            int x = c * CELL_SIZE + CELL_SIZE / 2;
            int y = (r + 1) * CELL_SIZE + CELL_SIZE / 2;
            SDL_Color color = BLACK;
            if (board[r][c] == RED) color = RED_COLOR;
            else if (board[r][c] == YELLOW) color = YELLOW_COLOR;

            SDL_SetRenderDrawColor(renderer, color.r, color.g, color.b, color.a);
            SDL_RenderFillCircles(renderer, x, y, CELL_SIZE / 3);
        }
    }
}

// Function to draw a filled circle
void SDL_RenderFillCircles(SDL_Renderer *renderer, int cx, int cy, int radius) {
    for (int w = -radius; w <= radius; w++) {
        for (int h = -radius; h <= radius; h++) {
            if (w * w + h * h <= radius * radius) { // Check if point is inside circle
                SDL_RenderDrawPoint(renderer, cx + w, cy + h);
            }
        }
    }
}

//  int board[ROWS][COLS] = {0};
//  THIS IS WHERE WE IMPLEMENT THE MIN-MAX ALGORITHM FOR THE CPU ANSWER
int cpu_move(int diff){
    int depth = 0;
    // we will copy the current state of the board to calculate the best move
    int** boardcopy = NULL;
    boardcopy = CreateMatrix();
    CopyBoard(boardcopy);
    //PrintCopiedBoard(boardcopy);
    //now that we have copied the board
    int bestScore = INT_MIN;
    int bestMove = -1;
    // Try every column and choose the best one
    for (int i = 0; i < COLS; i++) {
        if (IsValidMove(boardcopy, i)) {
            MakeMove(boardcopy, i, 1); // Simulate CPU move
            int score = MinMax(diff, depth + 1, boardcopy, 0); // Minimize opponent
            UndoMove(boardcopy, i); // Undo move
            if (score > bestScore) {
                bestScore = score;
                bestMove = i;
            }
        }
    }

    FreeMatrix(&boardcopy, ROWS);
    // we will also have to

    return bestMove; // after seeing the moves in min max we return the best one
    // We have to return the column that the next piece will drop
}

int MinMax(int diff, int depth, int** boardcopy, int maximizingPlayer){
    if (depth >= diff){
        return heuristic(boardcopy);
        // if we get to the wanted depth, then we calculate the heuristic
    } 
    int i=0;
    int bestScore;
    if (maximizingPlayer) {
        bestScore = INT_MIN; // Maximize AI's move
        for (int i = 0; i < COLS; i++) {
            if (IsValidMove(boardcopy, i)) {
                MakeMove(boardcopy, i, 1); // AI makes a move
                int score = MinMax(diff, depth + 1, boardcopy, 0); // Minimize opponent's move
                UndoMove(boardcopy, i); // Undo move
                if (score > bestScore) {
                    bestScore = score;
                }
            }
        }
    } else {
        bestScore = INT_MAX; // Minimize opponent's move
        for (int i = 0; i < COLS; i++) {
            if (IsValidMove(boardcopy, i)) {
                MakeMove(boardcopy, i, 2); // Opponent makes a move
                int score = MinMax(diff, depth + 1, boardcopy, 1); // Maximize AI's move
                UndoMove(boardcopy, i); // Undo move
                if (score < bestScore) {
                    bestScore = score;
                }
            }
        }
    }
    return bestScore;
}

void CopyBoard(int ** boardcopy){
    if(boardcopy == NULL){
        printf("\n\tError copying board\n");
        return;
    }
    int i=0, j=0;
    for(i=0; i < ROWS; i++){
        for(j=0; j < COLS; j++)
            boardcopy[i][j] = board[i][j];
    }
}

void PrintCopiedBoard(int** boardcopy){
    int i=0, j=0;
    for(i=0; i < ROWS; i++){
        for(j=0; j < COLS; j++)
            printf("%d ", boardcopy[i][j]);
        printf("\n");
    }
}

int heuristic(int ** boardcopy){
    /*  WE ARE THE CPU
    Eval = Cant3InLine(CPU) + Cant2InLine(CPU) + Cant1InLine(CPU) -
            Cant3InLine(P) - Cant2InLine(P) - Cant1InLine(P)
    */
    int i=0, j=0;
    int eval = 0;
    int cpu = 0; // CPU is Yellow (2)
    int player = 0; // Player is Red (1)

    for(i=0; i < ROWS; i++){
        for(j=0; j < COLS; j++){
            if (boardcopy[i][j] == YELLOW && i >= 0 && i < ROWS && j >= 0 && j < COLS){ // 1 in line 
                // we have to check the number of pieces in line
                // we will check horizontally, vertically and diagonally
                // we will also have to check the number of pieces in line for the player
                // and subtract it from the eval
                if(boardcopy[i+1][j] == YELLOW && i+1 >= 0 && i+1 < ROWS && j >= 0 && j < COLS){ // check horizontally (-> this way)   2 in line
                    if(boardcopy[i+2][j] == YELLOW && i+2 >= 0 && i+2 < ROWS && j >= 0 && j < COLS){ // 3 in line
                        if(boardcopy[i+3][j] == YELLOW && i+3 >= 0 && i+3 < ROWS && j >= 0 && j < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                /*
                if(boardcopy[i-1][j] == YELLOW && i-1 >= 0 && i-1 < ROWS && j >= 0 && j < COLS){// check horizontally (<- this way)   2 in line
                    if(boardcopy[i-2][j] == YELLOW && i-2 >= 0 && i-2 < ROWS && j >= 0 && j < COLS){ // 3 in line
                        if(boardcopy[i-3][j] == YELLOW && i-3 >= 0 && i-3 < ROWS && j >= 0 && j < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                */
                /*
                if(boardcopy[i][j+1] == YELLOW && i >= 0 && i < ROWS && j+1 >= 0 && j+1 < COLS){ // check vertically (way up)   2 in line
                    if(boardcopy[i][j+2] == YELLOW && i >= 0 && i < ROWS && j+2 >= 0 && j+2 < COLS){ // 3 in line
                        if(boardcopy[i][j+3] == YELLOW && i >= 0 && i < ROWS && j+3 >= 0 && j+3 < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                */
                
                if(boardcopy[i][j-1] == YELLOW && i >= 0 && i < ROWS && j-1 >= 0 && j-1 < COLS){ // check vertically (way down)   2 in line
                    if(boardcopy[i][j-2] == YELLOW && i >= 0 && i < ROWS && j-2 >= 0 && j-2 < COLS){ // 3 in line
                        if(boardcopy[i][j-3] == YELLOW && i >= 0 && i < ROWS && j-3 >= 0 && j-3 < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                
                if(boardcopy[i+1][j+1] == YELLOW && i+1 >= 0 && i+1 < ROWS && j+1 >= 0 && j+1 < COLS){ // check diagonally (down-right)   2 in line
                    if(boardcopy[i+2][j+2] == YELLOW && i+2 >= 0 && i+2 < ROWS && j+2 >= 0 && j+2 < COLS){ // 3 in line
                        if(boardcopy[i+3][j+3] == YELLOW && i+3 >= 0 && i+3 < ROWS && j+3 >= 0 && j+3 < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                /*
                if(boardcopy[i-1][j-1] == YELLOW && i-1 >= 0 && i-1 < ROWS && j-1 >= 0 && j-1 < COLS){ // check diagonally (up-left)   2 in line
                    if(boardcopy[i-2][j-2] == YELLOW && i-2 >= 0 && i-2 < ROWS && j-2 >= 0 && j-2 < COLS){ // 3 in line
                        if(boardcopy[i-3][j-3] == YELLOW && i-3 >= 0 && i-3 < ROWS && j-3 >= 0 && j-3 < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                */
                /*
                if(boardcopy[i+1][j-1] == YELLOW && i+1 >= 0 && i+1 < ROWS && j-1 >= 0 && j-1 < COLS){ // check diagonally (down-left)   2 in line
                    if(boardcopy[i+2][j-2] == YELLOW && i+2 >= 0 && i+2 < ROWS && j-2 >= 0 && j-2 < COLS){ // 3 in line
                        if(boardcopy[i+3][j-3] == YELLOW && i+3 >= 0 && i+3 < ROWS && j-3 >= 0 && j-3 < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                */
                /*
                if(boardcopy[i-1][j+1] == YELLOW && i-1 >= 0 && i-1 < ROWS && j+1 >= 0 && j+1 < COLS){ // check diagonally (up-right)   2 in line
                    if(boardcopy[i-2][j+2] == YELLOW && i-2 >= 0 && i-2 < ROWS && j+2 >= 0 && j+2 < COLS){ // 3 in line
                        if(boardcopy[i-3][j+3] == YELLOW && i-3 >= 0 && i-3 < ROWS && j+3 >= 0 && j+3 < COLS){ // 4 in line
                            cpu += 1000; // (win)
                        }
                        cpu+=3;
                    }
                    cpu+=2;
                }
                */
                cpu++;
            }
            if (boardcopy[i][j] == RED && i >= 0 && i < ROWS && j >= 0 && j < COLS){
                // we have to check the number of pieces in line
                // we will check horizontally, vertically and diagonally
                // we will also have to check the number of pieces in line for the cpu
                // and add it to the eval
                if(boardcopy[i+1][j] == RED && i+1 >= 0 && i+1 < ROWS && j >= 0 && j < COLS){ // check horizontally (-> this way)   2 in line
                    if(boardcopy[i+2][j] == RED && i+2 >= 0 && i+2 < ROWS && j >= 0 && j < COLS){ // 3 in line
                        if(boardcopy[i+3][j] == RED && i+3 >= 0 && i+3 < ROWS && j >= 0 && j < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                /*
                if(boardcopy[i-1][j] == RED && i-1 >= 0 && i-1 < ROWS && j >= 0 && j < COLS){// check horizontally (<- this way)   2 in line
                    if(boardcopy[i-2][j] == RED && i-2 >= 0 && i-2 < ROWS && j >= 0 && j < COLS){ // 3 in line
                        if(boardcopy[i-3][j] == RED && i-3 >= 0 && i-3 < ROWS && j >= 0 && j < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                */
                /*
                if(boardcopy[i][j+1] == RED && i >= 0 && i < ROWS && j+1 >= 0 && j+1 < COLS){ // check vertically (way up)   2 in line
                    if(boardcopy[i][j+2] == RED && i >= 0 && i < ROWS && j+2 >= 0 && j+2 < COLS){ // 3 in line
                        if(boardcopy[i][j+3] == RED && i >= 0 && i < ROWS && j+3 >= 0 && j+3 < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                */
                if(boardcopy[i][j-1] == RED && i >= 0 && i < ROWS && j-1 >= 0 && j-1 < COLS){ // check vertically (way down)   2 in line
                    if(boardcopy[i][j-2] == RED && i >= 0 && i < ROWS && j-2 >= 0 && j-2 < COLS){ // 3 in line
                        if(boardcopy[i][j-3] == RED && i >= 0 && i < ROWS && j-3 >= 0 && j-3 < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                
                if(boardcopy[i+1][j+1] == RED && i+1 >= 0 && i+1 < ROWS && j+1 >= 0 && j+1 < COLS){ // check diagonally (down-right)   2 in line
                    if(boardcopy[i+2][j+2] == RED && i+2 >= 0 && i+2 < ROWS && j+2 >= 0 && j+2 < COLS){ // 3 in line
                        if(boardcopy[i+3][j+3] == RED && i+3 >= 0 && i+3 < ROWS && j+3 >= 0 && j+3 < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                /*
                if(boardcopy[i-1][j-1] == RED && i-1 >= 0 && i-1 < ROWS && j-1 >= 0 && j-1 < COLS){ // check diagonally (up-left)   2 in line
                    if(boardcopy[i-2][j-2] == RED && i-2 >= 0 && i-2 < ROWS && j-2 >= 0 && j-2 < COLS){ // 3 in line
                        if(boardcopy[i-3][j-3] == RED && i-3 >= 0 && i-3 < ROWS && j-3 >= 0 && j-3 < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                */
                /*
                if(boardcopy[i+1][j-1] == RED && i+1 >= 0 && i+1 < ROWS && j-1 >= 0 && j-1 < COLS){ // check diagonally (down-left)   2 in line
                    if(boardcopy[i+2][j-2] == RED && i+2 >= 0 && i+2 < ROWS && j-2 >= 0 && j-2 < COLS){ // 3 in line
                        if(boardcopy[i+3][j-3] == RED && i+3 >= 0 && i+3 < ROWS && j-3 >= 0 && j-3 < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                */
                /*
                if(boardcopy[i-1][j+1] == RED && i-1 >= 0 && i-1 < ROWS && j+1 >= 0 && j+1 < COLS){ // check diagonally (up-right)   2 in line
                    if(boardcopy[i-2][j+2] == RED && i-2 >= 0 && i-2 < ROWS && j+2 >= 0 && j+2 < COLS){ // 3 in line
                        if(boardcopy[i-3][j+3] == RED && i-3 >= 0 && i-3 < ROWS && j+3 >= 0 && j+3 < COLS){ // 4 in line
                            player += 1000; // (win)
                        }
                        player+=3;
                    }
                    player+=2;
                }
                */
                player++;
            }
        }
    }
    eval = cpu - player;
    return eval;
}

int IsValidMove(int** boardcopy, int col) {
    return (col >= 0 && col < COLS && boardcopy[0][col] == EMPTY);
}

void MakeMove(int** boardcopy, int col, int player) {
    for (int r = ROWS - 1; r >= 0; r--) {
        if (boardcopy[r][col] == EMPTY) {
            boardcopy[r][col] = player;
            break;
        }
    }
}

void UndoMove(int** boardcopy, int col) {
    for (int r = 0; r < ROWS; r++) {
        if (boardcopy[r][col] != EMPTY) {
            boardcopy[r][col] = EMPTY;
            break;
        }
    }
}