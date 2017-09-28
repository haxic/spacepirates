package utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;

public class Maths {
	public static Matrix4f matrix1 = new Matrix4f();
	public static Vector3f vector1 = new Vector3f();
	public static Matrix4f createModelMatrix(Vector3f offset, Vector3f rotation, Vector3f scale) {
		// Set identity matrix.
		matrix1.identity();
		matrix1.translate(offset);
		matrix1.rotateX((float) Math.toRadians(rotation.x));
		matrix1.rotateY((float) Math.toRadians(rotation.y));
		matrix1.rotateZ((float) Math.toRadians(rotation.z));
		matrix1.scale(scale);
		return matrix1;
	}
	public static Matrix4f createViewMatrix(Camera camera) {
		// Set identity matrix.
		matrix1.identity();
		matrix1.rotateX((float) Math.toRadians(camera.rotation.x));
		matrix1.rotateY((float) Math.toRadians(camera.rotation.y));
		matrix1.rotateZ((float) Math.toRadians(camera.rotation.z));
		matrix1.translate(camera.position.negate(vector1));
		return matrix1;
	}
}
