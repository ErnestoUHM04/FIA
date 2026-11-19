/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musica;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import org.jpl7.Query;

/**
 *
 * @author 
 */
public class Gui extends JFrame 
{
    private Query q1, q2;
    private String[] clases;
    private JList listaClases;
    private final DefaultListModel listModel1 = new DefaultListModel();
    private final JTextArea displayArea = new JTextArea();
    private final JButton imagen = new JButton();
    private Clip clip;
    private boolean playing=false;
    
    
    public Gui()
    {
        super("Metal");  
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int width = pantalla.width;
        int height = pantalla.height;
        this.setBounds((width-855)/2,(height-580)/2,855,580);
        initComponents();
    }
    
    private void initComponents()
    {
        JLabel et01 = new JLabel("Géneros:");
        JLabel et02 = new JLabel("Propiedades:");
        
        cargaConocimiento("engine.pl");
        cargaConocimiento("knowledge.pl");
        
        clases = consultaClases();
        for(int i=0;i<clases.length;i++)
            listModel1.addElement((clases[i]).trim());
        listaClases = new JList(listModel1);
        
        // Propiedades del JFrame
        
        
        this.setLayout(null);
        et01.setBounds(10,10,300,20);
        et02.setBounds(10,250,300,20);
        et01.setFont(new Font("Arial",Font.BOLD,16));
        et02.setFont(new Font("Arial",Font.BOLD,16));        
        listaClases.setBounds(10,30,300,200);
        listaClases.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        listaClases.setFont(new Font("Consolas",Font.BOLD,18));
        displayArea.setBounds(10,270,300,250);
        displayArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        displayArea.setFont(new Font("Arial",Font.PLAIN,16));
        imagen.setBounds(330,30,500,500);
        imagen.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
               
        this.add(et01);
        this.add(et02);
        this.add(listaClases);
        this.add(displayArea);
        this.add(imagen);
        
        listaClases.addListSelectionListener(evt -> gestionaClases(evt));        
        listaClases.setSelectedIndex(0);
        imagen.addActionListener(
                (ActionEvent e)-> {pause();});
        
        // Handle the X-Button 
        class MyWindowAdapter extends WindowAdapter
        {
            @Override
            public void windowClosing(WindowEvent eventObject)
            {
		goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());  
        
    }
    
    private void gestionaClases(ListSelectionEvent ev)
    {
        String selectedValue = (String) listaClases.getSelectedValue();
        String t = "";
   
        if(!selectedValue.trim().equals("top"))
        {
            String[] superclases = consultaSuperclases(selectedValue);
            for(int i=0;i<superclases.length;i++)
                if(!superclases[i].trim().equals("top"))
                    t+="descendiente del "+superclases[i].trim()+"\n";
        }
        
        String[] propiedades = consultaPropiedades(selectedValue);
        for(int i=0;i<propiedades.length;i++) 
            t+=propiedades[i].trim()+"\n";
  
        displayArea.setText(t);
        imagen.setIcon(new ImageIcon("imagenes/"+selectedValue + ".jpg"));
        if(clip!=null) {clip.stop(); playing=false;}
        playAudio(selectedValue);
        clip.start();playing=true;
    }
    
    private String[] consultaClases()
    {
        String consulta = "clases(L)";
        String[] resultado = null;
        q2 = new Query(consulta);
        if(q2.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q2.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;
    }
   
    private String[] consultaPropiedades(String obj)
    {
        String consulta = "propiedadesc("+ obj+ ", L)";
        //System.out.println(consulta);
        String[] resultado = null;
        q2 = new Query(consulta);
        if(q2.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q2.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;
        
    }
    
    private String[] consultaSuperclases(String obj)
    {
        String consulta = "superclases_de("+ obj+ ", L)";
        //System.out.println(consulta);
        String[] resultado = null;
        q2 = new Query(consulta);
        if(q2.hasSolution())
        {
           String respuesta = "";
           Map soluciones = q2.nextSolution();
           respuesta+=soluciones.get("L");
           resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;
        
    }
        
    private void cargaConocimiento(String archivo)
    {
        String consulta = "consult('" + "prolog/" + archivo + "')";
        q1 = new Query(consulta); // Se general el Query con el String de consulta
        System.out.println( consulta + " " + (q1.hasSolution() ? "succeeded" : "failed") ); // se realiza la consulta               
    }
    
    private void goodBye()
    {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Are you sure?","Exit",JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.YES_OPTION) System.exit(0);
    }
    
    private void playAudio(String audioFile)
    {
               try
        {    
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("audio/"+audioFile+".au"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.out);
        } 
    }
    
    private void pause()
    {
        if(playing)
        {
            clip.stop();
            playing=false;
        }
        else
        {
            clip.start();
            playing=true;
        }
    }
}
