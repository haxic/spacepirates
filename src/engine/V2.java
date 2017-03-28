package engine;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class V2 {
	public double x, y;
	private double magnitude;

	public V2(double x, double y) {
		this.x = x;
		this.y = y;
		calculateMagnitude();
	}

	public V2() {
		x = 0;
		y = 0;
	}

	public V2 add(V2 v) {
		return new V2(x + v.x, y + v.y);
	}

	public V2 sub(V2 v) {
		return new V2(x - v.x, y - v.y);
	}

	private void calculateMagnitude() {
		magnitude = Math.sqrt(x * x + y * y);
	}

	public double magnitude() {
		return magnitude;
	}

	public V2 cross(V2 v) {
		return new V2(x * v.y, y * v.x);
	}

	public double dot(V2 v) {
		return x * v.x + y * v.y;
	}

	public V2 mul(double s) {
		return new V2(x * s, y * s);
	}

	public V2 normalize() {
		V2 v = new V2(0, 0);
		if (magnitude != 0) {
			v.x = x / magnitude;
			v.y = y / magnitude;
		}
		return v;
	}

	public V2 negate() {
		return new V2(-x, -y);
	}

	public V2 negateX() {
		return new V2(-x, y);
	}

	public V2 negateY() {
		return new V2(x, -y);
	}

	public V2 divide(double d) {
		return new V2(x / d, y / d);
	}

	public V2 clone() {
		return new V2(x, y);
	}

	@Override
	public String toString() {
		return "[" + x + ":" + y + "]";
	}

	public double toRadians() {
		return Math.atan2(y, x);
	}
	
	public V2 radToV2(double rad) {
		return new V2(Math.cos(rad), Math.sin(rad));
	}

	public String toStringFormatted() {
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		return "[" + df.format(x) + ":" + df.format(y) + "]";
	}
}
