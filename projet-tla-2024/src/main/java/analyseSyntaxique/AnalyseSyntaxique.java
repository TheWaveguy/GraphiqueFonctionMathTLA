
package analyseSyntaxique;

import java.util.List;

import analyseLexicale.Token;
import analyseLexicale.TypeDeToken;
import arbreSyntaxique.Noeud;
import arbreSyntaxique.TypeDeNoeud;

/**
 * Classe implémentant les méthodes et attributs afin de réaliser une analyse syntaxique, notamment à partir d'une liste de tokens
 */
public class AnalyseSyntaxique {

	private int pos;
	private List<Token> tokens;

	/**
	 * effectue l'analyse syntaxique à partir de la liste de tokens
	 * et retourne le noeud racine de l'arbre syntaxique abstrait
	 * @param tokens - liste des tokens que l'on va analyser
	 * @return Noeud - le noeud racine correspondant à l'arbre abstrait de la fonction
	 * @throws Exception - incompleteParsingException, tout les tokens n'ont pas pu être examinés
	 */
	public Noeud analyse(List<Token> tokens) throws Exception {
		pos = 0;
		this.tokens = tokens;
		Noeud expr = S();
		if (pos != tokens.size()) {
			throw new IncompleteParsingException("L'analyse syntaxique s'est terminé avant l'examen de tous les tokens à la position : " + tokens.get(pos).getPosFin());
		}
		return expr;
	}

	
	
	/**
	 * Traite la dérivation du symbole non-terminal S
	 * @return Noeud
	 * @throws UnexpectedTokenException - le token qui suit n'est pas celui attendu
	 */
	private Noeud S() throws UnexpectedTokenException {
		
		// production S -> Z S'
		if (getTypeDeToken() == TypeDeToken.intv ||
				getTypeDeToken() == TypeDeToken.floatv ||
				getTypeDeToken() == TypeDeToken.ident ||
				getTypeDeToken() == TypeDeToken.leftPar ||
				getTypeDeToken() == TypeDeToken.kFunctionPow ||
				getTypeDeToken() == TypeDeToken.kAbs ||
				getTypeDeToken() == TypeDeToken.kCos ||
				getTypeDeToken() == TypeDeToken.kSin ||
				getTypeDeToken() == TypeDeToken.kTan ||
				getTypeDeToken() == TypeDeToken.kExp ||
				getTypeDeToken() == TypeDeToken.negative) {


			Noeud z = Z();
			return S_prime(z);
		}
		// gestion de l'erreur de token et de sa position
		// cas ou le token problématique est au tout début de la chaîne
		if(pos == 0) {
			throw new UnexpectedTokenException("intv ou floatv ou ident "
					+ "ou ( ou pow ou abs ou cos ou sin ou tan ou exp attendu à la position 1");
		} else {
			throw new UnexpectedTokenException("intv ou floatv ou ident "
					+ "ou ( ou pow ou abs ou cos ou sin ou tan ou exp attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
		}
	}

	
	
	/**
	 * Traite la dérivation du symbole non-terminal S'
	 * S' -> + S S' | - S S' | * S S' | / S S' | ^ S S' | epsilon
	 * @param i - Noeud
	 * @return Noeud
	 * @throws UnexpectedTokenException - le token qui suit n'est pas celui attendu
	 */
	private Noeud S_prime(Noeud i) throws UnexpectedTokenException {
		
		// production S' -> epsilon
		if (getTypeDeToken() == TypeDeToken.rightPar ||
				getTypeDeToken() == TypeDeToken.comma ||
				finAtteinte()) {

			return i;
		}
		
		// production S' -> + S S'
		if (getTypeDeToken() == TypeDeToken.add) {

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.add);
			n.ajout(i);
			n.ajout(S());
			return n;
		}
		
		// production S' -> - S S'
		if (getTypeDeToken() == TypeDeToken.substract) {

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.substract);
			n.ajout(i);
			n.ajout(S());
			return S_prime(n);
		}
		
		// production S' -> * S S'
		if (getTypeDeToken() == TypeDeToken.multiply) {

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.multiply);
			n.ajout(i);
			n.ajout(S());
			return S_prime(n);
		}
		
		// production S' -> / S S'
		if (getTypeDeToken() == TypeDeToken.divide) {

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.divide);
			n.ajout(i);
			n.ajout(S());
			return S_prime(n);
		}
		
		// production s' -> ^ S S'
		if (getTypeDeToken() == TypeDeToken.kSymbolPow) {

			Token t = lireToken();
			Noeud n = new Noeud(TypeDeNoeud.kSymbolPow);
			n.ajout(i);
			n.ajout(S());
			return S_prime(n);
		}
		
		throw new UnexpectedTokenException("+ ou - ou * ou / ou ^ ou ) ou \",\" attendu à la position " + (tokens.get(pos-1).getPosFin()));
	}

	
	
	/**
	 * Traite la dérivation du symbole non-terminal Z
	 * Z ->  intv | floatv | ident | ( S ) | pow (S,S) | abs(S) | cos(S) | sin(S) | tan(S) | exp(S)
	 * @return Noeud
	 * @throws UnexpectedTokenException - le token qui suit n'est pas celui attendu
	 */
	private Noeud Z() throws UnexpectedTokenException {
		
		// production Z -> ( S )
		if (getTypeDeToken() == TypeDeToken.leftPar) {

			lireToken();
			Noeud s = S();

			if (getTypeDeToken() == TypeDeToken.rightPar) {
				lireToken();
				return s;
			}
			throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
		}

		// production Z -> intv
		if (getTypeDeToken() == TypeDeToken.intv) {

			Token t = lireToken();
			return new Noeud(TypeDeNoeud.intv, t.getValeur());
		}
		
		// production Z -> floatv
		if (getTypeDeToken() == TypeDeToken.floatv) {

			Token t = lireToken();
			return new Noeud(TypeDeNoeud.floatv, t.getValeur());
		}

		// production Z -> ident
		if (getTypeDeToken() == TypeDeToken.ident) {

			Token t = lireToken();
			return new Noeud(TypeDeNoeud.ident, t.getValeur());
		}
		
		// production Z -> pow ( S , S )
		if (getTypeDeToken() == TypeDeToken.kFunctionPow) {

			Token oldToken = lireToken(); // récupère le Token précédent et avance
			Token token = lireToken(); // récupère le Token courant et passe au token suivant

			if (token == null || token.getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu à la position " + (oldToken.getPosFin()+1));
			}

			Noeud n = new Noeud(TypeDeNoeud.kFunctionPow);
			n.ajout(S());

			token = lireToken(); // avance au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.comma) {
				throw new UnexpectedTokenException(", attendu à la position " + tokens.get(pos-1).getPosFin());
			}

			n.ajout(S());
			token = lireToken(); // avance au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()));
			}

			return n;
		}
		
		// production Z -> abs ( S )
		if (getTypeDeToken() == TypeDeToken.kAbs) {

			Token oldToken = lireToken(); // récupère le Token précédent et avance
			Token token = lireToken(); // récupère le Token courant et passe au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu à la position " + (oldToken.getPosFin()+1));
			}

			Noeud n = new Noeud(TypeDeNoeud.kAbs);
			n.ajout(S());
			
			token = lireToken(); // avance au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
			}

			return n;
		}
		
		// production Z -> cos ( S )
		if (getTypeDeToken() == TypeDeToken.kCos) {
			
			Token oldToken = lireToken(); // récupère le Token précédent et avance
			Token token = lireToken(); // récupère le Token courant et passe au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu à la position " + (oldToken.getPosFin()+1));
			}

			Noeud n = new Noeud(TypeDeNoeud.kCos);
			n.ajout(S());
			
			token = lireToken(); // avance au token suivant

			if (token == null || token.getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
			}

			return n;
		}
		
		// production Z -> sin ( S )
		if (getTypeDeToken() == TypeDeToken.kSin) {

			Token oldToken = lireToken(); // récupère le Token précédent et avance
			Token token = lireToken(); // récupère le Token courant et passe au token suivant


			if (token == null || token.getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu à la position " + (oldToken.getPosFin()+1));
			}

			Noeud n = new Noeud(TypeDeNoeud.kSin);
			n.ajout(S());

			token = lireToken(); // avance au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
			}

			return n;
		}

		// production Z -> tan ( S )
		if (getTypeDeToken() == TypeDeToken.kTan) {

			Token oldToken = lireToken(); // récupère le Token précédent et avance
			Token token = lireToken(); // récupère le Token courant et passe au token suivant


			if (token == null || token.getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu à la position " + (oldToken.getPosFin()+1));
			}

			Noeud n = new Noeud(TypeDeNoeud.kTan);
			n.ajout(S());
			
			token = lireToken(); // avance au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
			}

			return n;
		}
		
		// production Z -> exp ( S )
		if (getTypeDeToken() == TypeDeToken.kExp) {

			Token oldToken = lireToken(); // récupère le Token précédent et avance
			Token token = lireToken(); // récupère le Token courant et passe au token suivant

			if(token == null || token.getTypeDeToken() != TypeDeToken.leftPar) {
				throw new UnexpectedTokenException("( attendu à la position " + (oldToken.getPosFin()+1));
			}

			Noeud n = new Noeud(TypeDeNoeud.kExp);
			n.ajout(S());
			
			token = lireToken(); // avance au token suivant
			
			if (token == null || token.getTypeDeToken() != TypeDeToken.rightPar) {
				throw new UnexpectedTokenException(") attendu à la position " + (tokens.get(pos-1).getPosFin()+1));
			}

			return n;
		}
		
		
		// production Z -> negative S
		if (getTypeDeToken() == TypeDeToken.negative) {

			Token token = lireToken(); // récupère le Token courant et passe au token suivant

			Noeud n = new Noeud(TypeDeNoeud.negative);
			n.ajout(S());
			
			return n;
		}
		
		throw new UnexpectedTokenException("intv, floatv, (, pow, abs, cos, sin, tan, exp ou ident attendu à la position " + (tokens.get(pos).getPosFin()+1));
	}


	// ----------------- méthodes utilitaires ------------------
	
	/**
	 * test si la fin du tableau des tokens est atteinte
	 * @return boolean - vrai si fin atteinte
	 */
	private boolean finAtteinte() {
		return pos >= tokens.size();
	}

	/**
	 * Retourne la classe du token en cours de lecture
	 * SANS AVANCER au token suivant
	 * @return TypeDeToken
	 */
	private TypeDeToken getTypeDeToken() {
		if (pos >= tokens.size()) {
			return null;
		} else {
			return tokens.get(pos).getTypeDeToken();
		}
	}

	/**
	 * Retourne le token en cours de lecture
	 * ET AVANCE au token suivant
	 * @return Token  
	 */
	private Token lireToken() {
		if (pos >= tokens.size()) {
			return null;
		} else {
			Token t = tokens.get(pos);
			pos++;
			return t;
		}
	}

}
