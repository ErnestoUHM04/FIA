package reactivos;
/*
 * This is the Node class, used to store the moves for the A*
 */
public class Node implements Comparable<Node> {
    /*NODO:
     * debe apuntar a su padre                                                                                                                                                                                                                                                                                        
     * guarda el valor de f(n)
     * guarda el estado
    */
    public int row, col;
    public int g, h;
    public Node parent;

    public Node(int row, int col, int g, int h, Node parent) {
        this.row = row;
        this.col = col;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }

    public int getF() {
        return g + h;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getF(), other.getF());
    }
}