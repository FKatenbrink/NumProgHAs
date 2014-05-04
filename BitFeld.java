import java.util.Arrays;

/**
 * Update by
 * 
 * @author Jürgen Bräckle (braeckle@in.tum.de)
 * @version 1.2  10.Mai 2013
 * 
 *          Diese Klasse verwaltet ein Bitarray und liefert diverse Funktionen
 *          zur Manipulation von Bitfeldern
 */

public class BitFeld {

	/* Array zur Speicherung der Bits */
	public boolean[] bits;
	/* Laenge des BitFelds */
	private int size;

	/** erzeugt ein Bitfeld der Laenge size */
	BitFeld(int size) {
		this.size = size;
		this.bits = new boolean[size];
	}

	/** erzeugt eine Kopie von Bitfeld b **/
	BitFeld(BitFeld b) {
		this(b.getSize());
		for (int i = 0; i < size; i++) {
			this.bits[i] = b.bits[i];
		}
	}

	/**
	 * erzeugt ein Bitfeld der Laenge size und belegt alle Bits mit wert
	 */
	BitFeld(int size, boolean wert) {
		this(size);
		for (int i = 0; i < size; i++)
			bits[i] = wert;
	}

	/**
	 * erzeugt ein Bitfeld der Laenge size und setzt die Bits entsprechend dem
	 * Integer wert
	 */
	BitFeld(int size, int wert) {
		this(size);
		this.setInt(wert);
	}

	/** liefert die Laenge des Bitfeldes */
	public int getSize() {
		return size;
	}

	/** setzt alle Bits des Bitfeldes auf wert */
	public void setBits(boolean wert) {
		Arrays.fill(bits, wert);
	}

	/**
	 * erwartet eine natuerliche Zahl. Setzt die Bits entsprechend dem int wert.
	 * Uebersteigt wert den Zahlenbereich des Bitfeldes, so werden nicht
	 * darstellbare Bits einfach weggelassen (entspricht einer modulo- Rechnung
	 * mit 2^anzBitsMantisse). Ist wert negativ, wird Null gesetzt.
	 */
	public void setInt(int wert) {
		setBits(false);
		if (wert < 0)
			return;
		for (int i = 0; i < size; i++) {
			if (wert == 0)
				break;
			if ((wert & 1) == 1)
				bits[i] = true;
			wert = wert >>> 1;
		}
	}

	/** liefert eine String-Repraesentation des Bitfelds */
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = size - 1; i >= 0; i--) {
			if (bits[i])
				s.append('1');
			else
				s.append('0');
		}
		return s.toString();
	}

	/**
	 * interpretiert das Bitfeld als natuerliche Zahl und ermittelt den
	 * repraesentierten int-Wert. Funktioniert nur bis 31 Bit, danach bricht die
	 * Berechnung zusammen, da der Zahlenbereich von int zu "klein" wird
	 */
	public int toInt() {
		int erg = 0;
		int zweiHochI = 1;
		for (int i = 0; i < size; i++) {
			if (bits[i])
				erg += zweiHochI;
			zweiHochI *= 2;
		}
		return erg;
	}

	/**
	 * interpretiert das Bitfeld als natuerliche Zahl und ermittelt den
	 * repraesentierten Double-Wert. Double deshalb, da Long nur bis 63 Bit
	 * funktioniert, Double aber, mit evtl. Ungenauigkeiten, sehr viel groessere
	 * Zahlen darstellen kann
	 */
	public double toDouble() {
		double erg = 0;
		double zweiHochI = 1;
		for (int i = 0; i < size; i++) {
			if (bits[i])
				erg += zweiHochI;
			zweiHochI *= 2;
		}
		return erg;
	}

	/** Gibt das Objekt in binärer Darstellung aus */
	public void ausgeben() {
		System.out.println(this.toString());
	}

	/** Gibt den Integer Wert des Objektes aus */
	public void ausgebenInt() {
		System.out.println(this.toInt());
	}

	/**
	 * prueft, ob ALLE Bits 0 sind (entspricht der Abfrage, ob das BitFeld,
	 * interpretiert als natuerliche Zahl, den Wert 0 hat).
	 */
	public boolean isNull() {
		for (int i = 0; i < size; i++)
			if (bits[i])
				return false;
		return true;
	}

	/** vergleicht den Wert zweier gleich langer Bitfelder */
	public int compareTo(BitFeld b) {
		/*
		 * liefert 1, falls |this| > |b| 0, falls |this| = |b| -1, falls |this|
		 * < |b|
		 */

		/* Arrays Bitweise vergleichen */
		for (int i = this.size - 1; i >= 0; i--) {
			if (this.bits[i] & !(b.bits[i]))
				return 1;
			if (!(this.bits[i]) & b.bits[i])
				return -1;
		}

		/*
		 * Es konnte keine Differenz gefunden werden => Bitfelder sind gleich
		 */
		return 0;

	}

	/** haengt VORNE anzahlNeueStellen an das Bitfeld an */
	public void erweitern(int anzahlNeueStellen) {

		if (anzahlNeueStellen <= 0)
			return;

		size = size + anzahlNeueStellen;
		this.bits = Arrays.copyOf(this.bits, size);
	}

	/**
	 * schiebt das gesamte Bitfeld um anzShifts Stellen nach rechts.
	 * Herausgeshiftete Bits gehen verloren, die Bit an de hoechsten Stellen
	 * erhalten den Wert von fillIn
	 */
	public void shiftRight(int anzShifts, boolean fillIn) {
		if (anzShifts <= 0)
			return;
		for (int i = anzShifts; i < size; i++)
			bits[i - anzShifts] = bits[i];
		for (int i = size - anzShifts; i < size; i++) {
			bits[i] = fillIn;
		}
	}

	/**
	 * schiebt das gesamte Bitfeld um anzShifts Stellen nach links.
	 * Herausgeshiftete Bits gehen verloren, die Bits an den niedrigsten Stellen
	 * erhalten den Wert von fillIn
	 */
	public void shiftLeft(int anzShifts, boolean fillIn) {
		if (anzShifts <= 0)
			return;
		for (int i = size - 1 - anzShifts; i >= 0; i--)
			bits[i + anzShifts] = bits[i];
		for (int i = 0; i < anzShifts; i++) {
			bits[i] = fillIn;
		}
	}

	/**
	 * erhoeht den Wert des als natuerliche Zahl interpretierten Bitfeldes um 1.
	 * Tritt dabei ein Ueberlauf auf, so wird die Zahl um eine Stelle nach
	 * rechts geschoben und eine 0 herausgeshiftet. Zurueckgeliefert wird, ob
	 * nach rechts geschoben wurde. Diese Methode dient zum Aufrunden im
	 * Zusammenhang mit reellen Zahlen
	 */
	public boolean inc() {
		int i = 0;
		/*
		 * Von rechts nach links, jede 1 wird zur 0 -> Uebertrag zur naechsten
		 * Stelle
		 */
		while (i < size && this.bits[i]) {
			this.bits[i] = false;
			i++;
		}
		/* die erste auftauchende 0 wird zur 1 -> wir sind fertig */
		if (i < size) {
			this.bits[i] = true;
			return false;
		}
		/*
		 * Es gab keine 0 ->Uebertrag -> Vergroesserung des Bitfeldes + shift um
		 * eins nach rechts
		 */
		else {
			/*
			 * Zu diesem Zeitpunkt ist das Feld voll mit "0"ern. Bitfeld
			 * erweitern + Rechtsshift entspricht setzten des fordersten Bits
			 */
			this.bits[size - 1] = true;
			return true;
		}
	}
	 
	/**
	 * addiert zum als natuerliche Zahl interpretierten Bitfeld das Bitfeld b.
	 * Beide Bitfelder muessen gleich lang sein. Der Fall nicht gleich langer
	 * Felder muss nicht abgefangen werden. Das Ergebnis wird in einem um eine
	 * Stelle laengeren Bitfeld zurueckgeliefert, damit ein eventueller
	 * Uebertrag gefasst werden kann.
	 */
	public BitFeld add(BitFeld b) {

		/*
		 * TODO: hier ist die bitweise Operation add zu implementieren. Die
		 * Verwendung von toInt ist hier nicht gestattet, da die BitFeldklasse
		 * auch fuer >32Bits funktionieren soll.
		 */
		BitFeld result = new BitFeld (b.getSize()+1);
		boolean borrow = false;
		for (int i = 0; i < b.getSize(); i++) {
			boolean temp = this.bits[i] ^ b.bits[i];
			result.bits[i] = temp ^ borrow;
			
			if(this.bits[i] & b.bits[i] || temp & borrow ){
				borrow = true;
			}
			else{
				borrow = false;
			}
		}
		result.bits[b.getSize()] = borrow;
		return result;
		
	}

	/**
	 * subtrahiert vom als natuerliche Zahl interpretierten Bitfeld das Bitfeld
	 * b. Beide Bitfelder muessen gleich lang sein. Das Ergebnis wird in einem
	 * neuen Bitfeld zurueckgeliefert, das ebenfalls die gleiche Laenge besitzt.
	 * Es kann davon ausgegangen werden, dass b stets kleiner oder gleich dem
	 * aktuellen Objekt ist.
	 */
	public BitFeld sub(BitFeld b) {

		/*
		 * TODO: hier ist die bitweise Operation sub zu implementieren. Die
		 * Verwendung von toInt ist hier nicht gestattet, da die BitFeldklasse
		 * auch fuer >32Bits funktionieren soll.
		 */
		BitFeld result = new BitFeld (b.getSize()+1);
		boolean borrow = false;
		for (int i = 0; i < b.getSize(); i++) {
			boolean temp = this.bits[i] ^ b.bits[i];
			result.bits[i] = temp ^ borrow;
			
			if(!this.bits[i] & b.bits[i] || !temp & borrow ){
				borrow = true;
			}
			else{
				borrow = false;
			}
		}
		result.bits[b.getSize()] = borrow;
		return result;
	}
}
