import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Die Klasse LinearInterpolation beschreibt ein stueckweises
 * Interpolationsverfahren. In jedem Intervall zwischen zwei benachbarten
 * Stuetzstellen werden die entsprechenden Stuetzwerte mit einer Geraden
 * verbunden. Liegt eine zu auswertende Stelle z ausserhalb der Stuetzgrenzen,
 * werden die aeussersten Werte y[0] bzw. y[n] zurueckgegeben.
 * 
 * @author braeckle
 * 
 */
public class LinearInterpolation implements InterpolationMethod {

	/** Die Stuetzstellen x_i */
	double[] x;
	/** Die Stuetzwerte y_i */
	double[] y;

	@Override
	public void init(final double a, final double b, final int n, final double[] y) {
		this.y = y;
		x = new double[n + 1];
		final double h = (b - a) / n;

		for (int i = 0; i < n + 1; i++) {
			x[i] = a + i * h;
		}
	}

	/**
	 * Initialisierung des Interpolationsverfahrens mit beliebigen Stuetzstellen
	 * und ordnet die Stuetzstellen der Groesse nach. Die Faelle
	 * "x und y sind unterschiedlich lang" oder "eines der beiden Arrays ist
	 * leer" werden nicht beachtet.
	 * 
	 * @param x
	 *           Stuetzstellen
	 * @param y
	 *           Stuetzwerte
	 */
	public void init(final double[] x, final double[] y) {
		if (x.length != y.length || x.length == 0) {
			return;
		}

		final int n = x.length;

		/* die Stuetzstellen werden der Groesse nach geordnet */
		final List<Integer> indices = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++) {
			indices.add(i);
		}

		final Comparator<Integer> comparator = new Comparator<Integer>() {

			@Override
			public int compare(final Integer i, final Integer j) {
				if (x[i] - x[j] < 0) {
					return -1;
				}
				if (x[i] - x[j] > 0) {
					return 1;
				} else {
					return 0;
				}
			}
		};

		Collections.sort(indices, comparator);

		this.x = new double[n];
		this.y = new double[n];

		for (int i = 0; i < n; i++) {
			final int index = indices.get(i);
			this.x[i] = x[index];
			this.y[i] = y[index];
		}
	}

	/**
	 * {@inheritDoc} Liegt z ausserhalb der Stuetzgrenzen, werden die aeussersten
	 * Werte y[0] bzw. y[n] zurueckgegeben. Liegt z zwischen den Stuetzstellen
	 * x_i und x_i+1, wird eine Gerade mit den Punkten (x_i,y_i) und (x_i+1,
	 * y_i+1) gebildet und in z ausgewertet.
	 * 
	 * Die Stuetzstellen liegen der Groesse nach geordnet vor. Der Fall
	 * ungeordneter Stuetzstellen oder leerer Stuetzstellen muss nicht extra
	 * behandelt werden.
	 */
	@Override
	public double evaluate(final double z) {
		if (z < x[0]) {
			return y[0];
		} else if (z > x[x.length - 1]) {
			return y[y.length - 1];
		}

		for (int i = 0; i < x.length - 1; i++) {
			if (z >= x[i] && z <= x[i + 1]) {
				// // a*(x-b) + c
				// final double a = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
				// final double b = x[i];
				// final double c = y[i];

				// m*x + t
				final double m = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
				final double t = y[i] - m * x[i];

				// assert (m * z + t == a * (z - b) + c);

				return m * z + t;
			}
		}

		return -1; // Wird nie aufgerufen.
	}

}
