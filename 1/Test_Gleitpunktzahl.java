public class Test_Gleitpunktzahl {

	public static void main(String[] argv) {
		testBitFeld();
		test_Gleitpunktzahl();
	}

	private static void testBitFeld() {
		System.out.println("-----------------------------------------");
		System.out.println("Test der Klasse BitFeld");

		/* Test der Addition mit BitFeld */
		try {
			BitFeld a;
			BitFeld b;
			BitFeld checkref;
			BitFeld erg;
			System.out.println("Test der Addition mit BitFeld");
			// Test: Bitweise Addition


			a = new BitFeld(8);
			b = new BitFeld(8);
			a.setInt(148);
			b.setInt(158);

			// Referenzwerte setzen
			checkref = new BitFeld(9);
			checkref.setInt(306);

			// Berechnung
			System.out.println("Test: Bitweise Addition");
			erg = a.add(b);

			// Test, ob Ergebnis korrekt
			if (erg.compareTo(checkref) != 0) {
				printAdd(a.toString(), b.toString());
				printErg(erg.toString(), checkref.toString());
			} else {
				System.out.println("    Richtiges Ergebnis\n");
			}

			/*************
			 * Eigene Tests einfuegen
			 */

		} catch (Exception e) {
			System.out.print("Exception bei der Auswertung des Ergebnis!!\n");
		}

		/* Test der Subtraktion mit BitFeld */
		System.out.println("Test der Subtraktion mit BitFeld");

		try {

			BitFeld a;
			BitFeld b;
			BitFeld checkref;
			BitFeld erg;
			System.out.println("Test der Subtraktion mit BitFeld");
			// Test: Bitweise Addition


			a = new BitFeld(8);
			b = new BitFeld(8);
			a.setInt(200);
			b.setInt(148);

			// Referenzwerte setzen
			checkref = new BitFeld(9);
			checkref.setInt(52);

			// Berechnung
			System.out.println("Test: Bitweise Subtraktion");
			erg = a.sub(b);

			// Test, ob Ergebnis korrekt
			if (erg.compareTo(checkref) != 0) {
				printSub(a.toString(), b.toString());
				printErg(erg.toString(), checkref.toString());
			} else {
				System.out.println("    Richtiges Ergebnis\n");
			}
			/*************
			 * Eigene Tests einfuegen
			 */

			System.out.println("\n\nEIGENE TESTS EINFÜGEN!!!!!!!\n\n");

		} catch (Exception e) {
			System.out.print("Exception bei der Auswertung des Ergebnis!!\n");
		}
	}

	public static void test_Gleitpunktzahl() {

		/**********************************/
		/* Test der Klasse Gleitpunktzahl */
		/**********************************/

		System.out.println("-----------------------------------------");
		System.out.println("Test der Klasse Gleitpunktzahl");

		/*
		 * Verglichen werden die BitFelder fuer Mantisse und Exponent und das
		 * Vorzeichen
		 */
		Gleitpunktzahl.setAnzBitsMantisse(8);
		Gleitpunktzahl.setAnzBitsExponent(6);

		Gleitpunktzahl x;
		Gleitpunktzahl y;
		Gleitpunktzahl gleitref = new Gleitpunktzahl();
		Gleitpunktzahl gleiterg;

		/* Addition */
		System.out.println("Test der Addition mit Gleitpunktzahl");
		try {
			// Test: Addition
			System.out.println("Test: Addition  x + y");
			x = new Gleitpunktzahl(13.57);
			y = new Gleitpunktzahl(5.723);

			// Referenzwerte setzen
			gleitref.mantisse.setInt(155);
			gleitref.exponent.setInt(37);
			gleitref.vorzeichen = false;
			gleitref = new Gleitpunktzahl(19.293);

			// Berechnung
			gleiterg = x.add(y);

			// Test, ob Ergebnis korrekt
			if (gleiterg.compareAbsTo(gleitref) != 0
					|| gleiterg.vorzeichen != gleitref.vorzeichen) {
				printAdd(x.toString(), y.toString());
				printErg(gleiterg.toString(), gleitref.toString());
			} else {
				System.out.println("    Richtiges Ergebnis\n");
			}

		} catch (Exception e) {
			System.out.print("Exception bei der Auswertung des Ergebnis!!\n");
		}

		/* Subtraktion */
		try {
			System.out.println("Test der Subtraktion mit Gleitpunktzahl");

			// Test: Addition
						System.out.println("Test: Subtraktion  x + y");
						x = new Gleitpunktzahl(13.57);
						y = new Gleitpunktzahl(5.723);

						// Referenzwerte setzen
						gleitref.mantisse.setInt(155);
						gleitref.exponent.setInt(37);
						gleitref.vorzeichen = false;
						gleitref = new Gleitpunktzahl(7.847);

						// Berechnung
						gleiterg = x.sub(y);

						// Test, ob Ergebnis korrekt
						if (gleiterg.compareAbsTo(gleitref) != 0
								|| gleiterg.vorzeichen != gleitref.vorzeichen) {
							printSub(x.toString(), y.toString());
							printErg(gleiterg.toString(), gleitref.toString());
						} else {
							System.out.println("    Richtiges Ergebnis\n");
						}
			/*************
			 * Eigene Tests einfuegen
			 */

			System.out.println("\n\nEIGENE TESTS EINFÜGEN!!!!!!!\n\n");

		} catch (Exception e) {
			System.out.print("Exception bei der Auswertung des Ergebnis!!\n");
		}

		/* Sonderfaelle */
		System.out.println("Test der Sonderfaelle mit Gleitpunktzahl");

		try {
			// Test: Sonderfaelle
			// 0 - inf
			System.out.println("Test: Sonderfaelle");
			x = new Gleitpunktzahl(0.0);
			y = new Gleitpunktzahl(1.0 / 0.0);

			// Referenzwerte setzen
			gleitref.mantisse.setInt(0);
			gleitref.exponent.setInt(63);
			gleitref.vorzeichen = true;

			// Berechnung mit der Methode des Studenten durchfuehren
			gleiterg = x.sub(y);

			// Test, ob Ergebnis korrekt
			if (gleiterg.compareAbsTo(gleitref) != 0
					|| gleiterg.vorzeichen != gleitref.vorzeichen) {
				printSub(x.toString(), y.toString());
				printErg(gleiterg.toString(), gleitref.toString());
			} else {
				System.out.println("    Richtiges Ergebnis\n");
			}

		} catch (Exception e) {
			System.out
					.print("Exception bei der Auswertung des Ergebnis in der Klasse Gleitpunktzahl!!\n");
		}

	}

	private static void printAdd(String x, String y) {
		System.out.println("    Fehler!\n      Es wurde gerechnet:            "
				+ x + " + " + y);
	}

	private static void printSub(String x, String y) {
		System.out.println("    Fehler!\n      Es wurde gerechnet:            "
				+ x + " - " + y + " = \n");
	}

	private static void printErg(String erg, String checkref) {
		System.out.println("      Ihr Ergebnis lautet:           " + erg
				+ "\n      Das Korrekte Ergebnis lautet:  " + checkref + "\n");
	}
}
