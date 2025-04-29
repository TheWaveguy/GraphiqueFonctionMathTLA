package analyseLexicale;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui implémente les méthodes et attributs afin de réaliser l'analyse lexicale d'une chaine de caractères
 */
public class AnalyseLexicale {

	/**
	 * Table de transition de l'analyse lexicale
	 */
	private static Integer TRANSITIONS[][] = {
			//            espace    +    *    (    )    ,  chiffre  lettre      .      ^      /      -
			/*  0 */    {      0, 101, 102, 103, 104, 105,       1,      2,   108,   109,   111,     4  },
			/*  1 */    {    106, 106, 106, 106, 106, 106,       1,    106,     3,   106,   106,   106  },
			/*  2 */    {    107, 107, 107, 107, 107, 107,       2,      2,   107,   107,   107,   107  },
			/*  3 */    {    110, 110, 110, 110, 110, 110,       3,    110,   110,   110,   110,   110  },
			/*  4 */    {    112, 113, 113, 113, 113, 113,     113,    113,   113,   113,   113,   113  },
			

			// 101 acceptation d'un +  
			// 102 acceptation d'un *  
			// 103 acceptation d'un (
			// 104 acceptation d'un )
			// 105 acceptation d'un ,
			// 106 acceptation d'un entier                   (retourArriere)
			// 107 acceptation d'un identifiant ou mot clé   (retourArriere)
			// 108 acceptation d'un point           
			// 109 acceptation d'un ^  
			// 110 acceptation d'un nombre décimal           (retourArriere)
			// 111 acceptation d'un /  
			// 112 acceptation d'un -  
			// 113 acceptation d'un - (symbole nb négatif)   (retourArriere)
	};

	/**
	 * La chaine de caractères qu'il faut analyser
	 */
	private String entree;
	
	/**
	 * Position dans la chaine de caractères lors de son parcours
	 */
	private int pos;
	
	/**
	 * Etat initial dans lequel on se trouve au début de l'analyse
	 */
	private static final int ETAT_INITIAL = 0;

	/**
	 * effectue l'analyse lexicale et retourne une liste de Token
	 * @param entree - chaine de caractères à analyser
	 * @return List<Token> - la liste des tokens suite à l'analyse lexicale, en vue d'une analyse syntaxique
	 * @throws Exception - notamment LexicalErrorException, plus de transitions possibles donc entree rejetée
	 */
	public List<Token> analyse(String entree) throws Exception {

		this.entree=entree;
		pos = 0;
		
		/**
		 * Liste des tokens qui sera renvoyée, en vue d'une analyse syntaxique notamment 
		 */
		List<Token> tokens = new ArrayList<>();

		/** copie des symboles en entrée
		 * - permet de distinguer les mots-clés des identifiants
		 * - permet de conserver une copie des valeurs particulières des tokens de type ident et intv
		 */
		String buf = "";

		Integer etat = ETAT_INITIAL;

		Character c;
		do {
			c = lireCaractere();
			Integer e = TRANSITIONS[etat][indiceSymbole(c)];
			
			// cas où la chaine de caractère n'est pas reconnu 
			if (e == null) {
				String messageErreur;
				// test pour affichage (1er ou enieme)
				if(pos == 1) messageErreur = "pas de transition depuis état " + etat + " avec symbole " + c;
				else messageErreur = "pas de transition depuis état " + etat + " avec symbole " + c;
				throw new LexicalErrorException(messageErreur);
			}
			// cas particulier lorsqu'un état d'acceptation est atteint
			if (e >= 100) {
				if (e == 101) {
					tokens.add(new Token(TypeDeToken.add, pos));
				} else if (e == 102) {
					tokens.add(new Token(TypeDeToken.multiply, pos));
				} else if (e == 103) {
					tokens.add(new Token(TypeDeToken.leftPar, pos));
				} else if (e == 104) {
					tokens.add(new Token(TypeDeToken.rightPar, pos));
				} else if (e == 105) {
					tokens.add(new Token(TypeDeToken.comma, pos));
				} else if (e == 108) {
					tokens.add(new Token(TypeDeToken.point, pos));
				} else if (e == 109) {
					tokens.add(new Token(TypeDeToken.kSymbolPow, pos));
				} else if (e == 111) {
					tokens.add(new Token(TypeDeToken.divide, pos));
				} else if (e == 112) {
					tokens.add(new Token(TypeDeToken.substract, pos+1)); // pos+1 : permet de rajouter l'espace obligatoire derrière
					retourArriere();
				} else if (e == 106) {
					tokens.add(new Token(TypeDeToken.intv, buf, pos-1));
					retourArriere();
				} else if (e == 110) {
					tokens.add(new Token(TypeDeToken.floatv, buf, pos-1));
					retourArriere();
				} else if (e == 113) {
					tokens.add(new Token(TypeDeToken.negative, pos-1));
					retourArriere();
				} else if (e == 107) {
				    if (buf.equals("pow")) {
						tokens.add(new Token(TypeDeToken.kFunctionPow, pos-1));
					} else if (buf.equals("abs")) {
						tokens.add(new Token(TypeDeToken.kAbs, pos-1));
					} else if (buf.equals("cos")) {
						tokens.add(new Token(TypeDeToken.kCos, pos-1));
					} else if (buf.equals("sin")) {
						tokens.add(new Token(TypeDeToken.kSin, pos-1));
					} else if (buf.equals("tan")) {
						tokens.add(new Token(TypeDeToken.kTan, pos-1));
					} else if (buf.equals("exp")) {
						tokens.add(new Token(TypeDeToken.kExp, pos-1));
					} else {
						tokens.add(new Token(TypeDeToken.ident, buf, pos-1));
					}
					retourArriere();
				}
				// un état d'acceptation ayant été atteint, retourne à l'état 0
				etat = 0;
				// reinitialise buf
				buf = "";
			} else {
				// enregistre le nouvel état
				etat = e;
				// ajoute le symbole qui vient d'être examiné à buf
				// sauf s'il s'agit un espace ou assimilé
				if (etat>0) buf = buf + c;
			}

		} while (c != null);

		return tokens;
	}
	
	/**
	 * Lit les caractères de la chaine de caractères "entree" 1 par 1
	 * @return Character - caractère de la chaine de caractère "entree" à la position pos
	 */
	private Character lireCaractere() {
		Character c;
		try {
			c = entree.charAt(pos);
			pos = pos + 1;
		} catch (StringIndexOutOfBoundsException ex) {
			pos++; // permet de gérer les positions d'erreurs, notamment pour les appels de fonctions si fin de chaine, exemple : "abs" ou "sin"
			c = null;
		}
		return c;
	}
	
	/**
	 * Effectue un retour arrière dans la lecture des caractères de la chaine de caractères "entree"
	 */
	private void retourArriere() {
		pos = pos - 1;
	}

	/**
	 * Pour chaque symbole terminal acceptable en entrée de l'analyse syntaxique
	 * retourne un indice identifiant soit un symbole, soit une classe de symbole :
	 * @param c - caractère à analyser
	 * @return int - indice de la colonne à laquelle correspond le caractère dans la matrice TRANSITIONS
	 * @throws IllegalCharacterException - le caractère est un symbole inconnu
	 */
	private int indiceSymbole(Character c) throws IllegalCharacterException {
		if (c == null) return 0;
		if (Character.isWhitespace(c)) return 0;
		if (c == '+') return 1;
		if (c == '*') return 2;
		if (c == '(') return 3;
		if (c == ')') return 4;
		if (c == ',') return 5;
		if (c == '.') return 8;
		if (c == '^') return 9;
		if (c == '/') return 10;
		if (c == '-') return 11;
		if (Character.isDigit(c)) return 6;
		if (Character.isLetter(c)) return 7;
		else {
			String messageErreur;
			if(pos == 1) messageErreur = "Erreur : Symbole à la " + pos + "ere position inconnu : " + c;
			else messageErreur = "Erreur : Symbole à la " + pos + "eme position inconnu : " + c;
			throw new IllegalCharacterException(messageErreur);
		}
	}

}
