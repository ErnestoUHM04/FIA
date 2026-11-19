package reactivos;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * @author macario
 *  version 2.0 Modified by: @ErnestoUHM04
 *                           @OviedoAparicioNadia
 */

public class Agente extends Thread
{
    private final String nombre;
    private int i;
    private int j;
    private final ImageIcon icon;
    private final int[][] matrix;
    private final JLabel tablero[][];
    private final ImageIcon motherIcon; 
    
    private JLabel casillaAnterior;
    //variable for the sample
    private boolean hasSample = false;
    //variables for the mothership
    private int mothershipRow = -1;
    private int mothershipCol = -1;

    private int samplesDelivered = 0;
    
    private LinkedList<Node> path;

    Random aleatorio = new Random(System.currentTimeMillis());
    
    public Agente(String nombre, ImageIcon icon, ImageIcon motherIcon, int[][] matrix, JLabel tablero[][], int initialRow, int initialCol){
        this.nombre = nombre;
        this.icon = icon;
        this.motherIcon = motherIcon;
        this.matrix = matrix;
        this.tablero = tablero;

        this.i = aleatorio.nextInt(matrix.length);
        this.j = aleatorio.nextInt(matrix.length);

        tablero[i][j].setIcon(icon);
        casillaAnterior = tablero[i][j];
    }
    
    public void setMothershipPosition(int row, int col) {
        this.mothershipRow = row;
        this.mothershipCol = col;
    }
    
    // the agent need to avoid the obstacles
    public void interactWithObstacle() {   
        System.out.println(nombre + " interact with obstacle");
        int[][] moves = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        boolean moved = false;

        for (int[] move : moves) {
            int newI = i + move[0];
            int newJ = j + move[1];

            if (newI >= 0 && newI < matrix.length && newJ >= 0 && newJ < matrix[0].length && matrix[newI][newJ] != 1) {
                i = newI;
                j = newJ;
                moved = true;
                break;
            }
        }

        if (!moved) {
            System.out.println(nombre + " is stuck!");
        }
    }

    // the agent need to collect the samples and get back to the mothership
    public void interactWithSample() {
        System.out.println(nombre + " collecting sample");
        hasSample = true;
        
        // Remove the sample from the board
        tablero[i][j].setIcon(null); 
        matrix[i][j] = 0; // Now the position is empty

        // Play an audio when the agent collects a sample
        playAudioClip("audio/Lego-batman-punch-sound-effects.aiff");

        actualizarPosicion();

        // Move towards the mothership using A* algorithm
        A_star();
        moveAlongPath();
        
        interactWithMothership();
        updateBoardWithTraces();
    }

    public void A_star() {
        // Inicializamos agregando el nodo inicial a ABIERTOS
        PriorityQueue<Node> openList = new PriorityQueue<>();
        // Inicializamos CERRADOS como vacía
        Set<Node> closedList = new HashSet<>(); 
        // Inicializamos nuestro primer nodo (posisción actual)
        Node startNode =  new Node (i, j, 0, heuristic(i,j), null);
        openList.add (startNode);

        while (!openList.isEmpty()) { // while there are nodes in OPENED
            Node currentNode = openList.poll(); // Tomamos el nodo de OPENED (priority queue)

            if (currentNode.row == mothershipRow && currentNode.col == mothershipCol) { // si logramos estar en la nave
                reconstructPath(currentNode);
                return;
            }

            closedList.add (currentNode);

            for(int[] move : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int newRow = currentNode.row + move[0];
                int newCol = currentNode.col + move[1];

                if (isValidMove(newRow, newCol) && !isInList(closedList, newRow, newCol)) { 
                    int newG = currentNode.g + 1; // g(n)
                    int newH = heuristic(newRow, newCol); // h(n)
                    Node neighborNode = new Node(newRow, newCol, newG, newH, currentNode);

                    if (!isInList(openList, newRow, newCol)) {
                        openList.add (neighborNode);
                    } else {
                        Node existingNode = getNodeFromList(openList, newRow, newCol);
                        if(newG < existingNode.g) {
                            openList.remove(existingNode);
                            openList.add(neighborNode);
                        }
                    }
                }
            }
        }
    }

    private int heuristic(int row, int col) {
        return Math.abs(row - mothershipRow) + Math.abs(col - mothershipCol);
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length && matrix[row][col] != 1;
    }

    private boolean isInList(Set<Node> list, int row, int col) {
        return list.stream().anyMatch(node -> node.row == row && node.col == col);
    }

    private boolean isInList(PriorityQueue<Node> list, int row, int col) {
        return list.stream().anyMatch(node -> node.row == row && node.col == col);
    }

    private Node getNodeFromList(PriorityQueue<Node> list, int row, int col) {
        return list.stream().filter(node -> node.row == row && node.col == col).findFirst().orElse(null);
    }

    private void reconstructPath(Node node) {
        path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node);
            node = node.parent;
        }
    }

    private void leaveTrace() {
        // EL 4 representa nuestro rastro
        matrix[i][j] = 4;

        // Ponemos el rastro en el tablero
        //ImageIcon traceIcon = new ImageIcon("imagenes/lego-1x1-transparent.png");
        //tablero[i][j].setIcon(traceIcon);

        int[][] moves = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        for (int[] move : moves) {
            int newI = i + move[0];
            int newJ = j + move[1];

            if (newI >= 0 && newI < matrix.length && newJ >= 0 && newJ < matrix[0].length && matrix[newI][newJ] == 0) {
                matrix[newI][newJ] = 4;
                //tablero[newI][newJ].setIcon(traceIcon);
                break; 
            }
        }

        tablero[i][j].revalidate();
        tablero[i][j].repaint();
    }

    private void moveAlongPath() {
        while (!path.isEmpty()) {
            Node nextNode = path.poll();
            i = nextNode.row;
            j = nextNode.col;
            leaveTrace();
            actualizarPosicionPath();

            //leaveTrace();
            try {
                sleep(100 + aleatorio.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    public void interactWithMothership() {
        //matrix[i][j] = 3; //do not eat the mothership
        matrix[mothershipRow][mothershipCol] = 3; //do not eat the mothership
        tablero[mothershipRow][mothershipCol].setIcon(motherIcon);
        System.out.println(nombre + " delivered sample");
        //tablero[i][j].setIcon(motherIcon); 
        hasSample = false;
        samplesDelivered++;
        System.out.println(nombre + " has delivered " + samplesDelivered + " samples.");

        actualizarPosicion(); 
    }

    @Override
    public void run() {
        while(true) {
            updateBoardWithTraces();
            casillaAnterior = tablero[i][j];
            
            int nI, nJ;

            // Movement 
            int[][] moves = {{1,0},{-1,0},{0,1},{0,-1}};
            do {
                int move = aleatorio.nextInt(4);
                nI = i + moves[move][0];
                nJ = j + moves[move][1];

            } while (nI < 0 || nI >= matrix.length || nJ < 0 || nJ >= matrix[0].length || matrix[nI][nJ] == 1);
            
            // Check if the next move is valid
            if (nI >= 0 && nI < matrix.length && nJ >= 0 && nJ < matrix[0].length && matrix[nI][nJ] != 1) {
                i = nI;
                j = nJ;
            }
                            
            // Check for obstacle
            if (matrix[i][j] == 1) {
                interactWithObstacle();
            } else if (matrix[i][j] == 2 && !hasSample) { // Check for sample
                interactWithSample();
            } else if (matrix[i][j] == 3) { // Case when the agent is in the mothership
                interactWithMothership();
            } else if (matrix[i][j] == 4 ){ // Case when the agent gets a trace
                // it needs to follow the trace
                updateBoardWithTraces();
                followTrace();
            } else {
                actualizarPosicion();
            }   

            try {
                // Speed of the agent
                sleep(100 + aleatorio.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    private void followTrace(){
        while (matrix[i][j] == 4){
            matrix[i][j] = 0;
            updateBoardWithTraces();
            int[][] moves = {{1,0},{-1,0},{0,1},{0,-1}};
            boolean moved = false;

            for (int[] move : moves){
                int newI = i + move[0];
                int newJ = j + move[1];

                if (newI >= 0 && newI < matrix.length && newJ >= 0 && newJ < matrix[0].length && matrix[newI][newJ] == 4) {
                    i = newI;
                    j = newJ;
                    actualizarPosicion();
                    moved = true;
                    break;
                }
            }

            if (!moved) 
                break;

            try{
                sleep(100 + aleatorio.nextInt(100));
            } catch (InterruptedException ex){
                ex.printStackTrace(System.out);
            }
        }
    }
    
    public synchronized void actualizarPosicion() {
        if(casillaAnterior != null && (mothershipRow >= 0 && mothershipCol >= 0) && casillaAnterior != tablero[mothershipRow][mothershipCol]) {
            casillaAnterior.setIcon(null);
        }

        casillaAnterior = tablero[i][j];

        if (casillaAnterior == tablero[mothershipRow][mothershipCol]) {
            tablero[mothershipRow][mothershipCol].setIcon(motherIcon);
        } else {
            tablero[i][j].setIcon(icon);
        }

        System.out.println(nombre + " in -> Row: " + i + " Col:" + j);

        //updateBoardWithTraces();
    }

    public synchronized void actualizarPosicionPath() {
        ImageIcon traceIcon = new ImageIcon("imagenes/lego-1x1-transparent.png");

        if(casillaAnterior != null && (mothershipRow >= 0 && mothershipCol >= 0) && casillaAnterior != tablero[mothershipRow][mothershipCol]) {
            casillaAnterior.setIcon(traceIcon);
        }

        casillaAnterior = tablero[i][j];

        if (casillaAnterior == tablero[mothershipRow][mothershipCol]) {
            tablero[mothershipRow][mothershipCol].setIcon(motherIcon);
        } else {
            tablero[i][j].setIcon(icon);
        }

        System.out.println(nombre + " in -> Row: " + i + " Col:" + j);

    }

    private void updateBoardWithTraces() {
        ImageIcon traceIcon = new ImageIcon("imagenes/lego-1x1-transparent.png");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                if (matrix[row][col] == 4) {
                    tablero[row][col].setIcon(traceIcon);
                }
            }
        }
    }

    private void playAudioClip(String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 500);

            clip.stop();
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}