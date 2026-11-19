
package reactivos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
 *
 * @author macario
 */

public class Escenario extends JFrame
{
    
    public JLabel[][] tablero;     
    private int[][] matrix;
    private final int dim = 15;

    public ImageIcon robot1;
    public ImageIcon robot2;
    public ImageIcon obstacleIcon;
    public ImageIcon sampleIcon;
    public ImageIcon actualIcon;
    public ImageIcon motherIcon;
    
    private Agente Batman;
    private Agente Robin;
   
    private final BackGroundPanel fondo = new BackGroundPanel(new ImageIcon("imagenes/gotham.jpg"));
    
    private final JMenu settings = new JMenu("Settigs");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");
    
    public Escenario()
    {
        this.setContentPane(fondo);
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(50,50,dim*50+35,dim*50+85);
        initComponents();
        
                // Clip de audio
        try
        {    
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("audio/02-You-Can-Bank-on-BatmanThe-Roof.aiff"));
            /* Other songs
            01-The-Batman-Theme.aiff
            02-You-Can-Bank-on-BatmanThe-Roof.aiff
            03-The-Bat-Cave.aiff
            04-Mysterious-PianoThe-Joker-Minions.aiff
            08-Jokers-Theme-Funland.aiff
            */
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out);
        }
    }
        
    private void initComponents()
    {
        ButtonGroup settingsOptions = new ButtonGroup();
        settingsOptions.add(sample);
        settingsOptions.add(obstacle);       
        settingsOptions.add(motherShip);
        
        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem run  = new JMenuItem("Run");
        
        JMenuItem exit   = new JMenuItem("Exit");
              
        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(motherShip);
        settings.add(obstacle);
        settings.add(sample);
            
        robot1 = new ImageIcon("imagenes/LEGO_batman.png");
        robot1 = new ImageIcon(robot1.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        robot2 = new ImageIcon("imagenes/LEGO_robin.png");
        robot2 = new ImageIcon(robot2.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        obstacleIcon = new ImageIcon("imagenes/greywall.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(70,70,  java.awt.Image.SCALE_SMOOTH));
        
        sampleIcon = new ImageIcon("imagenes/goon.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        motherIcon = new ImageIcon("imagenes/batmobile.png");
        motherIcon = new ImageIcon(motherIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        this.setLayout(null);
        formaPlano();  
        
        exit.addActionListener(evt -> gestionaSalir(evt));
        run.addActionListener(evt -> gestionaRun(evt));
        obstacle.addItemListener(evt -> gestionaObstacle(evt));
        sample.addItemListener(evt -> gestionaSample(evt));
        motherShip.addItemListener(evt -> gestionaMotherShip(evt)); 
            
        class MyWindowAdapter extends WindowAdapter
        {
            @Override
            public void windowClosing(WindowEvent eventObject)
            {
		        goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());
        
        // Crea 2 agentes
        //Random random = new Random();
        long seed1 = System.currentTimeMillis();
        long seed2 = 896670674;

        Random random1 = new Random(seed1);
        Random random2 = new Random(seed2);

        int A1I = random1.nextInt(dim);
        int A1J = random1.nextInt(dim);

        Batman = new Agente("Batman",robot1, motherIcon,matrix, tablero, A1I, A1J); 

        int A2I, A2J;
        do{
            A2I = random2.nextInt(dim);
            A2J = random2.nextInt(dim);
        }while(A1I == A2I && A1J == A2J);

        Robin = new Agente("Robin",robot2, motherIcon, matrix, tablero, A2I, A2J); 
        
    }
        
    private void gestionaSalir(ActionEvent eventObject)
    {
        goodBye();
    }
        
    private void goodBye()
    {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?","Aviso",JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.YES_OPTION) System.exit(0);
    }
  
    private void formaPlano()
    {
        tablero = new JLabel[dim][dim];
        matrix = new int[dim][dim];
        
        int i, j;
        
        for(i=0;i<dim;i++)
            for(j=0;j<dim;j++)
            {
                matrix[i][j]=0;
                tablero[i][j]=new JLabel();
                tablero[i][j].setBounds(j*50+10,i*50+10,50,50);
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false);
                this.add(tablero[i][j]);
                
                tablero[i][j].addMouseListener(new MouseAdapter() // Este listener nos ayuda a agregar poner objetos en la rejilla
                    {
                        @Override
                        public void mousePressed(MouseEvent e) 
                        {
                            insertaObjeto(e);
                        }   
                
                        @Override
                        public void mouseReleased(MouseEvent e) 
                        {
                            insertaObjeto(e);
                        }   
                
                    });
                                
            }
    }
        
    private void gestionaObstacle(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = obstacleIcon;
        else actualIcon = null;        
    }
    
    private void gestionaSample(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = sampleIcon;
        else actualIcon = null;   
    }
    
    private void gestionaMotherShip(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = motherIcon;
        else actualIcon = null;   
    }
    private void gestionaRun(ActionEvent eventObject)
    {
        if(!Batman.isAlive()) Batman.start();
        if(!Robin.isAlive()) Robin.start();
        settings.setEnabled(false);
    }
       
    public void insertaObjeto(MouseEvent e) {
        JLabel casilla = (JLabel) e.getSource();
        
        int fila = -1, columna = -1;
        
        // Encontrar la posición de la casilla en la matriz
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (tablero[i][j] == casilla) {
                    fila = i;
                    columna = j;
                    break;
                }
            }
            if (fila != -1) break; // Salir del bucle si ya encontramos la casilla
        }
        
        if (fila != -1 && columna != -1) {
            if (actualIcon == sampleIcon) {
                // Verificar si se puede formar un bloque 2x2 sin salir del tablero
                if (fila < dim - 1 && columna < dim - 1) {
                    // Colocar samples en las 4 casillas del bloque 2x2
                    matrix[fila][columna] = 2;
                    tablero[fila][columna].setIcon(sampleIcon);
    
                    matrix[fila][columna + 1] = 2;
                    tablero[fila][columna + 1].setIcon(sampleIcon);
    
                    matrix[fila + 1][columna] = 2;
                    tablero[fila + 1][columna].setIcon(sampleIcon);
    
                    matrix[fila + 1][columna + 1] = 2;
                    tablero[fila + 1][columna + 1].setIcon(sampleIcon);
                }
            } else if (actualIcon == obstacleIcon) {
                matrix[fila][columna] = 1;
                tablero[fila][columna].setIcon(obstacleIcon);
            } else if (actualIcon == motherIcon) {
                matrix[fila][columna] = 3;
                tablero[fila][columna].setIcon(motherIcon);
    
                // Guardar la posición de la nave nodriza
                Batman.setMothershipPosition(fila, columna);
                Robin.setMothershipPosition(fila, columna);
            }
        }
    }
    
    
}
