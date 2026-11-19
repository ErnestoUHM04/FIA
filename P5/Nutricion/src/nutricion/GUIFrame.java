package nutricion;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.management.Query;
import javax.swing.*;
import java.util.*;
import org.jpl7.*;

public class GUIFrame extends JFrame 
{
    private final JLabel[] desayuno;
    private final JLabel[] almuerzo;
    private final JLabel[] comida;
    private final JLabel[] merienda;
    private final JLabel[] cena;
    //private final JTextField gastoField;
    //private final JTextField sexField;
    private final JButton maleButton = new JButton("M");
    private final JButton femaleButton = new JButton("F");
    private String selectedSex = "";
    private final JTextField ageField;
    private final JTextField weightField;
    private final JTextField heightField;
    // so now that we have the sex, age, weight and height we can calculate the basal metabolic rate (BMR)
    private final JLabel gastoLabel = new JLabel("Gasto metabólico (kcal): ");

    private final JTextArea resultadosArea;
    private final JLabel jMenu = new JLabel();
    private final JButton firstButton;
    private final JButton prevButton;
    private final JButton nextButton;
    private final JButton lastButton;
    private List<MenuDieta> menus = new ArrayList<>();
    private int currentIndex = 0;

    public GUIFrame() {
        setTitle("Planificador de Nutrición del Dr. Simi Miku");
        setSize(1200, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        
        //sexField = new JTextField(1);
        ageField = new JTextField(3);
        heightField = new JTextField(4);
        weightField = new JTextField(4);

        JLabel imag = new JLabel();

        ImageIcon icono = new ImageIcon("img/dr_simi_miku.png");
        Image imagen = icono.getImage();
        int w =  imagen.getWidth(null);
        int h =  imagen.getHeight(null);       
        imagen = icono.getImage().getScaledInstance((int) (w/1.6), (int)(h/1.6), Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagen);
        imag.setIcon(iconoEscalado);
        
        imag.setBounds(520,50,imagen.getWidth(null),imagen.getHeight(null));
        this.add(imag);
        
        //JLabel label = new JLabel("Gasto metabólico (kcal): ");
        //gastoField = new JTextField(10);
        
        JLabel sexLabel = new JLabel("Sexo (M/F): ");
        //JTextField sexField = new JTextField(1);

        JLabel ageLabel = new JLabel("Edad (años): ");
        JTextField ageField = new JTextField(3);

        JLabel weightLabel = new JLabel("Peso (kg): ");
        JTextField weightField = new JTextField(4);

        JLabel heightLabel = new JLabel("Altura (cm): ");
        JTextField heightField = new JTextField(4);

        JButton buscarButton = new JButton("Buscar Menús");


        //label.setBounds(10,10,150,30);
        //gastoField.setBounds(150,10,100,30);
        //buscarButton.setBounds(250,10,150,30);

        sexLabel.setBounds(10, 10, 100, 30);
        //sexField.setBounds(110, 10, 50, 30);

        maleButton.setBounds(100, 10, 50, 30);
        femaleButton.setBounds(140, 10, 50, 30);

        ageLabel.setBounds(10, 50, 100, 30);
        ageField.setBounds(110, 50, 50, 30);

        heightLabel.setBounds(10, 90, 100, 30);
        heightField.setBounds(110, 90, 50, 30);

        weightLabel.setBounds(10, 130, 100, 30);
        weightField.setBounds(110, 130, 50, 30);

        buscarButton.setBounds(450, 10, 150, 30);

        gastoLabel.setBounds(10, 170, 200, 30);

        //this.add(label);
        //this.add(gastoField);

        this.add(sexLabel);
        //this.add(sexField);

        this.add(maleButton);
        this.add(femaleButton);

        this.add(ageLabel);
        this.add(ageField);
        this.add(heightLabel);
        this.add(heightField);
        this.add(weightLabel);
        this.add(weightField);
        this.add(buscarButton);

        this.add(gastoLabel);
        
        resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        resultadosArea.setLineWrap(true);
        resultadosArea.setWrapStyleWord(true);

        JScrollPane panelResultado = new JScrollPane(resultadosArea);
        
        JPanel breakfastPanel = new JPanel();
        JPanel brunchPanel = new JPanel();
        JPanel lunchPanel = new JPanel();
        JPanel snackPanel = new JPanel();
        JPanel dinnerPanel = new JPanel();
        
        breakfastPanel.setLayout(new GridLayout(1,4));
        brunchPanel.setLayout(new GridLayout(1, 2));
        lunchPanel.setLayout(new GridLayout(1,3));
        snackPanel.setLayout(new GridLayout(1,2));   
        dinnerPanel.setLayout(new GridLayout(1, 3));

        JLabel et1 = new JLabel("Desayuno");
        JLabel et2 = new JLabel("Almuerzo");
        JLabel et3 = new JLabel("Comida");
        JLabel et4 = new JLabel("Merienda");
        JLabel et5 = new JLabel("Cena");
                
        desayuno = new JLabel[4];
        almuerzo = new JLabel[2];
        comida = new JLabel[3];
        merienda = new JLabel[2];
        cena = new JLabel[3];
        
        for (int i=0;i<desayuno.length;i++) 
        {
            desayuno[i] = new JLabel();
            desayuno[i].setIcon(null);
            breakfastPanel.add(desayuno[i]);
        }
        
        for (int i = 0; i < almuerzo.length; i++) 
        {
            almuerzo[i] = new JLabel();
            almuerzo[i].setIcon(null);
            brunchPanel.add(almuerzo[i]); // Add to the brunchPanel
        }

        for (int i=0;i<comida.length;i++) 
        {
            comida[i] = new JLabel();
            comida[i].setIcon(null);
            lunchPanel.add(comida[i]);
        }
        
        for (int i=0;i<merienda.length;i++) 
        {
            merienda[i] = new JLabel();
            merienda[i].setIcon(null);
            snackPanel.add(merienda[i]);
        }

        for (int i = 0; i < cena.length; i++) {
            cena[i] = new JLabel();
            cena[i].setIcon(null);
            dinnerPanel.add(cena[i]);
        }
        
        firstButton = new JButton("First");
        prevButton = new JButton("Prev");
        nextButton = new JButton("Next");
        lastButton = new JButton("Last");
        
        jMenu.setBounds(300,10,200,20);
        jMenu.setFont(new Font("Consolas", Font.BOLD, 14));

        et1.setFont(new Font("Consolas", Font.BOLD, 14));
        et2.setFont(new Font("Consolas", Font.BOLD, 14));
        et3.setFont(new Font("Consolas", Font.BOLD, 14));
        et4.setFont(new Font("Consolas", Font.BOLD, 14));   
        et5.setFont(new Font("Consolas", Font.BOLD, 14));           

        panelResultado.setBounds(10,220,500,150);

        et1.setBounds(10, 390, 100, 20);
        et2.setBounds(10, 530, 100, 20);
        et3.setBounds(10, 670, 100, 20);
        et4.setBounds(420, 390, 100, 20);
        et5.setBounds(420, 530, 100, 20);

        breakfastPanel.setBounds(10,420,400,100);
        brunchPanel.setBounds(10,560,400,100);
        lunchPanel.setBounds(10,700,400,100);
        snackPanel.setBounds(420,420,400,100);
        dinnerPanel.setBounds(420,560,400,100);
                
        firstButton.setBounds(420, 750, 100, 30);
        prevButton.setBounds(540, 750, 100, 30);
        nextButton.setBounds(650, 750, 100, 30);
        lastButton.setBounds(760, 750, 100, 30);

        this.add(jMenu);
        this.add(panelResultado);
        this.add(et1);
        this.add(et2);
        this.add(et3);
        this.add(et4);
        this.add(et5);
        this.add(breakfastPanel);
        this.add(brunchPanel);
        this.add(lunchPanel);
        this.add(snackPanel);
        this.add(dinnerPanel);
        this.add(firstButton);
        this.add(prevButton);
        this.add(nextButton);
        this.add(lastButton);
        
        firstButton.addActionListener(e -> mostrarPrimero());
        prevButton.addActionListener(e -> mostrarAnterior());
        nextButton.addActionListener(e -> mostrarSiguiente());
        lastButton.addActionListener(e -> mostrarUltimo());

        maleButton.addActionListener(e -> {
            selectedSex = "M";
            maleButton.setEnabled(false);
            femaleButton.setEnabled(true);
            //sexField.setText(selectedSex);
        });

        femaleButton.addActionListener(e -> {
            selectedSex = "F";
            femaleButton.setEnabled(false);
            maleButton.setEnabled(true);
            //sexField.setText(selectedSex);
        });

        buscarButton.addActionListener(e -> buscarMenus(/*sexField*/ ageField, heightField, weightField));
    }

    private void buscarMenus(/*JTextField sexField, */ JTextField ageField, JTextField heightField, JTextField weightField) {
        resultadosArea.setText("");
        //String gastoTexto = gastoField.getText();

        try {
            int gasto = 0; //java.lang.Integer.parseInt(gastoTexto);
            float BMR = 0;

            //String sex = sexField.getText().trim().toUpperCase();
            int age = java.lang.Integer.parseInt(ageField.getText().trim());
            int height = java.lang.Integer.parseInt(heightField.getText().trim());
            int weight = java.lang.Integer.parseInt(weightField.getText().trim());

            if (!selectedSex.equals("M") && !selectedSex.equals("F")) {
                throw new IllegalArgumentException("Seleccione un sexo (M/F).");
            }

            String sex = selectedSex;

            if(age < 0 || height < 0 || weight < 0) {
                throw new IllegalArgumentException("Edad, altura y peso deben ser positivos.");
            }

            if(sex.equals("M")) { // if man
                BMR = (float) ((13.75 * weight) + (5.003 * height) - (6.75 * age) + 66.5);
            } else { // if woman
                BMR = (float) ((9.563 * weight) + (1.85 * height) - (4.676 * age) + 655.1);
            }

            // after calculating BMR we can calculate the TDEE (Total Daily Energy Expenditure)
            // TDEE = BMR * activity level
            // activity level = 1.2 (sedentary) to 1.9 (very active)

            // we will trim the float into a int and then use "gasto" for the prolog queries
            //gasto = (int) (BMR);
            gasto = Math.round(BMR);

            gastoLabel.setText("Gasto metabólico: " + gasto + " kcal");

            menus = obtenerMenusDesdeProlog(gasto);
            currentIndex = 0;
            if (menus.isEmpty()) {
                resultadosArea.append("No se encontraron menús que cumplan con el criterio.\n");
            } else {
                mostrarMenuActual();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMenuActual() {
        if (!menus.isEmpty() && currentIndex >= 0 && currentIndex < menus.size()) 
        {
            resultadosArea.setText(menus.get(currentIndex).toString());
            jMenu.setText("Menu: " + (currentIndex+1)+ " de " + menus.size() );
            MenuDieta m = menus.get(currentIndex);

            List<String> mDesayuno = m.getDesayuno();
            List<String> mAlmuerzo = m.getAlmuerzo();
            List<String> mComida = m.getComida();
            List<String> mMerienda = m.getMerienda();
            List<String> mCena = m.getCena();
            
            for (JLabel food : desayuno) food.setIcon(null);
            for (JLabel food : almuerzo) food.setIcon(null);
            for (JLabel food : comida) food.setIcon(null);
            for (JLabel food : merienda) food.setIcon(null);
            for (JLabel food : cena) food.setIcon(null);
 
            setIcons(mDesayuno, desayuno);
            setIcons(mAlmuerzo, almuerzo);
            setIcons(mComida, comida);
            setIcons(mMerienda, merienda);
            setIcons(mCena, cena);
        }
    }

    private void setIcons(List<String> alimentos, JLabel[] labels) {
        for (int i = 0; i < alimentos.size(); i++) {
            ImageIcon icon = new ImageIcon("img/" + alimentos.get(i) + ".jpg");
            Image imagen = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            labels[i].setIcon(new ImageIcon(imagen));
        }
    }

    private void mostrarPrimero() {
        currentIndex = 0;
        mostrarMenuActual();
    }

    private void mostrarAnterior() {
        if (currentIndex > 0) {
            currentIndex--;
            mostrarMenuActual();
        }
    }

    private void mostrarSiguiente() {
        if (currentIndex < menus.size() - 1) {
            currentIndex++;
            mostrarMenuActual();
        }
    }

    private void mostrarUltimo() {
        currentIndex = menus.size() - 1;
        mostrarMenuActual();
    }

    private List<MenuDieta> obtenerMenusDesdeProlog(int gasto) {
        List<MenuDieta> resultados = new ArrayList<>();
        try {
            org.jpl7.Query cargar = new org.jpl7.Query("consult('knowledge.pl')");
            if (!cargar.hasSolution()) {
                System.err.println("Error: Prolog file 'knowledge.pl' could not be loaded.");
                return resultados;
            }

            String consulta = "dietas(" + gasto + ", Menus)";
            org.jpl7.Query q = new org.jpl7.Query(consulta);

            if (q.hasSolution()) {
                Map<String, Term> sol = q.oneSolution();
                Term menusTerm = sol.get("Menus");

                System.out.println("Prolog query result: " + menusTerm);
                /* 
                
                */
                if (menusTerm.isList()) {
                    Term[] menusArray = menusTerm.listToTermArray();
                    System.out.println("Number of menus: " + menusArray.length);
                    for (Term menu : menusArray) {
                        System.out.println("Menu: " + menu);
                    }
                } else {
                    System.err.println("Error: Prolog query did not return a proper list.");
                }

                if (menusTerm.isList()) {
                    Term[] menusArray = menusTerm.listToTermArray();
                    for (Term menu : menusArray) {
                        try {
                            List<String> mDesayuno = convertirTermLista(menu.arg(1).listToTermArray());
                            List<String> mAlmuerzo = convertirTermLista(menu.arg(2).listToTermArray());
                            List<String> mComida = convertirTermLista(menu.arg(3).listToTermArray());
                            List<String> mMerienda = convertirTermLista(menu.arg(4).listToTermArray());
                            List<String> mCena = convertirTermLista(menu.arg(5).listToTermArray());
                            int kcal = menu.arg(6).intValue();
                
                            resultados.add(new MenuDieta(mDesayuno, mAlmuerzo, mComida, mMerienda, mCena, kcal));
                        } catch (Exception e) {
                            System.err.println("Error processing menu: " + menu);
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.err.println("Error: Prolog query did not return a proper list.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultados;
    }

    private List<String> convertirTermLista(Term[] terminos) {
        List<String> lista = new ArrayList<>();
        for (Term t : terminos) {
            lista.add(t.name());
        }
        return lista;
    }
}