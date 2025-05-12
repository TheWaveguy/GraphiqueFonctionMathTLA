package main;

/**
 * Exception levée quand le tableau tabRacinesFonction est plein
 * S'occupe de modifier le JLabel encartErreur de la classe Main pour notifier l'erreur
 */
public class tabFonctionFull extends Exception {
	public tabFonctionFull() {
		super("Maximum de fonctions affichables atteinte");
		Main.setEncartErreur("Maximum de fonctions affichables atteinte");
	}
}

