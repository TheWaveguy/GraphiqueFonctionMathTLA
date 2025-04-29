package analyseSyntaxique;

import main.Main;

/**
 *  l'analyse syntaxique s'est terminée sans avoir examinée tous les tokens en entrée
 *  S'occupe de modifier le JLabel encartErreur de la classe Main pour notifier l'erreur
 */
public class IncompleteParsingException extends Exception {
	public IncompleteParsingException(String message) {
		super("Incomplete Parsing : " + message);
		Main.setEncartErreur("Illegal Character : " + message);
	}}
