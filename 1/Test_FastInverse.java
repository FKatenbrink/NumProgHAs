import javax.swing.JFrame;

/**
 * @author Christoph Riesinger (riesinge@in.tum.de)
 * @author Jürgen Bräckle (braeckle@in.tum.de)
 * @version 1.2  10.Mai 2013
 * 
 *          This class just contains a main() method to use the FastMath class
 *          and to invoke the plotter.
 */
public class Test_FastInverse {

	/** Beispielwerte fuer IEEE Standard mit 32 Bits */
	private static int MAGIC_NUMBER = 0x5f3759df;

	private static int anzBitsExponent = 8;
	private static int anzBitsMantisse = 24;

	/**
	 * Uses the FastMath class and invokes the plotter. In a logarithmically
	 * scaled system, the exakt solutions of 1/sqrt(x) are shown in green, and
	 * the ABSOLUTE ERRORS of the Fast Inverse Square Root in RED. Can be used
	 * to test and debug the own implementation of the fast inverse square root
	 * algorithm and to play while finding an optimal magic number.
	 * 
	 * @param args
	 *            args is ignored.
	 */
	public static void main(String[] args) {

		Gleitpunktzahl.setAnzBitsExponent(anzBitsExponent);
		Gleitpunktzahl.setAnzBitsMantisse(anzBitsMantisse);
		FastMath.setMagic(MAGIC_NUMBER);

		int numOfSamplingPts = 1001;
		float[] xData = new float[numOfSamplingPts];
		float[] yData = new float[numOfSamplingPts];
		float x = 0.10f;

		/* calculate data to plot */
		for (int i = 0; i < numOfSamplingPts; i++) {
			xData[i] = x;
			Gleitpunktzahl y = new Gleitpunktzahl(x);
			yData[i] = (float) FastMath.absInvSqrtErr(y);

			x *= Math.pow(100.0d, 1.0d / numOfSamplingPts);
		}

		/* initialize plotter */
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			frame.add(new Plotter(xData, yData));
		} catch (InstantiationException exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		frame.setSize(960, 720);
		frame.setLocation(0, 0);
		frame.setVisible(true);
	}
}
