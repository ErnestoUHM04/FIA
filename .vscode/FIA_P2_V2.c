/* This program is capable of solving both the 8-Puzzle and the 15-Puzzle

 *      @author : ErnestoUHM04
                  Ernesto Ulises Hernández Martínez
*/

#include <stdio.h>
#include <stdlib.h>

#define N 4 
#define MAX_DEPTH 80

/* FINAL ORDER
    int demoMatrix[4][4] = {
            //0 -> n  2nd []    [][this one]
            {1, 2, 3, 4}, // 0
            {5, 6, 7, 8}, // ->
            {9, 10, 11, 12}, // n
            {13, 14, 15, 0} // 1st []
    };
*/

// STRUCTURES
// Move structure (keep the positions i and j)
typedef struct move{
    int i;
    int j;
    int** matrix;
    struct move* parent;
}t_move, Move;

// STACK AND QUEUE STRUCTURES
typedef struct nodo{
    Move move;
    struct nodo *enlace;
}t_nodo,*Nodo;

typedef struct pila{
    int length;
    Nodo base;
    Nodo tope;
}t_pila, *Pila;

typedef struct cola{
    int length;
    Nodo first;
    Nodo last;
}t_cola, *Cola; 

// FUNCTIONS PROTOTYPES
// DFS ans BFS
int CheckPositions4x4(int** matrix);
int CheckPositions3x3(int** matrix);
int DFS_Solution(int** matrix, int n, int i, int j, int old_i, int old_j, int depth, int max_depth, Pila path);
int BFS_Solution(int** matrix, int n, int i, int j, Cola queue);
// Matrix
void Swap(int* a, int* b);
void PrintMatrix(int** matrix, int n);
int** CreateMatrix(int n);
void FreeMatrix(int*** matrix, int n);
void CopyMatrix(int** src, int** dest);
// Stack
void Push(Pila p, Move move);
void Pop(Pila p);
int CrearPila(Pila *p);
int DestruirPila(Pila *p);
void VerPila(Pila p);
void VerPilaBackwards(Pila p);
// Queue
int CreateQueue(Cola *c);
int DestroyQueue(Cola *c);
void EnQueue(Cola c, Move move);
void DeQueue(Cola c, Move* moveOut);

int main(){
    Pila path = NULL;
    if(CrearPila(&path) == 0)
        return -1;//ERROR if the stack isnt properly created
    Cola queue = NULL;
    if(CreateQueue(&queue) == 0)
        return -1;//ERROR if the queue isnt properly created
    int i, j, swt;
    int z_i, z_j; // Z means 0-zero
    int n = N ,max_depth = MAX_DEPTH; // n is the size of the matrix
    // 31 is the max movements for the 8 puzzle
    // 80 is the max movements for the 15 puzzle
    int** matrix = CreateMatrix(n);
    //for the demo lets use a 3x3 matrix
    int demoMatrix[N][N] = {
        //0 -> n  2nd []    [][this one]
        {1, 2, 3, 4}, // 0
        {5, 6, 8, 12}, // ->
        {9, 10, 11, 7}, // n
        {13, 14, 0, 15} // 1st []
    };
    
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
            matrix[i][j] = demoMatrix[i][j];
        }
    }
    printf("\n\tGiven matrix is:\n");
    PrintMatrix(matrix, n);

    //we need to get the position of the 0
    for(i = 0; i < n; i++){
        for (j = 0; j < n; j++){
            if (matrix[i][j] == 0){
                z_i=i;
                z_j=j;
                break;
            }
        }
    }
    // For debugging purposes comment this section
    
    do{
        printf("\n\tSolve puzzle by \n1. DFS\n2. BFS\n");
        scanf("%d", &swt);
    }while(swt > 2 || swt <= 0);

    if (swt == 1){
        // Depth First Search
        if (DFS_Solution(matrix, n, z_i, z_j, z_i, z_j, 0, max_depth, path)){ // we start from the position of the 0
            VerPila(path);
        } else {
            printf("\n\tNo solution found\n");
        }
    }else if (swt ==2){
        // Breadth First Search
        if (BFS_Solution(matrix, n, z_i, z_j, queue)){ // we start from the position of the 0
            //VerPila(path);
        } else {
            printf("\n\tNo solution found\n");
        }
    }
    
    /*
    if (BFS_Solution(matrix, n, z_i, z_j, queue, path)){ // we start from the position of the 0
        //VerPila(path);
    } else {
        printf("\n\tNo solution found\n");
    }
    */
    DestroyQueue(&queue);
    DestruirPila(&path);
    FreeMatrix(&matrix, n);
    return 0;
}

// this function checks if the matrix is in the correct order
int CheckPositions4x4(int** matrix){
    if(matrix[0][0] == 1 && matrix[0][1] == 2 && matrix[0][2] == 3 && matrix[0][3] == 4 &&
       matrix[1][0] == 5 && matrix[1][1] == 6 && matrix[1][2] == 7 && matrix[1][3] == 8 &&
       matrix[2][0] == 9 && matrix[2][1] == 10 && matrix[2][2] == 11 && matrix[2][3] == 12 &&
       matrix[3][0] == 13 && matrix[3][1] == 14 && matrix[3][2] == 15 && matrix[3][3] == 0)
        return 1; // 1 is true
    return 0;    // 0 is false
}

// this function checks if the demo matrix (3x3) is in the correct order
int CheckPositions3x3(int** matrix){
    if(matrix[0][0] == 1 && matrix[0][1] == 2 && matrix[0][2] == 3 &&
       matrix[1][0] == 4 && matrix[1][1] == 5 && matrix[1][2] == 6 &&
       matrix[2][0] == 7 && matrix[2][1] == 8 && matrix[2][2] == 0)
        return 1; // 1 is true
    return 0;    // 0 is false
}

// DEPTH FIRST SEARCH
// We need to mark the visited positions in order to avoid cycles
int DFS_Solution(int** matrix, int n, int i, int j, int old_i, int old_j, int depth, int max_depth, Pila path){
    // dont get to deep
    if (depth > max_depth)
        return 0; 
    //Starting from the position of the 0
    //we need to move to all positions available and check if the matrix is in the final order
    //to move, we swap positions, the actual position (the 0), with the nort, south, east and west positions 
    //we need to avoid returning to the previous position
    if (i < 0 || i >= n || j < 0 || j >= n) 
        return 0;

    if (CheckPositions4x4(matrix)){ // if true (all the positiosn are in order)
        printf("\n\tThe matrix is in the final order\n"); // we got it
        PrintMatrix(matrix, n);
        printf("Moves used: %d \n", depth);
        return 1;
    }

    // Try moving in all four directions
    int moves[4][2] = {  
        {-1,  0}, // Move up
        { 1,  0}, // Move down
        { 0, -1}, // Move left
        { 0,  1}  // Move right
    };

    for (int k = 0; k < 4; k++) {
        int new_i = i + moves[k][0];
        int new_j = j + moves[k][1];

        if (new_i >= 0 && new_i < n && new_j >= 0 && new_j < n && 
            (new_i != old_i || new_j != old_j)) { // Avoid going back
            Swap(&matrix[i][j], &matrix[new_i][new_j]); // Move empty space
            //PrintMatrix(matrix, n); //this line is used to debug
            Move move = {new_i, new_j};
            move.matrix = CreateMatrix(n);
            CopyMatrix(matrix, move.matrix);
            //PrintMatrix(move.matrix, n); // this line is used to debug
            Push(path, move); //push
            if (DFS_Solution(matrix, n, new_i, new_j, i, j, depth + 1, max_depth, path)) // Recursive call
                return 1;
            Pop(path); //pop
            Swap(&matrix[i][j], &matrix[new_i][new_j]); // Backtrack
        }
    }
    return 0;
}

// BREADTH FIRST SEARCH
// for this one we need a Queue
int BFS_Solution(int** matrix, int n, int i, int j, Cola queue){
    // moves in all four directions
    int success = 0; //success is false
    int moves[4][2] = {  
        {-1,  0}, // Move up
        { 1,  0}, // Move down
        { 0, -1}, // Move left
        { 0,  1}  // Move right
    };
    // we will try these moves from the first state, ensuring visiting all these nodes first
    // we will need to initialize and enqueue our first move
    // that being the starting point
    Move startMove = {i, j};
    startMove.matrix = CreateMatrix(n);        
    CopyMatrix(matrix, startMove.matrix);
    startMove.parent = NULL;
    //PrintMatrix(matrix, n);
    //PrintMatrix(startMove.matrix, n);
    EnQueue(queue, startMove);// we enqueue our first move
    
    while (queue -> first != NULL && success == 0){ // we continue while the queue has elements
        // we first need to dequeue the first move
        //PrintMatrix(matrix, n); // this line is used for debugging
        Move currentMove;
        currentMove.matrix = CreateMatrix(n);
        DeQueue (queue, &currentMove); // First Out ERROR
        //PrintMatrix(currentMove.matrix, n); // this line is used for debugging HERE THE MATRIX IS ALL ZEROS 
        i = currentMove.i;
        j = currentMove.j;
        //currentMove.matrix = CreateMatrix(n);
        CopyMatrix (currentMove.matrix, matrix);
        //PrintMatrix(matrix, n); // this line is used for debugging
        //PrintMatrix(currentMove.matrix, n); // this line is used for debugging HERE THE MATRIX IS ALL ZEROS
        //Push(path, currentMove);
        // now we check on the matrix
        if (CheckPositions4x4(currentMove.matrix)){
            success = 1; // success is true
            printf("\n\tThe matrix is in the final order\n");
            PrintMatrix(matrix, n);
            //Print the path to the solution
            Pila path;
            CrearPila (&path);
            Move* movePtr = &currentMove;
            while (movePtr != NULL){
                Push (path, *movePtr);
                movePtr = movePtr->parent;
            }
            int n_moves = path->length;
            printf("Moves used: %d \n", n_moves);
            VerPilaBackwards(path);
            DestruirPila(&path);
            FreeMatrix(&currentMove.matrix, n);
            return 1; // if the solution was found
        }

        //try all posible moves
        for (int k = 0; k < 4; k++){
            int new_i = i + moves[k][0];
            int new_j = j + moves[k][1];

            if (new_i >= 0 && new_i < n && new_j >= 0 && new_j < n){
                Move newMove = {new_i, new_j};
                newMove.matrix = CreateMatrix(n);
                CopyMatrix(matrix, newMove.matrix);
                //PrintMatrix(matrix, n); // this line is used to debug
                //PrintMatrix(newMove.matrix, n); // this line is used to debug
                Swap(&newMove.matrix[i][j], &newMove.matrix[new_i][new_j]); // FIXED ERROR: copying an empty matrix (all zeros)
                //PrintMatrix(newMove.matrix, n); // this line is used to debug
                newMove.parent = (Move*) malloc(sizeof(Move)); // **Link parent**
                *newMove.parent = currentMove;
                EnQueue(queue, newMove);
            }
        }
        //FreeMatrix(&currentMove.matrix, n);
    }
    return 0; // if no solution was found
}

//this function creates a matrix of n x n
int** CreateMatrix(int n){
    int i=0, j=0;
    int** matrix = NULL; 
    
    matrix = (int **) malloc ((n)* sizeof(int*));
    if(matrix == NULL){
        printf("\n\tError en reservar memoria\n");
        return matrix;
    }

    for (i=0; i < n; i++)
        matrix [i] = (int *) malloc ((n)* sizeof(int));               
    return matrix;
}

//this function frees the memory of the matrix
void FreeMatrix(int*** matrix, int n){
    int** aux = NULL;
    aux = *matrix;
    int i=0;
    
    for (i=0; i < n; i++)
        free (aux[i]);
    free(aux);
    *matrix = NULL;
}

//this function swaps two integers in the matrix
void Swap(int* a, int* b){
    int aux = *a;
    *a = *b;
    *b = aux;
}

//this function prints the matrix
void PrintMatrix(int** matrix, int n){
    int i=0, j=0;
    printf("\n");
    if (matrix != NULL){
        for (i=0; i < n; i++){
            for (j=0; j < n; j++)
                printf("%d ", matrix[i][j]);
            printf("\n");
        }
    }    
}

// STACK FUNCTIONS IMPLEMENTATION

// Push an element to the stack
void Push(Pila p, Move move){
    Nodo new = (Nodo) malloc (sizeof(t_nodo));
    if (new == NULL) return;

    new -> move = move;
    new -> enlace = p -> tope;

    //Si tenemos una pila vacia
    if(p -> base == NULL)
        p -> base = new;

    //Actualizamos la pila
    p -> tope = new;
    (p -> length)++;
}

// Pop an element from the stack
void Pop(Pila p){
    //Si tenemos una pila vacia
    if(p -> base == NULL)
        return;
    
    Nodo temp = p -> tope;
    p -> tope = p -> tope -> enlace;

    if(p -> tope == NULL) //Si hay un elemento
        p -> base = NULL;

    free(temp);
    (p -> length)--;
}

// Create a stack
int CrearPila(Pila *p){
    Pila q = NULL;
    q = (Pila) malloc (sizeof(t_pila));
    
    if (q == NULL) return 0; //si falla
    //inicializamos la pila
    q -> base = NULL;
    q -> tope = NULL;
    q -> length = 0;

    *p = q;
    return 1;
}

// Destroy a stack
int DestruirPila(Pila *p){
    if (*p == NULL) return 0;

    Nodo temp = (*p) -> tope;
    while(temp != NULL){
        Nodo siguiente = temp -> enlace;
        free(temp);
        temp = siguiente;
    }

    free(*p);
    *p = NULL;
    return 1;
}

// QUEUE FUNCTIONS IMPLEMENTATION
// Queue: [A] <- [B] <- [C] 
//         Front       Rear
//         First       Last
// Create a queue
int CreateQueue(Cola *c){
    Cola q = NULL;
    q = (Cola) malloc (sizeof(t_cola));
    
    if (q == NULL) return 0; //si falla
    //inicializamos la pila
    q -> first = NULL;
    q -> last = NULL;
    q -> length = 0;

    *c = q;
    return 1;
}

// Destroy a queue
int DestroyQueue(Cola *c){
    if (*c == NULL) return 0;

    Nodo temp = (*c) -> first; //Start from the front
    while(temp != NULL){
        Nodo siguiente = temp -> enlace;
        free(temp);
        temp = siguiente;
    }

    free(*c);
    *c = NULL;
    return 1;
}

//      ENQUEUE 
// Queue: [A] <- [B] <- [C] <- [D] 
//        Front             Rear
void EnQueue(Cola c, Move move){
    Nodo new = (Nodo) malloc (sizeof(t_nodo));
    if (new == NULL) return;

    new -> move.i = move.i;
    new -> move.j = move.j;
    new -> move.matrix = CreateMatrix(N); 
    CopyMatrix(move.matrix, new -> move.matrix);
    new -> move.parent = move.parent;

    new -> enlace = NULL;

    // if we have an empty queue
    if(c -> last == NULL){
        c -> last = new;
        c -> first = new;
    }
    
    // we add the new element at the rear
    c -> last -> enlace = new; // Link the old last node to the new one
    c -> last = new;         // Move rear pointer

    (c -> length)++;
}

//      DEQUEUE
// Queue: [B] <- [C] <- [D]
//        Front       Rear
void DeQueue(Cola c, Move* moveOut){
    // if we have an empty queue
    if(c -> first == NULL) return;

    Nodo temp  = c -> first;
    *moveOut = temp -> move; // we store the dequeued move here
    c -> first =  c -> first -> enlace; // Move front pointer

    //free(temp);

    if (c->first == NULL) // If queue is now empty, update last
        c->last = NULL;

    //free(temp); //ERROR: Double free
    (c -> length)--;
}

// Print the stack          DONE
// here we also need to print the matrix, for a better visualization
// for printing the stack, if do it this way, we actually get the steps for solving the matrix, but in the other way around
// meaning that we actually start from the end
void VerPila(Pila p){
    Pila pilatemp = NULL;
    if(CrearPila(&pilatemp) == 0)
        return;
    Nodo temp = p -> tope;

    while(temp != NULL){
        //PrintMatrix(temp -> move.matrix, 3); // THIS CURRENTLY PRINTS THE FINAL FORM OF THE MATRIX
        // we need to print how was the matrix on that moment
        Push (pilatemp, temp -> move); // we get a new stack, with the element of the path stack, in order to then show this path
        //PrintMatrix(temp -> move.matrix, N);
        //printf("Move to (%d, %d)\n", temp->move.i, temp->move.j);
        temp = temp->enlace;
    }
    temp = pilatemp -> tope;
    while(temp != NULL){
        PrintMatrix(temp -> move.matrix, N);
        printf("Move to (%d, %d)\n", temp->move.i, temp->move.j);
        temp = temp->enlace;
    }
    printf("\n");
    DestruirPila(&pilatemp);
}

void VerPilaBackwards(Pila p){
    Pila pilatemp = NULL;
    if(CrearPila(&pilatemp) == 0)
        return;
    Nodo temp = p -> tope;

    while(temp != NULL){
        PrintMatrix(temp -> move.matrix, N);
        printf("Move to (%d, %d)\n", temp->move.i, temp->move.j);
        temp = temp->enlace;
    }
    printf("\n");
    DestruirPila(&pilatemp);
}

// Function to copy the matrix 
void CopyMatrix(int** src, int** dest) { // THIS WORKS JUST FINE
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            dest[i][j] = src[i][j];
        }
    }
}