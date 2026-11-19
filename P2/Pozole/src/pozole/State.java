package pozole;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author 
 */
public class State {
    private byte[][] board; 
    private int posI; // Fila del espacio vacío
    private int posJ; // Columna del espacio vacío
    private char movement = 'n';
    private State parent = null;

    // Constructor a partir de una cadena de 16 dígitos (por ejemplo, "2831647051211109")
    public State(String sts) {   
        int n = 0;
        this.board = new byte[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {   
                char c = sts.charAt(n);
                board[i][j] = (byte) Character.getNumericValue(c);
                if (board[i][j] == 0) {
                    posI = i;
                    posJ = j;
                }
                n++;
            }
    }
    
    // Constructor a partir de un tablero, un movimiento y el estado padre
    public State(byte[][] board, char movement, State parent) {
        this.movement = movement;
        this.board = new byte[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                this.board[i][j] = board[i][j];
                if (board[i][j] == 0) {
                    posI = i;
                    posJ = j;
                }
            }
        this.parent = parent;
    }
    
    // Muestra el estado por consola
    public void show() {
        switch (movement) {
            case 'u' -> System.out.println("Move UP");
            case 'd' -> System.out.println("Move DOWN");
            case 'l' -> System.out.println("Move LEFT");
            case 'r' -> System.out.println("Move RIGHT");
            case 'n' -> System.out.println("START");
        }
        System.out.println("+-+-+-+-+-+-+-+");
        for (int i = 0; i < 4; i++) {
            System.out.print("|");
            for (int j = 0; j < 4; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println("\n+-+-+-+-+-+-+-+");
        }
    }
    
    // Realiza el movimiento: intercambia la posición del cero con la posición (i, j)
    public void swap(int i, int j) {
        int aux = board[i][j];
        board[i][j] = 0;
        board[posI][posJ] = (byte) aux;
        posI = i;
        posJ = j;
    }
    
    // Genera los estados vecinos (movimientos posibles)
    public List<State> getNeighbors() {
        List<State> neighbors = new ArrayList<>();
        byte[][] newBoard = new byte[4][4];
        
        // Clona el tablero actual
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                newBoard[i][j] = this.board[i][j];
     
        // Movimiento: Arriba
        if (posI > 0) {
            int newI = posI - 1;
            State newState = new State(newBoard, 'u', this);                
            newState.swap(newI, posJ);
            neighbors.add(newState);                        
        }
        
        // Movimiento: Abajo
        if (posI < 3) {
            int newI = posI + 1;
            State newState = new State(newBoard, 'd', this);
            newState.swap(newI, posJ);
            neighbors.add(newState);        
        }
                
        // Movimiento: Izquierda
        if (posJ > 0) {
            int newJ = posJ - 1;
            State newState = new State(newBoard, 'l', this);
            newState.swap(posI, newJ);
            neighbors.add(newState);
        }
        
        // Movimiento: Derecha
        if (posJ < 3) {
            int newJ = posJ + 1;
            State newState = new State(newBoard, 'r', this);                
            newState.swap(posI, newJ);
            neighbors.add(newState);
        }
                
        return neighbors;
    }
    
    public byte[][] getBoard() {
        return board;
    }
    
    // Comprueba si el estado actual es igual al estado objetivo
    public boolean isGoal(State goal) {
        boolean success = true;
        byte[][] goalBoard = goal.getBoard();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (goalBoard[i][j] != board[i][j]) {
                    success = false;
                    break;
                }
            }
        return success;        
    }
    
    public int getI() {
        return posI;
    }
    
    public int getJ() {
        return posJ;
    }
    
    public State getParent() {
        return parent;
    }
    
    public char getMovement() {
        return movement;
    }
    
    // Compara dos estados para ver si son iguales (evitar repeticiones)
    public boolean isEqual(State s) {
        boolean isEqual = true;
        byte[][] sBoard = s.getBoard();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (sBoard[i][j] != board[i][j]) {
                    isEqual = false;
                    break;
                }
            }
        return isEqual;        
    }
}