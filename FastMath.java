/**
 * @author Christoph Riesinger (riesinge@in.tum.de)
 * @author Jürgen Bräckle (braeckle@in.tum.de)
 * @version 1.2  10.Mai 2013
 * 
 *          This class contains methods for rapidly calculating basic
 *          mathematical operations.
 */
public class FastMath {
	/**
	 * The "magic" constant which is used in the fast inverse square root
	 * algorithm.
	 * 
	 * The given initial value is just a test value for 8 mantissa bits and 4
	 * exponent bits, and has to be optimized by the students.
	 * 
	 * In literature, several of those constants for floats or doubles can be
	 * found. There's no optimal constant for all cases.
	 */
	private static int MAGIC_NUMBER = 1024;

	/**
	 * belegt die MAGIC_NUMBER mit dem Wert magic
	 */
	public static void setMagic(int magic) {
		FastMath.MAGIC_NUMBER = magic;
	}

	/**
	 * This method contains the code for the fast inverse square root algorithm
	 * which can e.g. be found in "Fast Inverse Square Root" from Lomont, Chris
	 * (February, 2003).
	 * 
	 * It approximately calculates the value 1 / sqrt(x).
	 * 
	 * No Newton steps to improve the result has to be implemented in this
	 * exercise.
	 * 
	 * @param x
	 *            Input value of which the inverse square root should be
	 *            computed.
	 * @return Approximation for 1 / sqrt(x).
	 */
	public static Gleitpunktzahl invSqrt(Gleitpunktzahl x) {
		/* TODO: hier den "fast inverse square root" Algorithmus implementieren */
		if (x.vorzeichen || x.isInfinite() || x.isNaN() || x.isNull())
			return Gleitpunktzahl.getNaN();
		int y = gleitpunktzahlToIEEE(x).toInt()/2;
		y = FastMath.MAGIC_NUMBER - y;
		return iEEEToGleitpunktzahl(new BitFeld(32, y));
		
		
	}

	/**
	 * Calculates the absolute error between the result of the fast inverse
	 * square root algorithm and the "exact" IEEE-conform result.
	 * 
	 * @param x
	 *            Position where the absolute error should be determined.
	 * @return Absolute error between invSqrt(x) and 1 / Math.sqrt(x).
	 */
	public static double absInvSqrtErr(Gleitpunktzahl x) {
		double exact = 1 / Math.sqrt(x.toDouble());
		double approx = invSqrt(x).toDouble();
		double absErr = Math.abs(exact - approx);

		return absErr;
	}

	/**
	 * Calculates the relative error between the result of the fast inverse
	 * square root algorithm and the "exact" IEEE-conform result.
	 * 
	 * @param x
	 *            Position where the relative error should be determined.
	 * @return Relative error between invSqrt(x) and 1 / Math.sqrt(x).
	 */
	public static double relInvSqrtErr(Gleitpunktzahl x) {
		double absErr = absInvSqrtErr(x);
		double relErr = Math.abs(absErr * Math.sqrt(x.toDouble()));

		return relErr;
	}

	/**
	 * Uebersetzt die Gleitpunktzahl in eine BitFolge aehnlich dem IEEE
	 * Standard, d.h. in die Form [Vorzeichen, Exponent, Mantisse], wobei die
	 * führende 1 der Mantisse nicht gespeichert wird. Dieser Wechsel ist noetig
	 * für ein Funktionieren des Fast Inverse Sqrt Algorithmus
	 */
	public static BitFeld gleitpunktzahlToIEEE(Gleitpunktzahl x) {
		int anzBitsExponent = x.getAnzBitsExponent();
		int anzBitsMantisse = x.getAnzBitsMantisse();

		BitFeld result = new BitFeld(anzBitsExponent + anzBitsMantisse);
		/* mantisse ohne fuehrende 1 einfuegen */
		for (int i = 0; i < anzBitsMantisse - 1; i++) {
			result.bits[i] = x.mantisse.bits[i];
		}
		/* exponent vorne anhaengen */
		for (int i = 0; i < anzBitsExponent; i++) {
			result.bits[anzBitsMantisse - 1 + i] = x.exponent.bits[i];
		}
		/* vorzeichen setzen */
		result.bits[anzBitsExponent + anzBitsMantisse - 1] = x.vorzeichen;

		return result;
	}

	/**
	 * Liefert aus einem BitFeld in IEEE Darstellung, d.h. [Vorzeichen,
	 * Exponent, Mantisse] mit Mantisse ohne führende Null, die entsprechende
	 * Gleitpunktdarstellung
	 */
	public static Gleitpunktzahl iEEEToGleitpunktzahl(BitFeld b) {
		Gleitpunktzahl g = new Gleitpunktzahl();
		int anzBitsExponent = g.getAnzBitsExponent();
		int anzBitsMantisse = g.getAnzBitsMantisse();

		/* mantisse ohne fuehrende 1 einfuegen */
		for (int i = 0; i < anzBitsMantisse - 1; i++) {
			g.mantisse.bits[i] = b.bits[i];
		}
		/* fuehrende 1 fuer mantisse eintragen */
		g.mantisse.bits[anzBitsMantisse - 1] = true;

		/* exponent vorne anhaengen */
		for (int i = 0; i < anzBitsExponent; i++) {
			g.exponent.bits[i] = b.bits[anzBitsMantisse - 1 + i];
		}
		/* vorzeichen setzen */
		g.vorzeichen = b.bits[anzBitsExponent + anzBitsMantisse - 1];

		return g;
	}
}
