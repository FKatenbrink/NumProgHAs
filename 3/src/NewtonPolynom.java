import java.util.Arrays;

/**
 * Die Klasse Newton-Polynom beschreibt die Newton-Interpolation. Die Klasse
 * bietet Methoden zur Erstellung und Auswertung eines Newton-Polynoms, welches
 * uebergebene Stuetzpunkte interpoliert.
 * 
 * @author braeckle
 * 
 */
public class NewtonPolynom implements InterpolationMethod {

	/** Stuetzstellen xi */
	double[] x;

	/**
	 * Koeffizienten/Gewichte des Newton Polynoms p(x) = a0 + a1*(x-x0) +
	 * a2*(x-x0)*(x-x1)+...
	 */
	double[] a;

	/**
	 * die Diagonalen des Dreiecksschemas. Diese dividierten Differenzen werden
	 * fuer die Erweiterung der Stuetzstellen benoetigt.
	 */
	double[] f;

	/**
	 * leerer Konstruktore
	 */
	public NewtonPolynom() {
	};

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *           Stuetzstellen
	 * @param y
	 *           Stuetzwerte
	 */
	public NewtonPolynom(final double[] x, final double[] y) {
		this.init(x, y);
	}

	/**
	 * {@inheritDoc} Zusaetzlich werden die Koeffizienten fuer das Newton-Polynom
	 * berechnet.
	 */
	@Override
	public void init(final double a, final double b, final int n, final double[] y) {
		x = new double[n + 1];
		final double h = (b - a) / n;

		for (int i = 0; i < n + 1; i++) {
			x[i] = a + i * h;
		}
		computeCoefficients(y);
	}

	/**
	 * Initialisierung der Newtoninterpolation mit beliebigen Stuetzstellen. Die
	 * Faelle "x und y sind unterschiedlich lang" oder "eines der beiden Arrays
	 * ist leer" werden nicht beachtet.
	 * 
	 * @param x
	 *           Stuetzstellen
	 * @param y
	 *           Stuetzwerte
	 */
	public void init(final double[] x, final double[] y) {
		this.x = Arrays.copyOf(x, x.length);
		computeCoefficients(y);
	}

	/**
	 * computeCoefficients belegt die Membervariablen a und f. Sie berechnet zu
	 * uebergebenen Stuetzwerten y, mit Hilfe des Dreiecksschemas der
	 * Newtoninterpolation, die Koeffizienten a_i des Newton-Polynoms. Die
	 * Berechnung des Dreiecksschemas soll dabei lokal in nur einem Array der
	 * Laenge n erfolgen (z.B. spaltenweise Berechnung). Am Ende steht die
	 * Diagonale des Dreiecksschemas in der Membervariable f, also f[0],f[1],
	 * ...,f[n] = [x0...x_n]f,[x1...x_n]f,...,[x_n]f. Diese koennen spaeter bei
	 * der Erweiterung der Stuetzstellen verwendet werden.
	 * 
	 * Es gilt immer: x und y sind gleich lang.
	 */
	private void computeCoefficients(final double[] y) {
		a = new double[y.length];
		f = new double[y.length];
		a[0] = y[y.length - 1];
		f[f.length - 1] = a[0];
		double below;
		for (int i = y.length - 2; i >= 0; i--) {
			below = a[0];
			a[0] = y[i];
			for (int j = 1; j < y.length - i; j++) {
				final double temp = a[j];
				a[j] = (below - a[j - 1]) / (x[i + j] - x[i]);
				below = temp;
			}
			f[i] = a[y.length - i - 1];
		}
	}

	/**
	 * Gibt die Koeffizienten des Newton-Polynoms a zurueck
	 */
	public double[] getCoefficients() {
		return a;
	}

	/**
	 * Gibt die Dividierten Differenzen der Diagonalen des Dreiecksschemas f
	 * zurueck
	 */
	public double[] getDividedDifferences() {
		return f;
	}

	/**
	 * addSamplintPoint fuegt einen weiteren Stuetzpunkt (x_new, y_new) zu x
	 * hinzu. Daher werden die Membervariablen x, a und f vergoessert und
	 * aktualisiert . Das gesamte Dreiecksschema muss dazu nicht neu aufgebaut
	 * werden, da man den neuen Punkt unten anhaengen und das alte Dreiecksschema
	 * erweitern kann. Fuer diese Erweiterungen ist nur die Kenntnis der
	 * Stuetzstellen und der Diagonalen des Schemas, bzw. der Koeffizienten
	 * noetig. Ist x_new schon als Stuetzstelle vorhanden, werden die
	 * Stuetzstellen nicht erweitert.
	 * 
	 * @param x_new
	 *           neue Stuetzstelle
	 * @param y_new
	 *           neuer Stuetzwert
	 */
	public void addSamplingPoint(final double x_new, final double y_new) {
		final double[] extX = new double[x.length + 1];
		final double[] extA = new double[x.length + 1];
		for (int i = 0; i < x.length; i++) {
			extX[i] = x[i];
			extA[i] = a[i];
		}
		extX[extX.length - 1] = x_new;

		final double[] extF = new double[x.length + 1];
		extF[extF.length - 1] = y_new;
		for (int i = extF.length - 2; i >= 0; i--) {
			extF[i] = (extF[i + 1] - f[i]) / (x_new - x[i]);
		}
		extA[extA.length - 1] = extF[0];

		x = extX;
		a = extA;
		f = extF;
	}

	/**
	 * {@inheritDoc} Das Newton-Polynom soll effizient mit einer Vorgehensweise
	 * aehnlich dem Horner-Schema ausgewertet werden. Es wird davon ausgegangen,
	 * dass die Stuetzstellen nicht leer sind.
	 */
	@Override
	public double evaluate(final double z) {
		double p_z = a[a.length - 1];
		for (int i = a.length - 2; i >= 0; i--) {
			p_z *= (z - x[i]);
			p_z += a[i];
		}
		return p_z;
	}
}
