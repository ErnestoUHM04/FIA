package parasitos;

/*
 * @author Ernesto Ulises Hernandez Martinez
 * ErnestoUlisesHM
 * */

import java.util.HashMap;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import org.jpl7.Query;

public class GUI extends JFrame
{
    private Query q1;
    private String[] species;
    private JList speciesList;
    private final Map<String, String> displayToAtom = new HashMap<>();
    private final DefaultListModel listModel1 = new DefaultListModel();
    private final JTextArea taxonomyArea = new JTextArea();
    JLabel imagen = new JLabel();
    //private final JLabel imageLabel = new JLabel();
    private final JLabel imageLabelTab1 = new JLabel();
    private final JLabel imageLabelTab2 = new JLabel();
    private final JLabel imageLabelTab3 = new JLabel();

    public GUI()
    {
        super("Parasitos");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int width = pantalla.width;
        int height = pantalla.height;
        this.setBounds((width-1065)/2,(height-600)/2,1065,610);
        initComponents();
    }

    private void initComponents()
    {
        JTabbedPane tabPan = new JTabbedPane();
        tabPan.setBounds(0,0,855,580);
        this.add(tabPan);
        JPanel tab01 = new JPanel();
        JPanel tab02 = new JPanel();
        JPanel tab03 = new JPanel();
        //JPanel tab03 = new JPanel();

        tabPan.add("Especies",tab01);
        tabPan.add("Taxonomía de especie", tab02);
        tabPan.add("Descripción", tab03);
        //tabPan.add("Árbol taxonómico", tab03);

        cargaConocimiento("engine.pl");
        cargaConocimiento("knowledge.pl");

        // Panel de Especies
        species = consultaEspecies();
        for (String specie : species){
            //listModel1.addElement(specie.trim());
            String trimmed = specie.trim();
            String display = formatName(trimmed);
            listModel1.addElement(display);
            displayToAtom.put(display, trimmed); // Mapeo de nombre formateado a átomo
        }
        speciesList = new JList(listModel1);

        JLabel et01 = new JLabel("Especies:");
        JScrollPane spSpecies = new JScrollPane(speciesList);

        tab01.setLayout(null);
        et01.setBounds(10,10,400,20);
        et01.setFont(new Font("Arial",Font.BOLD,16));

        spSpecies.setBounds(10,30,400,500);
        speciesList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        speciesList.setFont(new Font("Consolas",Font.BOLD,18));

        tab01.add(et01);
        tab01.add(spSpecies);

        imageLabelTab1.setBounds(620, 30, 300, 300);
        tab01.add(imageLabelTab1);

        // Panel de Taxonomía
        tab02.setLayout(null);
        JLabel et02 = new JLabel("Taxonomía completa de la especie seleccionada:");
        et02.setBounds(10,10,500,20);
        et02.setFont(new Font("Arial",Font.BOLD,16));

        taxonomyArea.setEditable(false);
        taxonomyArea.setFont(new Font("Arial",Font.PLAIN,16));
        JScrollPane spTaxonomy = new JScrollPane(taxonomyArea);
        spTaxonomy.setBounds(10,40,800,500);

        tab02.add(et02);
        tab02.add(spTaxonomy);

        imageLabelTab2.setBounds(820, 40, 200, 200);
        tab02.add(imageLabelTab2);

        // Layout for tab03
        tab03.setLayout(null);

        JTextArea descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane spDesc = new JScrollPane(descArea);
        spDesc.setBounds(10, 40, 600, 500);
        tab03.add(spDesc);

        JLabel imageLabelTab3 = new JLabel();
        imageLabelTab3.setBounds(650, 40, 300, 300);
        tab03.add(imageLabelTab3);

        // Listener para mostrar taxonomía al seleccionar especie
        speciesList.addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                String displayName = (String) speciesList.getSelectedValue();
                String prologAtom = displayToAtom.get(displayName);

                String imagePath = "/img/" + prologAtom + ".png";
                java.net.URL imgURL = getClass().getResource(imagePath);
                //ImageIcon icon = null;
                ImageIcon iconBig = null;
                ImageIcon iconSmall = null;

                String descripcion = consultaDescripcion(prologAtom);
                descArea.setText(descripcion);

                if (imgURL != null) {
                    //icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
                    //imageLabel.setIcon(new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)));
                    iconBig = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH));
                    iconSmall = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
                }
                imageLabelTab1.setIcon(iconBig);
                imageLabelTab2.setIcon(iconSmall);
                imageLabelTab3.setIcon(iconBig);

                String[] taxonomy = consultaTaxonomia(prologAtom);
                StringBuilder sb = new StringBuilder();
                sb.append(displayName).append(":\n\n");
                for (String t : taxonomy) {
                    String tipo = consultaTipo(t.trim());
                    sb.append(" - ")
                      .append(formatType(tipo))
                      .append(": ")
                      .append(formatName(t.trim()))
                      .append("\n");
                }
                taxonomyArea.setText(sb.toString());
                //tabPan.setSelectedIndex(1); // Cambia a la pestaña de taxonomía
            }
        });


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

    private String[] consultaEspecies()
    {
        String consulta = "todas_especies(L)";
        String[] resultado = null;
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
            String respuesta = "";
            Map soluciones = q1.nextSolution();
            respuesta+=soluciones.get("L");
            resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;
    }

    private String[] consultaTaxonomia(String especie)
    {
        String consulta = "taxonomia_completa(" + especie + ", L)";
        String[] resultado = null;
        q1 = new Query(consulta);
        if(q1.hasSolution())
        {
            String respuesta = "";
            Map soluciones = q1.nextSolution();
            respuesta+=soluciones.get("L");
            resultado = respuesta.substring(respuesta.indexOf("[")+1, respuesta.indexOf("]")).split(",");
        }
        return resultado;
    }

    private String consultaTipo(String nombre) {
        String consulta = "frame(" + nombre + ", _, Tipo)";
        q1 = new Query(consulta);
        if (q1.hasSolution()) {
            Map soluciones = q1.nextSolution();
            return soluciones.get("Tipo").toString();
        }
        return "";
    }

    private String consultaDescripcion(String especie) {
        String consulta = "descripcion_especie(" + especie + ", Desc)";
        q1 = new Query(consulta);
        if (q1.hasSolution()) {
            Map soluciones = q1.nextSolution();
            String desc = soluciones.get("Desc").toString();
            if (desc.startsWith("'") && desc.endsWith("'") && desc.length() > 1) {
                desc = desc.substring(1, desc.length() - 1);
            }
            return desc;
            
        }
        return "Sin descripción.";
    }

    private String formatName(String raw) {
        String withSpaces = raw.replace('_', ' ');
        if (withSpaces.isEmpty()) return withSpaces;
        return withSpaces.substring(0, 1).toUpperCase() + withSpaces.substring(1).toLowerCase();
    }

    private String formatType(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        return raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
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
}
