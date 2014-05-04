public class Gleitpunktzahl {

	/**
	 * Update by
	 * 
	 * @author Jürgen Bräckle (braeckle@in.tum.de)
	 * @author Thomas Hutzelmann
	 * @version 1.2  10.Mai 2013
	 * 
	 *          Diese Klasse beschreibt eine Form von Gleitpunktarithmetik
	 */

	/********************/
	/* Membervariablen: */
	/********************/

	/* Vorzeichen, Mantisse und Exponent der Gleitpunktzahl */
	public boolean vorzeichen; /* true = "-1" */
	public BitFeld mantisse; /* linksbuendig, fuehrende 1 wird gespeichert */
	public BitFeld exponent;

	/*
	 * Anzahl der Bits fuer Mantisse und Exponent: einmal gesetzt, soll sie
	 * nicht mehr veraendert werden koennen
	 */
	private static int anzBitsMantisse = 32;
	private static boolean anzBitsMantisseFixed = false;
	private static int anzBitsExponent = 8; /* Maximale Groesse: 32 */
	private static boolean anzBitsExponentFixed = false;

	/*
	 * Aus der Anzahl an Bits fuer den Exponenten laesst sich der maximale
	 * Exponent und der Offset berechnen
	 */
	private static int maxExponent = (int) Math.pow(2, anzBitsExponent) - 1;
	private static int expOffset = (int) Math.pow(2, anzBitsExponent - 1) - 1;

	/**
	 * Falls die Anzahl der Bits der Mantisse noch nicht gesperrt ist, so wird
	 * sie auf abm gesetzt und gesperrt
	 */
	public static void setAnzBitsMantisse(int abm) {
		/*
		 * Falls anzBitsMantisse noch nicht gesetzt und abm > 0 dann setze auf
		 * abm und sperre den Zugriff
		 */
		if (!anzBitsMantisseFixed & (abm > 0)) {
			anzBitsMantisse = abm;
			anzBitsMantisseFixed = true;
		} else {
			System.out.println("Bitanzahl kann nicht mehr gesetzt werden");
		}
	}

	/**
	 * Falls die Anzahl der Bits des Exponenten noch nicht gesperrt ist, so wird
	 * sie auf abe gesetzt und gesperrt. maxExponent und expOffset werden
	 * festgelegt
	 */
	public static void setAnzBitsExponent(int abe) {
		if (!anzBitsExponentFixed & (abe > 0)) {
			anzBitsExponent = abe;
			anzBitsExponentFixed = true;
			maxExponent = (int) Math.pow(2, abe) - 1;
			expOffset = (int) Math.pow(2, abe - 1) - 1;
		} else {
			System.out.println("Bitanzahl kann nicht mehr gesetzt werden");
		}
	}

	/** Liefert die Anzahl der Bits der Mantisse */
	public int getAnzBitsMantisse() {
		return this.mantisse.getSize();
	}

	/** Liefert die Anzahl der Bits des Exponenten */
	public int getAnzBitsExponent() {
		return this.exponent.getSize();
	}

	/**
	 * erzeugt eine Gleitpunktzahl ohne Anfangswert. Die Bitfelder fuer Mantisse
	 * und Exponent werden angelegt. Ist die Anzahl der Bits noch nicht gesetzt,
	 * wird sie auf 32 bzw. 8 gesetzt und gesperrt
	 */
	Gleitpunktzahl() {
		if (!anzBitsMantisseFixed) {
			anzBitsMantisse = 32;
			anzBitsMantisseFixed = true;
		}
		if (!anzBitsExponentFixed) {
			anzBitsExponent = 8;
			anzBitsExponentFixed = true;
			maxExponent = (int) Math.pow(2, anzBitsExponent) - 1;
			expOffset = (int) Math.pow(2, anzBitsExponent - 1) - 1;
		}
		/*
		 * Erzeuge neue BitFelder fuer die Speicherung von Mantisse und Exponent
		 */
		this.mantisse = new BitFeld(anzBitsMantisse);
		this.exponent = new BitFeld(anzBitsExponent);
	}

	/** erzeugt eine Kopie der reellen Zahl r */
	Gleitpunktzahl(Gleitpunktzahl r) {

		/* Vorzeichen kopieren */
		this.vorzeichen = r.vorzeichen;
		/*
		 * Kopiert den Inhalt der jeweiligen Felder aus r
		 */
		this.mantisse = new BitFeld(r.mantisse);
		this.exponent = new BitFeld(r.exponent);
	}

	/**
	 * erzeugt eine reelle Zahl mit der Repraesentation des Double-Wertes d. Ist
	 * die Anzahl der Bits fuer Mantisse und Exponent noch nicht gesetzt, wird
	 * sie auf 32 bzw. 8 gesetzt und gesperrt
	 */
	Gleitpunktzahl(double d) {

		this();
		this.setDouble(d);
	}

	/**
	 * setzt dieses Objekt mit der Repraesentation des Double-Wertes d.
	 */
	public void setDouble(double d) {

		/* Abfangen der Sonderfaelle */
		if (d == 0) {
			this.vorzeichen = false;
			this.mantisse.setBits(false);
			this.exponent.setBits(false);
			return;
		}
		if (Double.isInfinite(d)) {
			this.vorzeichen = (d < 0);
			this.mantisse.setBits(false);
			this.exponent.setInt(maxExponent);
			return;
		}
		if (Double.isNaN(d)) {
			this.vorzeichen = false;
			this.mantisse.setBits(true);
			this.exponent.setInt(maxExponent);
			return;
		}

		/* Falls d<0 -> Vorzeichen setzten, Vorzeichen von d wechseln */
		if (d < 0) {
			this.vorzeichen = true;
			d = -d;
		} else
			this.vorzeichen = false;

		/*
		 * Exponent exp von d zur Basis 2 finden d ist danach im Intervall [1,2)
		 */
		int exp = 0;
		while (d >= 2) {
			d = d / 2;
			exp++;
		}
		while (d < 1) {
			d = 2 * d;
			exp--;
		} /* d in [1,2) */

		/*
		 * Mantisse finden, analog zur binaeren Division Mantisse um 1
		 * vergroessern fuer evtl. Runden
		 */
		double rest = d;
		this.mantisse.erweitern(1);
		for (int i = anzBitsMantisse; i >= 0; i--) {
			if (rest >= 1) {
				rest = rest - 1;
				this.mantisse.bits[i] = true;
			} else
				this.mantisse.bits[i] = false;
			rest = 2 * rest;
		}

		/*
		 * normalisiere uebernimmt die Aufgaben des Rundens, das Setzten des
		 * Exponenten und Abfangen von Sonderfaellen. Deshalb setzten wir zuerst
		 * Exponent auf 0;
		 */
		this.exponent.setInt(expOffset);
		this.normalisiere(anzBitsMantisse - exp);
	}

	/** liefert eine String-Repraesentation des Objekts */
	public String toString() {
		StringBuffer s = new StringBuffer();
		if (this.vorzeichen)
			s.append('-');
		for (int i = this.mantisse.getSize() - 1; i >= 0; i--) {
			if (i == this.mantisse.getSize() - 2)
				s.append(',');
			if (this.mantisse.bits[i])
				s.append('1');
			else
				s.append('0');
		}
		s.append(" * 2^(");
		s.append(this.exponent.toString());
		s.append("-");
		s.append(expOffset);
		s.append(")");
		return s.toString();
	}

	/** berechnet den Double-Wert des Objekts */
	public double toDouble() {
		/*
		 * Wenn der Exponent maximal ist, nimmt die Gleitpunktzahl einen der
		 * speziellen Werte an
		 */
		if (this.exponent.toInt() == maxExponent) {
			/*
			 * Wenn die Mantisse Null ist, hat die Zahl den Wert Unendlich oder
			 * -Unendlich
			 */
			if (this.mantisse.isNull()) {
				if (this.vorzeichen)
					return -1.0 / 0.0;
				else
					return 1.0 / 0.0;
			}
			/* Ansonsten ist der Wert NaN */
			else
				return 0.0 / 0.0;
		}
		double m = this.mantisse.toDouble();
		if (this.vorzeichen)
			m *= (-1);
		return m
				* Math.pow(2, (this.exponent.toInt() - expOffset)
						- (this.mantisse.getSize() - 1));
	}

	/** Gibt das Objekt in binärer Darstellung aus */
	public void ausgeben() {
		System.out.println(this.toString());
	}

	/** Gibt den Double Wert des Objektes aus */
	public void ausgebenDouble() {
		System.out.println(this.toDouble());
	}

	/**
	 * Sonderfaelle abfragen
	 */
	/** Liefert true, wenn die Gleitpunktzahl die Null repräsentiert */
	public boolean isNull() {
		return (!this.vorzeichen && this.mantisse.isNull() && this.exponent
				.isNull());
	}

	/**
	 * Liefert true, wenn die Gleitpunktzahl der NotaNumber Darstellung
	 * entspricht
	 */
	public boolean isNaN() {
		return (!this.mantisse.isNull() && (this.exponent.toInt() == maxExponent));
	}

	/** Liefert true, wenn die Gleitpunktzahl betragsmaessig unendlich gross ist */
	public boolean isInfinite() {
		return (this.mantisse.isNull() && (this.exponent.toInt() == maxExponent));
	}
	
	/**
	 * Gibt eine Gleitpunktzahl mit dem Wert Null zurück
	 */
	public static Gleitpunktzahl getNull() {
		
		Gleitpunktzahl tmp = new Gleitpunktzahl();
		
		tmp.vorzeichen = false;
		tmp.mantisse.setBits(false);
		tmp.exponent.setBits(false);
		
		return tmp;
	}
	
	/**
	 * Gibt eine Gleitpunktzahl mit dem Wert NaN zurück
	 */
	public static Gleitpunktzahl getNaN() {
		
		Gleitpunktzahl tmp = new Gleitpunktzahl();
		
		tmp.vorzeichen = false;
		tmp.mantisse.setBits(true);
		tmp.exponent.setInt(maxExponent);
		
		return tmp;
	}
	
	
	/**
	 * Gibt eine Gleitpunktzahl mit dem Wert plus Unendlich zurück
	 */
	public static Gleitpunktzahl getPosInfinite() {
		
		Gleitpunktzahl tmp = new Gleitpunktzahl();
		
		tmp.vorzeichen = false;
		tmp.mantisse.setBits(false);
		tmp.exponent.setInt(maxExponent);
	
		return tmp;
	}
	
	/**
	 * Gibt eine Gleitpunktzahl mit dem Wert minus Unendlich zurück
	 */
	public static Gleitpunktzahl getNegInfinite() {
		
		Gleitpunktzahl tmp = new Gleitpunktzahl();
		
		tmp.vorzeichen = true;
		tmp.mantisse.setBits(false);
		tmp.exponent.setInt(maxExponent);
	
		return tmp;
	}

	/**
	 * vergleicht betragsmaessig den Wert des aktuellen Objekts mit der reellen
	 * Zahl r
	 */
	public int compareAbsTo(Gleitpunktzahl r) {
		/*
		 * falls |this| > |r| -> 1 falls |this| = |r| -> 0 falls |this| < |r| ->
		 * -1
		 */

		/* Exponenten vergleichen */
		int expVergleich = this.exponent.compareTo(r.exponent);

		if (expVergleich != 0)
			return expVergleich;

		/* Bei gleichen Exponenten: Bitweisses Vergleichen der Mantissen */
		return this.mantisse.compareTo(r.mantisse);
	}

	/**
	 * vergleicht den Wert des aktuellen Objekts mit der reellen Zahl r
	 */
	public int compareTo(Gleitpunktzahl r) {
		if (this.vorzeichen == false && r.vorzeichen == true)
			return 1;
		if (this.vorzeichen == true && r.vorzeichen == false)
			return -1;
		int compabs = compareAbsTo(r);
		if (!this.vorzeichen)
			return compabs;
		else
			return -compabs;

	}

	/**
	 * normalisiert und rundet das aktuelle Objekt auf die Darstellung r =
	 * (-1)^vorzeichen * 1,r_t-1 r_t-2 ... r_1 r_0 * 2^exponent. Die 0 wird zu
	 * (-1)^0 * 0,00...00 * 2^0 normalisiert WICHTIG: Es kann sein, dass die
	 * Anzahl der Bits nicht mit anzBitsMantisse uebereinstimmt. Das Ergebnis
	 * soll aber eine Mantisse mit anzBitsMantisse Bits haben. Deshalb muss
	 * evtl. mit Bits aufgefuellt oder Bits abgeschnitten werden. Dabei muss das
	 * Ergebnis nach Definition gerundet werden.
	 * 
	 * anzNachkommastellen -> ist die bekannte Anzahl an Nachkommastellen der
	 * aktuellen Mantisse bzw. die Stelle des Kommas
	 * 
	 * Beispiel: Bei 4 Mantissenbits wird die Zahl 110.11 * 2^-1 mit 2
	 * Nachkommastellen zu 1.110 * 2^1
	 */
	public void normalisiere(int anzNachkommastellen) {

		/* Null normalisieren */
		if (mantisse.isNull()) {
			this.mantisse = new BitFeld(anzBitsMantisse, false);
			if (this.exponent.toInt() < maxExponent) {
				this.vorzeichen = false;
				this.exponent.setBits(false);
			}
			return;
		}

		/* Fuehrende 1 suchen */
		int positionErsteEins = this.mantisse.getSize() - 1;
		while (!(this.mantisse.bits[positionErsteEins]))
			positionErsteEins--;

		/* Exponent versetzten, um Mantisse zu normalisieren */
		int exp = this.exponent.toInt() - expOffset - anzNachkommastellen
				+ positionErsteEins;

		/*
		 * Ist Exponent um 1 zu klein -> aufrunden zur kleinsten darstellbaren
		 * positiven Zahl
		 */
		if (exp + expOffset == -1) {
			this.mantisse = new BitFeld(anzBitsMantisse, false);
			this.mantisse.bits[anzBitsMantisse - 1] = true;
			this.exponent.setBits(false);
			return;
		}

		/* neue Mantisse aufbauen */
		BitFeld neueMantisse = new BitFeld(anzBitsMantisse, false);

		/*
		 * Ist die Anzahl an gueltigen Stellen <= anzBitsMantisse -> mit 0ern
		 * auffuellen
		 */
		if (positionErsteEins < anzBitsMantisse) {
			for (int i = positionErsteEins; i >= 0; i--) {
				neueMantisse.bits[anzBitsMantisse - 1 + i - positionErsteEins] = this.mantisse.bits[i];
			}
		}
		/* Ist die Anzahl an gueltigen Stellen > anzBitsMantisse -> Runden */
		else {
			for (int i = positionErsteEins; i > positionErsteEins
					- anzBitsMantisse; i--) {
				neueMantisse.bits[anzBitsMantisse - 1 + i - positionErsteEins] = this.mantisse.bits[i];
			}
			/* Aufrunden, falls das darauffolgende Bit "1" waere */
			if (this.mantisse.bits[positionErsteEins - anzBitsMantisse]) {
				boolean geshiftet = neueMantisse.inc();
				if (geshiftet)
					exp++;
			}
		}

		this.mantisse = neueMantisse;

		/* Sonderfaelle abfangen und Exponent setzen */
		if (exp + expOffset >= maxExponent) { /* Exponent zu groß -> Unendlich */
			this.exponent.setBits(true);
			this.mantisse.setBits(false);
		} else if (exp + expOffset < 0) {/* Exponent zu klein -> Null */
			this.vorzeichen = false;
			this.mantisse.setBits(false);
			this.exponent.setBits(false);
		} else {/* Exponent = exp + offset */
			this.exponent.setInt(exp + expOffset);
		}
	}

	/**
	 * denormalisiert die betragsmaessig goessere Zahl, so dass die Exponenten
	 * von a und b gleich sind. Die Mantissen beider Zahlen werden entsprechend
	 * erweitert. Denormalisieren wird fuer add und sub benoetigt.
	 */
	public static void denormalisiere(Gleitpunktzahl a, Gleitpunktzahl b) {
		int expDiff = Math.abs(a.exponent.toInt() - b.exponent.toInt());

		/* Haben beide Zahlen den gleichen Exponenten, ist nichts zu tun */
		if (expDiff == 0)
			return;

		/* Beide Mantissenfelder um expDiff vergroeßern */
		a.mantisse.erweitern(expDiff);
		b.mantisse.erweitern(expDiff);

		/*
		 * Exponent der groeßeren Zahl anpassen und entsprechende Mantisse nach
		 * links shiften
		 */
		if (a.compareAbsTo(b) > 0) {
			a.exponent = new BitFeld(b.exponent);
			a.mantisse.shiftLeft(expDiff, false);
		} else {
			b.exponent = new BitFeld(a.exponent);
			b.mantisse.shiftLeft(expDiff, false);
		}
	}

	/**
	 * addiert das aktuelle Objekt und die Gleitpunktzahl r. Dabei wird zuerst
	 * die betragsmaeßig groeßere Zahl denormalisiert und die Mantissen beider
	 * zahlen entsprechend vergroessert. Das Ergebnis wird in einem neuen Objekt
	 * gespeichert, normiert, und dieses wird zurueckgegeben.
	 */
	public Gleitpunktzahl add(Gleitpunktzahl r) {

		/* 
		 * TODO: hier ist die Operation add zu implementieren. Verwenden Sie die
		 * Funktionen normalisiere, denormalisiere und die Funktionen add/sub
		 * der BitFeldklasse. Achten Sie auf Sonderfaelle und die Einschraenkung
		 * der Funktion BitFeld.sub.
		 */
		if (this.isNaN() || r.isNaN())
			return getNaN();
		if (this.isInfinite() || r.isInfinite())
			return getPosInfinite();
		Gleitpunktzahl a = new Gleitpunktzahl(this);
		Gleitpunktzahl b = new Gleitpunktzahl(r);
		Gleitpunktzahl result = new Gleitpunktzahl();
		
		if (a.vorzeichen && b.vorzeichen) {
			a.vorzeichen = false;
			b.vorzeichen = false;
			result = a.add(b);
			result.vorzeichen = true;
			return result;
		}
		else if (a.vorzeichen){
			a.vorzeichen = false;
			return b.sub(a);
		}
		else if (b.vorzeichen) {
			b.vorzeichen = false;
			return a.sub(b);
		}
		
		denormalisiere(a, b);
		result.exponent = a.exponent;
		result.mantisse = a.mantisse.add(b.mantisse);
		result.normalisiere(anzBitsMantisse-1);
		return result;
	}

	/**
	 * subtrahiert vom aktuellen Objekt die Gleitpunktzahl r. Dabei wird zuerst
	 * die betragsmaeßig groeßere Zahl denormalisiert und die Mantissen beider
	 * zahlen entsprechend vergroessert. Das Ergebnis wird in einem neuen Objekt
	 * gespeichert, normiert, und dieses wird zurueckgegeben.
	 */
	public Gleitpunktzahl sub(Gleitpunktzahl r) {

		/*
		 * TODO: hier ist die Operation sub zu implementieren Verwenden Sie die
		 * Funktionen normalisiere, denormalisiere und die Funktionen add/sub
		 * der BitFeldklasse. Achten Sie auf Sonderfaelle und die Einschraenkung
		 * der Funktion BitFeld.sub.
		 */
		if ((this.isInfinite() && r.isInfinite()) || this.isNaN() || r.isNaN())
			return getNaN();
		if (this.isInfinite())
			return getPosInfinite();
		if (r.isInfinite())
			return getNegInfinite();
		Gleitpunktzahl a = new Gleitpunktzahl(this);
		Gleitpunktzahl b = new Gleitpunktzahl(r);
		Gleitpunktzahl result = new Gleitpunktzahl();
		
		
		if (a.vorzeichen && b.vorzeichen) {
			a.vorzeichen = false;
			b.vorzeichen = false;
			return b.sub(a);
		}
		else if (a.vorzeichen){
			a.vorzeichen = false;
			result = a.add(b);
			result.vorzeichen = true;
			return result;
		}
		else if (b.vorzeichen) {
			b.vorzeichen = false;
			return a.add(b);
		}
		
		denormalisiere(a, b);
		result.exponent = a.exponent;
		if (a.mantisse.compareTo(b.mantisse) > -1) {
			result.mantisse = a.mantisse.sub(b.mantisse);
		} else {
			result.mantisse = b.mantisse.sub(a.mantisse);
			result.vorzeichen = true;
		}
		result.normalisiere(anzBitsMantisse-1);
		return result;
	}

}
