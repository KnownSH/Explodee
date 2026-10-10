package derg.explodee.api;

public final class ExplodeeMath {
	private static final double ANGLE_STEP = Math.PI * (1 + Math.sqrt(5));

	private ExplodeeMath() {
	}

	public static int rayCount(float radius) {
		return (int) Math.max(radius * radius, 100);
	}

	private static double phi(int i, int n) {
		return Math.acos(1 - 2.0 * i / n);
	}

	private static double theta(int i) {
		return ANGLE_STEP * i;
	}

	public static double x(int i, int n) {
		return Math.sin(phi(i, n)) * Math.cos(theta(i));
	}

	public static double y(int i, int n) {
		return Math.sin(phi(i, n)) * Math.sin(theta(i));
	}

	public static double z(int i, int n) {
		return Math.cos(phi(i, n));
	}
}
