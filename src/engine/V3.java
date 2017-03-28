package engine;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class V3 extends V2 {
	public double z;

	public V3(double x, double y, double z) {
		super(x, y);
		this.z = z;
	}

	public V3(double x, double y) {
		super(x, y);
		z = 0;
	}

	public V3() {
		super(0, 0);
		z = 0;
	}

	public V3 add(V3 v) {
		return new V3(x + v.x, y + v.y, z + v.z);
	}

	public V3 sub(V3 v) {
		return new V3(x - v.x, y - v.y, z - v.z);
	}

	public V3 mul(double d) {
		return new V3(x * d, y * d, z * d);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public V3 cross(V3 v) {
		double crossX = y * v.z - v.y * z;
		double crossY = z * v.x - v.z * x;
		double crossZ = x * v.y - v.x * y;
		return (new V3(crossX, crossY, crossZ));
	}

	public double dot(V3 v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public V3 scale(double s) {
		return new V3(x * s, y * s, z * s);
	}

	public V3 scale(V3 v) {
		return new V3(x * v.x, y * v.y, z * v.z);
	}

	public V3 normalize() {
		double magnitude = magnitude();
		if (magnitude == 0)
			return new V3(0, 0, 0);
		double x = this.x / magnitude;
		double y = this.y / magnitude;
		double z = this.z / magnitude;
		return new V3(x, y, z);
	}

	public V3 scaleTo(double d) {
		double magnitude = magnitude();
		if (magnitude == 0)
			return new V3(0, 0, 0);
		magnitude = d / magnitude;
		double x = this.x * magnitude;
		double y = this.y * magnitude;
		double z = this.z * magnitude;
		return new V3(x, y, z);
	}

	public V3 negate() {
		return new V3(-x, -y, -z);
	}

	public V3 negateX() {
		return new V3(-x, y, z);
	}

	public V3 negateY() {
		return new V3(x, -y, z);
	}

	public V3 negateZ() {
		return new V3(x, y, -z);
	}

	public V3 divide(double i) {
		return new V3(x / i, y / i, z / i);
	}

	public V3 clone() {
		return new V3(x, y, z);
	}

	@Override
	public String toStringFormatted() {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		return "[" + df.format(x) + ":" + df.format(y) + ":" + df.format(z) + "]";
	}

	public V3 unit() {
		double magnitude = magnitude();
		if (magnitude > 0) {
			return this.divide(magnitude);
		}
		return new V3(1, 0, 0);
	}

	public static V3 radToV3(double rad) {
		return new V3(Math.cos(rad), Math.sin(rad));
	}

	@Override
	public String toString() {
		return x + " " + y + " " + z;
	}
}
