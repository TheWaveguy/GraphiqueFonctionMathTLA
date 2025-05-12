package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import analyseLexicale.AnalyseLexicale;
import analyseLexicale.Token;
import analyseSyntaxique.AnalyseSyntaxique;
import arbreSyntaxique.Noeud;
import arbreSyntaxique.TypeDeNoeud;
import arbreSyntaxique.Interpretation;


/**
 * Plot : calcul du tracé de différentes fonctions en dur, sur un intervalle
 */
public class Plot {
		
    /**
     * Nombre de point calculés dans l'intervalle
     */
    final static double STEPS = 4000;

    /**
     * le calcul des valeurs de la fonction se fait sur l'intervalle [-range...range]
     */
    double range = 2;


    /**
     * point d'entrée d'affectation du range par l'IHM
     */
    void setRange(double range) {
        this.range = range;
    }
    
    public String fonction="0";
    
    /**
     * tableau contenant les racines des fonctions que l'on affiche (évite de refaire analyse syntaxique et lexical à chaque fois)
     */
    public Noeud tabRacinesFonction[] = new Noeud[5]; 
    
    /**
     * tableau des couleurs de courbes de fonction
     */
    public Color tabCouleur[] = {Color.BLACK, Color.MAGENTA, Color.ORANGE, Color.GREEN, Color.CYAN};
    
    /**
     * indice de remplissage de tabRacinesFonction (permet des enregistrements en temps constant et plus simples)
     */
    public int remplissageTableau = 0;
    
    
    /**
     * Permet de vider le tableau tabRacinesFonction, c'est à dire de supprimer la sauvegarde des fonctions affichées
     */
    public void delFonction() {
    	remplissageTableau = 0;
		Main.setEncartErreur("");
    	tabRacinesFonction = new Noeud[5]; 
    	
//    	//Vérifications de la suppression dans le tableau
//    	int cpt=0;
//    	while(cpt < tabRacinesFonction.length) {
//    		if(tabRacinesFonction[cpt] != null) {
//    			System.out.println("fonction " + cpt + " : " + tabRacinesFonction[cpt].toString());
//    			cpt++;
//    		}
//    		else {
//    			break;
//    		}
//    	}
    }
    
    /**
     * S'occupe de l'analyse lexicale et syntaxique de la chaine de caractères en paramètres qui est censé représenter une fonction.
     * Puis, si la fonction est acceptée, va l'ajouter au tableau tabRacinesFonction dans afficher la courbe correspondante
     * 
     * @param entree - String - la chaine de caractères de la fonction que l'on veut ajouter
     * @throws tabFonctionFull - renvoyé si le tableau tabRacinesFonction est plein, càd impossible de rajouter des fonctions à l'affichage
     */
    public void ajoutFonction(String entree) throws tabFonctionFull {
    	if(remplissageTableau < tabRacinesFonction.length) {
			List<Token> tokens;
			Noeud racine = null;
			try {
				tokens = new AnalyseLexicale().analyse(entree); // analyse lexicale de la chaine de caractères
				for(Token t : tokens) {
					System.out.println(t.toString());
				}
				racine = new AnalyseSyntaxique().analyse(tokens); // analyse syntaxique
				Main.setEncartErreur("Fonction acceptée"); //modif de l'encart d'échange avec l'utilisateur pour lui signifier que la fonction est acceptée
				//ajout de la racine à tabRacinesFonction
	    		tabRacinesFonction[remplissageTableau] = racine;
	    		remplissageTableau++;
				//Noeud.afficheNoeud(racine, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else {
    		throw new tabFonctionFull();
    	}
    	
//    	//Vérifications de l'ajout au tableau
//    	System.out.println("Ajout de la fonction au tableau : " + ajout);
//    	int cpt=0;
//    	while(cpt < tabFonction.length) {
//    		if(tabFonction[cpt] != null) {
//    			System.out.println("fonction " + cpt + " : " + tabFonction[cpt].toString());
//    			cpt++;
//    		}
//    		else {
//    			break;
//    		}
//    	}
    }

    /**
     * Méthode appelée par PlotPanel pour effectuer le tracé du graphique,
     * selon le range et la fonction selectionnée
     * @param g objet permettant de dessiner sur un JPanel
     * @param w largeur du JPanel
     * @param h hauteur du JPanel
     */
    void paint(Graphics2D g, double w, double h) {

        double step = range / STEPS;

        double centerX = w / 2;
        double centerY = h / 2;

        double halfMinSize = Math.max(w, h) / 2;

        // affiche le repère
        g.setColor(Color.GRAY);
        g.drawLine((int)centerX, 0, (int)centerX, (int)h);
        g.drawLine(0, (int)centerY, (int)w, (int)centerY);

		for(int i=0; i < tabRacinesFonction.length && tabRacinesFonction[i] != null; i++) {
			// affiche différents points représentant la fonction sélectionnée
	        g.setColor(tabCouleur[i]);

		    // calcul des points de la courbe de chaque fonction et affichage
		    for (double x = -range; x<= range; x += step) {
		    	double y=0;
		
		        y = new Interpretation(x).interpreter(tabRacinesFonction[i]);
		        /*
		        Affichage du point de coordonnées (x,y), coordonnées ajustées à la dimension
		        de la zone d'affichage du tracé
		        */
		        if (Double.isFinite(y)) {
		            g.drawRect(
		                (int) (centerX + x * halfMinSize / range),
		                (int) (centerY + -y * halfMinSize / range),
		                1,
		                1
		            );
		        }
		    }
		}
    }
}
