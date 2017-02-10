package com.deltabase.everphase.engine.toolbox;

import com.deltabase.everphase.entity.EntityPlayer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, double rx, double ry, double rz, float scale) {
        Matrix4f matrix = new Matrix4f();

        matrix.identity();

        matrix.translate(translation);

        matrix.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0));
        matrix.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0));
        matrix.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1));

        matrix.scale(new Vector3f(scale, scale, scale));

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.scale(scale);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(new Vector3f(translation.x, translation.y, 0.0F));
        matrix.scale(new Vector3f(scale.x, scale.y, 1.0F));

        return matrix;
    }

    public static Matrix4f createViewMatrix(EntityPlayer camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1));

        Vector3f cameraPos = camera.getPosition();
        Vector3f negCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negCameraPos);

        return viewMatrix;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Vector3f toVector3f(Vector4f vector4f) {
        return new Vector3f(vector4f.x, vector4f.y, vector4f.z);
    }

    public static Vector4f toVector4f(Vector3f vector3f) {
        return new Vector4f(vector3f.x, vector3f.y, vector3f.z, 0.0F);
    }

}
