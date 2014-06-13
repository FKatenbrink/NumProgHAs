import java.util.Arrays;

public class Test_Interpolation {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		testNewton();
		testSplines();
	}

	private static void testNewton() {

		final double[] x = { -1, 1, 3 };
		final double[] y = { -3, 1, -3 };
		final NewtonPolynom p = new NewtonPolynom(x, y);

		System.out.println(p.evaluate(0) + " sollte sein: 0.0");
		System.out.println("-------------------------------");

		final double[] x1 = { 1, -1, 0 };
		final double[] y1 = { 1, 1, 0 };
		final NewtonPolynom p1 = new NewtonPolynom(x1, y1);

		System.out.println(p1.evaluate(2) + " sollte sein: 4.0");
		System.out.println("-------------------------------");

		final double[] x2 = { 1 };
		final double[] y2 = { 1 };
		final NewtonPolynom p2 = new NewtonPolynom(x2, y2);

		System.out.println(p2.evaluate(-100) + " sollte sein: 1.0");
		System.out.println("-------------------------------");

		p2.addSamplingPoint(-2, -32);
		p2.addSamplingPoint(-4.5, -1845.28125);
		p2.addSamplingPoint(10, 100000);
		p2.addSamplingPoint(0.5, 0.03125);
		p2.addSamplingPoint(0.123, 0.00002815305);

		System.out.println((double) Math.round(p2.evaluate(3) * 10000) / 10000 + " sollte sein: 243.0");
		System.out.println("-------------------------------");

		final double[] x3 = { -10, 1 };
		final double[] y3 = { -304, 4 };
		final NewtonPolynom p3 = new NewtonPolynom(x3, y3);
		p3.addSamplingPoint(-3, -24);

		System.out.println((double) Math.round(p3.evaluate(7.4) * 100000) / 100000 + " sollte sein: -150.88");
		System.out.println("-------------------------------");
	}

	public static void testSplines() {
		final CubicSpline spl = new CubicSpline();
		final double[] y = { 2, 0, 2, 3 };
		spl.init(-1, 2, 3, y);
		spl.setBoundaryConditions(9, 0);
		System.out.println(Arrays.toString(spl.getDerivatives()) + " sollte sein: [9.0, -3.0, 3.0, 0.0].");
	}
}
