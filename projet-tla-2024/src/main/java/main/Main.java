package main;

import java.awt.*;
import javax.swing.*;

public class Main {

    Plot plot;
    static JLabel encartErreur;

    /**
     * dimension souhaitée de la zone d'affichage du graphique
     */
    final static int PREF_HEIGHT = 300;
    final static int PREF_WIDTH = 400;

    /**
     * le slider retournant une valeur entière, il est nécessaire de le diviser
     * pour ajuster la propriété range de l'objet plot avec une meilleure précision<br/>
     * range = valeur du slider / RANGE_ADJUST
     */
    final static double RANGE_ADJUST = 10;

    public static void main(String[] args) {
        Main main = new Main();
        SwingUtilities.invokeLater(main::init);
    }

    public void init() {

        plot = new Plot();

        // fenêtre principale
        JFrame frame = new JFrame("Projet TLA 2024");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // zone de tracé
        PlotPanel widgetTrace = new PlotPanel(plot);
        widgetTrace.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
        frame.add(widgetTrace, BorderLayout.CENTER);

        // panneau de contrôle
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField jtf = new JTextField(50);
        topPanel.add(jtf);
        
        JButton btnOk = new JButton("Ok");
        topPanel.add(btnOk);
        
        /**
         * Bouton qui permet de reset l'affichage de toutes les fonctions
         */
        JButton reset = new JButton("reset");
        topPanel.add(reset);
        
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 200, (int)(plot.range*RANGE_ADJUST));
        slider.setPaintLabels(true);
        topPanel.add(slider);
        
        encartErreur = new JLabel("");
        topPanel.add(encartErreur);

        frame.add(topPanel, BorderLayout.NORTH);
        
        
        // ------------------------ ACTION LISTENERS ------------------------
        
        jtf.addActionListener(event -> {
        	String fonction = jtf.getText();
        	try {
				plot.ajoutFonction(fonction);
			} catch (tabFonctionFull e) {e.printStackTrace();}
        	widgetTrace.repaint();
        });

        btnOk.addActionListener(event -> {
        	String fonction = jtf.getText();
        	try {
				plot.ajoutFonction(fonction);
			} catch (tabFonctionFull e) {e.printStackTrace();}
            widgetTrace.repaint();
        });
        
        reset.addActionListener(event -> {
        	plot.delFonction();
        	widgetTrace.remove(widgetTrace);
        	widgetTrace.revalidate();
        	widgetTrace.repaint();
        });

        slider.addChangeListener(event -> {
            plot.setRange((double)slider.getValue()/RANGE_ADJUST);
            widgetTrace.repaint();
        });

        // rend visible la fenêtre principale
        frame.pack();
        frame.setVisible(true);
    }
    
    
    /**
     * Méthode appelé pour modifier le texte du JLabel encartErreur
     * @param message - texte à écrire dans le JLabel
     */
    public static void setEncartErreur(String message) {
    	encartErreur.setText(message);
    }
}