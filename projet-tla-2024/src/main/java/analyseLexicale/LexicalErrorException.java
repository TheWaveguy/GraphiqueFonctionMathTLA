package analyseLexicale;

import main.Main;

/**
 * il n'y a plus de transitions possibles depuis le caractère en cours de lecture et l'état en cours
 * S'occupe de modifier le JLabel encartErreur de la classe Main pour notifier l'erreur
 */
public class LexicalErrorException extends Exception {
	public LexicalErrorException(String message) {
		super(message);
		Main.setEncartErreur("Lexical Error : " + message);
	}
}
