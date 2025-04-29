package arbreSyntaxique;

/**
 * Classe implémentant les méthodes et attributs permettant "d'éxecuter" notre fonction 
 */
public class Interpretation {
	/**
	 *  stocke les variables lues au clavier durant l'interprétation
	 */
	private Double variables;

	public Interpretation(Double var) {
		variables = var;
	}

	/**
	 * interprete le noeud n
	 * et appel récursif sur les noeuds enfants de n
	 *
	 * retourne
	 * null si le noeud est une instruction (kPrint ou kInput)
	 * la valeur de l'expression si le noeud est une expression
	 * @param n - Noeud - noeud à interpreter
	 * @return Double - résultat du calcul en utilisant la fonction
	 */
	public Double interpreter(Noeud n) {

		switch(n.getTypeDeNoeud()) {
			case ident:
				/* retourne la valeur d'une variable (désignée par son identifiant) */
				return variables;
			case intv:
				/* retourne la valeur d'entier litéral */
				return Double.valueOf(n.getValeur());
			case floatv:
				/* retourne la valeur d'un flottant litéral */
				return Double.valueOf(n.getValeur()).doubleValue();
			case add:
				/* retourne la somme entre le 1e fils du noeud et le 2e */
				return interpreter(n.enfant(0)) + interpreter(n.enfant(1));
			case multiply:
				/* retourne le produit entre le 1e fils du noeud et le 2e */
				return interpreter(n.enfant(0)) * interpreter(n.enfant(1));
			case substract:
				/* retourne la soustraction entre le 1e fils du noeud et le 2e */
				return interpreter(n.enfant(0)) - interpreter(n.enfant(1));
			case divide:
				/* retourne la division entre le 1e fils du noeud et le 2e */
				return interpreter(n.enfant(0)) / interpreter(n.enfant(1));
			case kFunctionPow: {
				/* retourne la puissance avec pour facteur le 1e fils du noeud et pour exposant le 2e fils */
				double d = Math.pow(interpreter(n.enfant(0)), interpreter(n.enfant(1)));
				return Double.valueOf(d).doubleValue();
			}
			case kSymbolPow:{
				/* retourne la puissance avec pour facteur le 1e fils du noeud et pour exposant le 2e fils */
				double d =Math.pow(interpreter(n.enfant(0)), interpreter(n.enfant(1)));
				return Double.valueOf(d).doubleValue();
			}
			case kAbs:
				/* retourne la valeur absolue */
				return Math.abs(Double.valueOf(interpreter(n.enfant(0))));
			case kCos:
				/* retourne le cosinus */
				return Math.cos(Double.valueOf(interpreter(n.enfant(0))));
			case kSin:
				/* retourne le sinus */
				return Math.sin(Double.valueOf(interpreter(n.enfant(0))));
			case kTan:
				/* retourne la tangente */
				return Math.tan(Double.valueOf(interpreter(n.enfant(0))));
			case kExp:
				/* retourne l'exponentiel */
				return Math.exp(Double.valueOf(interpreter(n.enfant(0))));
			case negative:
				return (-1.0) * Double.valueOf(interpreter(n.enfant(0)));
		}
		return null;
	}

}
