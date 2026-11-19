#include <stdio.h>
#include <stdlib.h>

/* FINAL ORDER
    int demoMatrix[4][4] = {
            //0 -> n  2nd []    [][this one]
            {1, 2, 3, 4}, // 0
            {5, 6, 7, 8}, // ->
            {9, 10, 11, 12}, // n
            {13, 14, 15, 0} // 1st []
    };
*/

// STRUCTURE


// FUNCTIONS PROTOTYPES

void PrintMatrix(int** matrix, int n);
int** CreateMatrix(int n);
void FreeMatrix(int*** matrix, int n);
int CheckPositions(int** matrix);
void OrderByDepth(int** matrix, int n, int i, int j);
void Swap(int* a, int* b);


int main(){
    int i, j;
    int n = 4;
    int** matrix = CreateMatrix(n);
    int demoMatrix[4][4] = {
        //0 -> n  2nd []    [][this one]
        {1, 8, 9, 0}, // 0
        {2, 7, 10, 15}, // ->
        {3, 6, 11, 14}, // n
        {4, 5, 12, 13} // 1st []
    };
    
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
            matrix[i][j] = demoMatrix[i][j];
        }
    }

    //we need to get the position of the 0
    for(i = 0; i < n; i++){
        for (j = 0; j < n; j++){
            if (matrix[i][j] == 0){
                OrderByDepth(matrix, n, i, j); // we start from the position of the 0
                break;
            }
        }
    }
    
    FreeMatrix(&matrix, n);
    return 0;
}

int CheckPositions(int** matrix){
    if(matrix[0][0] == 1 && matrix[0][1] == 2 && matrix[0][2] == 3 && matrix[0][3] == 4 &&
       matrix[1][0] == 5 && matrix[1][1] == 6 && matrix[1][2] == 7 && matrix[1][3] == 8 &&
       matrix[2][0] == 9 && matrix[2][1] == 10 && matrix[2][2] == 11 && matrix[2][3] == 12 &&
       matrix[3][0] == 13 && matrix[3][1] == 14 && matrix[3][2] == 15 && matrix[3][3] == 0)
        return 1; // 1 is true
    return 0;    // 0 is false
}

void OrderByDepth(int** matrix, int n, int i, int j){
    //Starting from the position of the 0
    //we need to move to all positions available and check if the matrix is in the final order
    //to move, we swap positions, the actual position (the 0), with the nort, south, east and west positions 
    if (i < 0 || i >= n || j < 0 || j >= n)
        return;
    int check = CheckPositions(matrix); // check if the matrix is in the final order
    if (check == 1){ // if true (all the positiosn are in order)
        printf("\n\tThe matrix is in the final order\n");
        //PrintMatrix(matrix, n); // lets check the matrix
        return;
    }
    if (i + 1 < n) {
        Swap(&matrix[i][j], &matrix[i+1][j]);
        OrderByDepth(matrix, n, i+1, j);
        Swap(&matrix[i][j], &matrix[i+1][j]); // Swap back to original position
    }
    if (i - 1 >= 0) {
        Swap(&matrix[i][j], &matrix[i-1][j]);
        OrderByDepth(matrix, n, i-1, j);
        Swap(&matrix[i][j], &matrix[i-1][j]); // Swap back to original position
    }
    if (j + 1 < n) {
        Swap(&matrix[i][j], &matrix[i][j+1]);
        OrderByDepth(matrix, n, i, j+1);
        Swap(&matrix[i][j], &matrix[i][j+1]); // Swap back to original position
    }
    if (j - 1 >= 0) {
        Swap(&matrix[i][j], &matrix[i][j-1]);
        OrderByDepth(matrix, n, i, j-1);
        Swap(&matrix[i][j], &matrix[i][j-1]); // Swap back to original position
    }

}

int** CreateMatrix(int n){
    int i=0, j=0;
    int** matrix = NULL; 
    
    matrix = (int **) malloc ((n)* sizeof(int*));
    if(matrix == NULL){
        printf("\n\tError en reservar memoria\n");
        return matrix;
    }

    for (i=0; i < n; i++){
        matrix [i] = (int *) malloc ((n)* sizeof(int));
        //for (j=0; j < n; j++)
            //matrix[i][j] = 0;// 0 es fondo
    }                         // 1 es imagen 
    return (matrix);
}

void FreeMatrix(int*** matrix, int n){
    int** aux = NULL;
    aux = *matrix;
    int i=0;
    
    for (i=0; i < n; i++)
        free (aux[i]);
    free(aux);
    *matrix = NULL;
}

void Swap(int* a, int* b){
    int aux = *a;
    *a = *b;
    *b = aux;
}

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