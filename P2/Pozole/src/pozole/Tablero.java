package pozole;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

/**
 *
 */
public class Tablero extends JFrame {
    private final JButton[][] jBoard = new JButton[4][4];   
    private final LinkedHashMap<String, BufferedImage> puzzle = new LinkedHashMap<>();
    private BufferedImage empty;
    private boolean depth = false;
    
    // Estado inicial y objetivo para el 15-puzzle (representados como cadenas de 16 dígitos)
    private final String start = "123456789ABCDE0F";
    private final String goal  = "123456789ABCDEF0";
    //                                     10-11-12-13-14-15
    //                                      A  B  C  D  E  F
   
    private final JMenuItem solveB = new JMenuItem("Solve BFS");
    private final JMenuItem solveD = new JMenuItem("Solve DFS");
    private final JSlider velocity = new JSlider(1, 200, 100);
    
    private long startTime; 
    private int totalNodes;
    private int totalLoops;
    private String thePath = "";
    
    // Para el DFS recursivo se almacena el estado solución cuando se encuentre
    private State solutionState = null;
    
    public Tablero() {       
        initComponents();
    }
      
    private void initComponents() {
        this.setTitle("15-Puzzle");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int width = pantalla.width;
        int height = pantalla.height;
        this.setBounds((width - 600) / 2, (height - 600) / 2, 600, 600); // Increase the window size
        velocity.setBounds(10, 600, 600, 20);
        
        JMenuBar mainMenu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        
        mainMenu.add(file);
        file.add(solveB);
        file.add(solveD);
        file.add(exit);
        this.setJMenuBar(mainMenu);
        velocity.setInverted(true);
        
        this.setLayout(null);
        this.imagePieces("imagenes/background.jpg");
        paintPieces();
        this.add(velocity);
        exit.addActionListener(evt -> gestionarExit(evt));  
        solveB.addActionListener(evt -> whichAlgorithm(evt)); 
        solveD.addActionListener(evt -> whichAlgorithm(evt));  
               
        // Maneja el cierre con la X del JFrame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent eventObject) {
                goodBye();
            }
        });       
    }
    
    private void goodBye() {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Are you sure?", "Exit", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION)
            System.exit(0);
    }
        
    private void gestionarExit(ActionEvent e) {
        goodBye();
    }
   
    // Separa la imagen en piezas
    private void imagePieces(String pathName) {
        try {
            BufferedImage buffer = ImageIO.read(new File(pathName));
            if (buffer == null) {
                throw new IllegalArgumentException("Image not found or unable to read: " + pathName);
            }
            BufferedImage subImage;
            int n = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    
                    subImage = buffer.getSubimage(j * 124, i * 124, 124, 124);  // Extrae un fragmento de la imagen 
                    String k = goal.substring(n, n + 1);
                    puzzle.put(k, subImage); // Almacena la pieza con clave basada en el estado objetivo
                    n++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    } 
     
    public void paintPieces() {
        int n = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                String k = start.substring(n, n + 1);
                BufferedImage subImage = puzzle.get(k);   
                jBoard[i][j] = new JButton();
                jBoard[i][j].setBounds(j * 124 + 1, i * 124 + 1, 124, 124); // Posición del botón
                this.add(jBoard[i][j]);                                                                        
                if (!k.equals("0"))
                    jBoard[i][j].setIcon(new ImageIcon(subImage));
                else
                    empty = subImage;
                n++;                 
            }
    }
    
    // Elige el algoritmo a usar (BFS o DFS recursivo)
    private void whichAlgorithm(ActionEvent e) {
        if (e.getSource() == solveD)  
            solveDFSRecursive(); // DFS recursivo (similar al DFS en C)
        else
            solveBFS();
    }
    
    // Búsqueda en anchura (BFS)
    private void solveBFS() {
        boolean success = false;
        totalNodes = 0;
        totalLoops = 0;
        State initialState = new State(start);
        State goalState = new State(goal);
        ArrayList<State> visited = new ArrayList<>();
        ArrayDeque<State> queue = new ArrayDeque<>();

        solveB.setEnabled(false);
        solveD.setEnabled(false);
        
        queue.offer(initialState);
        startTime = System.currentTimeMillis();
        
        while (!queue.isEmpty() && !success) { 
            totalLoops++;
            State current = queue.poll();
            
            if (current.isGoal(goalState)) {    
                success = true;
                System.out.println("Path found!");
                showBoard(current);
            } else {
                List<State> neighbors = current.getNeighbors(); // Expande el nodo
                visited.add(current);
                 
                for (State neighbor : neighbors) {
                    totalNodes++;
                    if (!repetido(visited, neighbor)) {
                        queue.offer(neighbor); // Agrega el nodo a la cola
                    }
                }                 
            }    
        } 
        if (!success) {
            JOptionPane.showMessageDialog(rootPane, "Path not found", "Sorry!!!", JOptionPane.WARNING_MESSAGE);
            System.out.println("Path not found");
        }
        System.out.println("Total Nodes: " + totalNodes);
        System.out.println("Total Loops: " + totalLoops);
    }
    
    // Búsqueda en profundidad recursiva (DFS) – similar a la función DFS_Solution en C
    private void solveDFSRecursive() {
        depth = true;
        totalNodes = 0;
        totalLoops = 0;
        State initialState = new State(start);
        State goalState = new State(goal);
        ArrayList<State> visited = new ArrayList<>();
        int maxDepth = 82; // Límite máximo de movimientos
        
        solveB.setEnabled(false);
        solveD.setEnabled(false);
        
        startTime = System.currentTimeMillis();
        boolean success = recursiveDFS(initialState, goalState, 0, maxDepth, visited);
        if (success && solutionState != null) {
            System.out.println("Path found!");
            showBoard(solutionState);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Path not found", "Sorry!!!", JOptionPane.WARNING_MESSAGE);
            System.out.println("Path not found");
        }
        System.out.println("Total Nodes: " + totalNodes);
        System.out.println("Total Loops: " + totalLoops);
    }
    
    // Método recursivo de DFS
    private boolean recursiveDFS(State current, State goal, int depth, int maxDepth, ArrayList<State> visited) {
        totalLoops++;
        if (depth > maxDepth)
            return false;
        if (current.isGoal(goal)) {
            solutionState = current;
            return true;
        }
        visited.add(current);
        for (State neighbor : current.getNeighbors()) {
            totalNodes++;
            if (!containsState(visited, neighbor)) {
                if (recursiveDFS(neighbor, goal, depth + 1, maxDepth, visited))
                    return true;
            }
        }
        return false;
    }
    
    // Ayuda a comprobar si un estado ya fue visitado (usando isEqual)
    private boolean containsState(ArrayList<State> list, State s) {
        for (State st : list) {
            if (st.isEqual(s))
                return true;
        }
        return false;
    }
    
    private void showBoard(State lastNode) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (depth) {
            this.setTitle("15-Puzzle (Depth-First Search)"); 
            buildSolution(lastNode);
        } else { 
            this.setTitle("15-Puzzle (Breadth-First Search)"); 
            buildPath(lastNode);
        }
        JOptionPane.showMessageDialog(rootPane, "Success!! \nPath with " + thePath.length() + " nodes"
            + "\nGenerated nodes: " + totalNodes + "\nLoops: " + totalLoops + "\nElapsed time: " + elapsed + " milliseconds", 
            "Good News!!!", JOptionPane.INFORMATION_MESSAGE);
        
        State initialState = new State(start);
        int i = initialState.getI();
        int j = initialState.getJ();
       
        // Se asume que Executor es una clase que anima la solución
        Executor exec = new Executor(jBoard, i, j, thePath, empty, velocity);
        exec.start();
    }  
    
    private void buildPath(State node) {
        if (node == null)
            return;
        buildPath(node.getParent());
        node.show();
        if (node.getMovement() != 'n')
            thePath = thePath + node.getMovement();
    }
    
    private void buildSolution(State node) {
        thePath = "";
        State n = node;
        while (n != null) {  
            thePath = thePath + n.getMovement();         
            n = n.getParent();         
        }
        StringBuilder sb = new StringBuilder(thePath);
        thePath = sb.reverse().toString();
    }
        
    // Evita nodos repetidos
    public boolean repetido(ArrayList<State> l, State s) {
        for (State ns : l) {
            if (ns.isEqual(s))
                return true;
        }
        return false;
    }
    
    // Método principal para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Tablero().setVisible(true);
        });
    }
}