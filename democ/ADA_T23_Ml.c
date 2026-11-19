//ADA T23 Hernandez Martinez 
//Monedas limitadas    50  25  5   1
//cantidad  ej.        
//                     1   2   3   7
#include <stdio.h>
#include <stdlib.h>

//estructura pila
typedef struct nodo{
    int valor;
    struct nodo *enlace;
}t_nodo,*Nodo;

typedef struct pila{
    int length;
    Nodo base;
    Nodo tope;
}t_pila, *Pila;

int maximoposible(int *cantm, int* coins);
void cambio(int n, int *change, int *cantm, int* coins, Pila p, int i);
void push(Pila p, int val);
void pop(Pila p);
int crearPila(Pila *p);
int destruirPila(Pila *p);
void verPila(Pila p);

int main (){
    Pila p = NULL;
    if(crearPila(&p) == 0)
        return -1;//error si no se crea la pila correctamente
    int n = 0, change = 0;
    int coins[] = {50, 25, 5, 1};
    int cantm[] = {1, 2, 3, 7};
    printf("Cantidad cambio: \n");
    scanf("%d", &n);
    if(n > maximoposible(cantm, coins)){
        printf("No es posible darle ese cambio");
        return 1;//si no es posible dar el cambio
    }    
    cambio(n, &change, cantm, coins, p, 0);

    //Ver la pila de monedas
    printf("Cambio: \n");
    verPila(p);
    
    destruirPila(&p);
    return 0;
}

int maximoposible(int *cantm, int* coins){
    int k = 0, max = 0;
    for(k = 0; k < 4; k++)
        max += cantm[k]*coins[k];
    return max;
}

//Cambio
void cambio(int n, int *change, int *cantm, int* coins, Pila p, int i){
    if (i >= 4) return; 

    int val = 0;

    if(cantm[i] > 0){  //Si aun tenemos monedas de esta denominacion
        val = coins[i]; //Empezamos agregando la moneda de mayor denominacion => GREEDY
        cantm[i]--;     //Bajamos el conteo de monedas
        *change += val; 
    }else{              //Si hay 0 de esas monedas
        cambio(n, change, cantm, coins, p, i+1);
        return;
    }
    

    if(*change == n){  //Si conseguimos la solucion agregando esta moneda, entonces terminamos => GREEDY
        //guardamos la moneda (push)
        push(p, val);
        return;
    } else if(*change > n){ //si nos pasamos, entonces vamos con una moneda menor => GREEDY
        *change -= val; //quitamos el valor pasado
        cambio(n, change, cantm, coins, p, i+1);
    } else if(*change < n){ //si estamos por debajo, agregamos la moneda, y luego probamos con una menor => GREEDY
        push(p, val);
        cambio(n, change, cantm, coins, p, i);
    }
}

void push(Pila p, int val){
    Nodo new = (Nodo) malloc (sizeof(t_nodo));
    if (new == NULL) return;

    new -> valor = val;
    new -> enlace = p -> tope;

    //Si tenemos una pila vacia
    if(p -> base == NULL)
        p -> base = new;

    //Actualizamos la pila
    p -> tope = new;
    (p -> length)++;
}

void pop(Pila p){
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

int crearPila(Pila *p){
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

int destruirPila(Pila *p){
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

void verPila(Pila p){
    Nodo temp = p -> tope;

    while(temp != NULL){
        printf("%d ", temp -> valor);
        temp = temp -> enlace;
    }
    printf("\n");
}