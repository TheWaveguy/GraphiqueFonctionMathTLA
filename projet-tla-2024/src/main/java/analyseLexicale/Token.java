package analyseLexicale;

/**
 * Classe qui implémente la représentation d'un token dans l'application
 */
public class Token {
	
	private TypeDeToken typeDeToken;
	private String valeur;
	/**
	 * position dans la chaine de caractères de la fin du token
	 * Exemple : dans la chaine "abs( x )", le token abs a comme posFin 3 et le token ( a comme posFin 4
	 * Sert à la gestion de la position des erreurs
	 */
	private int posFin;

	public Token(TypeDeToken typeDeToken, String value, int pos) {
		this.typeDeToken=typeDeToken;
		this.valeur=value;
		this.posFin = pos;
	}

	public Token(TypeDeToken typeDeToken, int pos) {
		this.typeDeToken=typeDeToken;
		this.posFin = pos;
	}

	public TypeDeToken getTypeDeToken() {
		return typeDeToken;
	}

	public String getValeur() {
		return valeur;
	}
	
	/**
	 * Retourne la valeur de la position de la fin du token dans la chaine de caractères
	 * @return int - posFin du token
	 */
	public int getPosFin() {
		return posFin;
	}
	
	/**
	 * Retourne une chaine de caractère correspondant à la description du Token
	 * @return String
	 */
	public String toString() {
		String res = typeDeToken.toString() + " - pos = " + this.posFin;
		if (valeur != null) res = res + "(" + valeur + ")";
		return res;
	}

}
