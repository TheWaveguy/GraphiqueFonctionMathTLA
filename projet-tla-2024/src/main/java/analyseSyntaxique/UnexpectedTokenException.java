/**
 * @author Forest Jules
 * @author Haton Tom
 */

package analyseSyntaxique;

import main.Main;

/**
 * L'analyse syntaxique rencontre un token qui n'est pas attendu à ce stade de l'analyse
 * S'occupe de modifier le JLabel encartErreur de la classe Main pour notifier l'erreur
 */
public class UnexpectedTokenException extends Exception {
	public UnexpectedTokenException(String message) {
		super("Unexpected Token : " + message);
		Main.setEncartErreur("Illegal Character : " + message);
	}
}
