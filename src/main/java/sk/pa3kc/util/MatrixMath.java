package sk.pa3kc.util;

import sk.pa3kc.entities.Camera;
import sk.pa3kc.pojo.matrix.Matrix4f;
import sk.pa3kc.pojo.matrix.Vector3f;

public abstract class MatrixMath {
    private MatrixMath() {}

    public static Matrix4f createTransformationMatrix(
        Vector3f translation,
        float rx,
        float ry,
        float rz,
        float scale
    ) {
        final Matrix4f result = Matrix4f.identified();

        translate(translation, result, result);
        rotate((float) Math.toRadians(rx), new Vector3f(1f, 0f, 0f), result, result);
        rotate((float) Math.toRadians(ry), new Vector3f(0f, 1f, 0f), result, result);
        rotate((float) Math.toRadians(rz), new Vector3f(0f, 0f, 1f), result, result);
        scale(new Vector3f(scale, scale, scale), result, result);

        return result;
    }
    public static Matrix4f createViewMatrix(Camera camera) {
        final Matrix4f viewMatrix = Matrix4f.identified();

        rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1f, 0f, 0f), viewMatrix, viewMatrix);
		rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0f, 1f, 0f), viewMatrix, viewMatrix);

		final Vector3f negCameraPos = Vector3f.negate(camera.getPosition());

		translate(negCameraPos, viewMatrix, viewMatrix);

		return viewMatrix;
    }

    public static void identify(Matrix4f m) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                m.set(row, col, row == col ? 1f : 0f);
            }
        }
    }

    public static void translate(Vector3f vec, Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }

        dest.add(3, 0, (src.get(0, 0) * vec.x) + (src.get(1, 0) * vec.y) + (src.get(2, 0) * vec.z));
		dest.add(3, 1, (src.get(0, 1) * vec.x) + (src.get(1, 1) * vec.y) + (src.get(2, 1) * vec.z));
		dest.add(3, 2, (src.get(0, 2) * vec.x) + (src.get(1, 2) * vec.y) + (src.get(2, 2) * vec.z));
		dest.add(3, 3, (src.get(0, 3) * vec.x) + (src.get(1, 3) * vec.y) + (src.get(2, 3) * vec.z));
    }

    public static void rotate(float angle, Vector3f axis, Matrix4f src, Matrix4f dest) {
		if (dest == null) {
            dest = new Matrix4f();
        }

        float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x*axis.y;
		float yz = axis.y*axis.z;
		float xz = axis.x*axis.z;
		float xs = axis.x*s;
		float ys = axis.y*s;
		float zs = axis.z*s;

		float f00 = axis.x*axis.x*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		// n[3] not used
		float f10 = xy*oneminusc-zs;
		float f11 = axis.y*axis.y*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		// n[7] not used
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axis.z*axis.z*oneminusc+c;

		float t00 = src.get(0, 0) * f00 + src.get(1, 0) * f01 + src.get(2, 0) * f02;
		float t01 = src.get(0, 1) * f00 + src.get(1, 1) * f01 + src.get(2, 1) * f02;
		float t02 = src.get(0, 2) * f00 + src.get(1, 2) * f01 + src.get(2, 2) * f02;
		float t03 = src.get(0, 3) * f00 + src.get(1, 3) * f01 + src.get(2, 3) * f02;
		float t10 = src.get(0, 0) * f10 + src.get(1, 0) * f11 + src.get(2, 0) * f12;
		float t11 = src.get(0, 1) * f10 + src.get(1, 1) * f11 + src.get(2, 1) * f12;
		float t12 = src.get(0, 2) * f10 + src.get(1, 2) * f11 + src.get(2, 2) * f12;
		float t13 = src.get(0, 3) * f10 + src.get(1, 3) * f11 + src.get(2, 3) * f12;
		dest.set(2, 0, src.get(0, 0) * f20 + src.get(1, 0) * f21 + src.get(2, 0) * f22);
		dest.set(2, 1, src.get(0, 1) * f20 + src.get(1, 1) * f21 + src.get(2, 1) * f22);
		dest.set(2, 2, src.get(0, 2) * f20 + src.get(1, 2) * f21 + src.get(2, 2) * f22);
		dest.set(2, 3, src.get(0, 3) * f20 + src.get(1, 3) * f21 + src.get(2, 3) * f22);
		dest.set(0, 0, t00);
		dest.set(0, 1, t01);
		dest.set(0, 2, t02);
		dest.set(0, 3, t03);
		dest.set(1, 0, t10);
		dest.set(1, 1, t11);
		dest.set(1, 2, t12);
		dest.set(1, 3, t13);
    }

    public static void scale(Vector3f vec, Matrix4f src, Matrix4f dest) {
        if (dest == null) {
            dest = new Matrix4f();
        }

        dest.set(0, 0, src.get(0, 0) * vec.x);
		dest.set(0, 1, src.get(0, 1) * vec.x);
		dest.set(0, 2, src.get(0, 2) * vec.x);
		dest.set(0, 3, src.get(0, 3) * vec.x);
		dest.set(1, 0, src.get(1, 0) * vec.y);
		dest.set(1, 1, src.get(1, 1) * vec.y);
		dest.set(1, 2, src.get(1, 2) * vec.y);
		dest.set(1, 3, src.get(1, 3) * vec.y);
		dest.set(2, 0, src.get(2, 0) * vec.z);
		dest.set(2, 1, src.get(2, 1) * vec.z);
		dest.set(2, 2, src.get(2, 2) * vec.z);
		dest.set(2, 3, src.get(2, 3) * vec.z);
    }
}
