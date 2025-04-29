
package analyseLexicale;

import main.Main;

/**
 *  l'analyse lexicale a détectée, en entrée, un symbole inconnu (que l'analyse syntaxique n'accepte pas) 
 *  S'occupe de modifier le JLabel encartErreur de la classe Main pour notifier l'erreur
 */
public class IllegalCharacterException extends Exception {
	public IllegalCharacterException(String message) {
		super(message);
		Main.setEncartErreur("Illegal Character : " + message);
	}
}
