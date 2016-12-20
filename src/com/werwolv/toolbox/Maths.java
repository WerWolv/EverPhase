package com.werwolv.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, double rx, double ry, double rz, float scale) {
        Matrix4f matrix;

        matrix = Maths.setIdentityMatrix();

        matrix.translate(translation);

        matrix.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0));
        matrix.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0));
        matrix.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1));

        matrix.scale(scale);

        return matrix;
    }

    public static Matrix4f setIdentityMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.m00(1.0F);
        matrix.m11(1.0F);
        matrix.m22(1.0F);
        matrix.m33(1.0F);

        matrix.m01(0.0F);
        matrix.m02(0.0F);
        matrix.m03(0.0F);
        matrix.m10(0.0F);
        matrix.m12(0.0F);
        matrix.m13(0.0F);
        matrix.m20(0.0F);
        matrix.m21(0.0F);
        matrix.m23(0.0F);
        matrix.m30(0.0F);
        matrix.m31(0.0F);
        matrix.m32(0.0F);

        return matrix;
    }
}
