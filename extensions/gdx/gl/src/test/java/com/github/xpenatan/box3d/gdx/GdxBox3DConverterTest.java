package com.github.xpenatan.box3d.gdx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GdxBox3DConverterTest {
    private static final float EPSILON = 0.00001f;

    @Test
    public void convertsVectorsBothWaysIntoReusableOutputs() {
        FakeB3Vec3 box3dSource = new FakeB3Vec3(1.25f, -2.5f, 3.75f);
        Vector3 gdxOut = new Vector3();

        assertSame(gdxOut, GdxBox3DConverter.toGdx(box3dSource, gdxOut));
        assertVector(1.25f, -2.5f, 3.75f, gdxOut);

        FakeB3Vec3 box3dOut = new FakeB3Vec3();
        assertSame(box3dOut, GdxBox3DConverter.toBox3D(new Vector3(-4.0f, 5.0f, 6.0f), box3dOut));
        assertVector(-4.0f, 5.0f, 6.0f, box3dOut);
    }

    @Test
    public void convertsQuaternionsBothWaysIntoReusableOutputs() {
        FakeB3Quat box3dSource = new FakeB3Quat(0.1f, 0.2f, 0.3f, 0.9f);
        Quaternion gdxOut = new Quaternion();

        assertSame(gdxOut, GdxBox3DConverter.toGdx(box3dSource, gdxOut));
        assertQuaternion(0.1f, 0.2f, 0.3f, 0.9f, gdxOut);

        FakeB3Quat box3dOut = new FakeB3Quat();
        assertSame(box3dOut,
                GdxBox3DConverter.toBox3D(new Quaternion(-0.2f, 0.4f, -0.1f, 0.85f), box3dOut));
        assertQuaternion(-0.2f, 0.4f, -0.1f, 0.85f, box3dOut);
    }

    @Test
    public void convertsRigidTransformsWithoutAllocatingOutputs() {
        float halfSqrt = (float)Math.sqrt(0.5);
        FakeB3Transform box3dSource = new FakeB3Transform(
                new FakeB3Vec3(2.0f, 3.0f, 4.0f),
                new FakeB3Quat(0.0f, 0.0f, halfSqrt, halfSqrt));
        Matrix4 gdxOut = new Matrix4();

        assertSame(gdxOut, GdxBox3DConverter.toGdx(box3dSource, gdxOut));
        Vector3 transformed = new Vector3(1.0f, 0.0f, 0.0f).mul(gdxOut);
        assertVector(2.0f, 4.0f, 4.0f, transformed);

        FakeB3Transform box3dOut = new FakeB3Transform();
        Vector3 position = new Vector3(-3.0f, 7.0f, 1.5f);
        Quaternion rotation = new Quaternion(0.25f, -0.5f, 0.1f, 0.75f);
        assertSame(box3dOut, GdxBox3DConverter.toBox3D(position, rotation, box3dOut));
        assertVector(position.x, position.y, position.z, box3dOut.GetP());
        assertQuaternion(rotation.x, rotation.y, rotation.z, rotation.w, box3dOut.GetQ());
    }

    private static void assertVector(float x, float y, float z, Vector3 actual) {
        assertEquals(x, actual.x, EPSILON);
        assertEquals(y, actual.y, EPSILON);
        assertEquals(z, actual.z, EPSILON);
    }

    private static void assertVector(float x, float y, float z, B3Vec3 actual) {
        assertEquals(x, actual.GetX(), EPSILON);
        assertEquals(y, actual.GetY(), EPSILON);
        assertEquals(z, actual.GetZ(), EPSILON);
    }

    private static void assertQuaternion(float x, float y, float z, float w, Quaternion actual) {
        assertEquals(x, actual.x, EPSILON);
        assertEquals(y, actual.y, EPSILON);
        assertEquals(z, actual.z, EPSILON);
        assertEquals(w, actual.w, EPSILON);
    }

    private static void assertQuaternion(float x, float y, float z, float w, B3Quat actual) {
        B3Vec3 vector = actual.GetV();
        assertEquals(x, vector.GetX(), EPSILON);
        assertEquals(y, vector.GetY(), EPSILON);
        assertEquals(z, vector.GetZ(), EPSILON);
        assertEquals(w, actual.GetS(), EPSILON);
    }

    private static final class FakeB3Vec3 extends B3Vec3 {
        private float x;
        private float y;
        private float z;

        FakeB3Vec3() {
            this(0.0f, 0.0f, 0.0f);
        }

        FakeB3Vec3(float x, float y, float z) {
            super((byte)0, (char)0);
            Set(x, y, z);
        }

        @Override
        public float GetX() {
            return x;
        }

        @Override
        public float GetY() {
            return y;
        }

        @Override
        public float GetZ() {
            return z;
        }

        @Override
        public void Set(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static final class FakeB3Quat extends B3Quat {
        private final FakeB3Vec3 vector = new FakeB3Vec3();
        private float scalar;

        FakeB3Quat() {
            this(0.0f, 0.0f, 0.0f, 1.0f);
        }

        FakeB3Quat(float x, float y, float z, float w) {
            super((byte)0, (char)0);
            Set(x, y, z, w);
        }

        @Override
        public B3Vec3 GetV() {
            return vector;
        }

        @Override
        public float GetS() {
            return scalar;
        }

        @Override
        public void Set(float x, float y, float z, float w) {
            vector.Set(x, y, z);
            scalar = w;
        }
    }

    private static final class FakeB3Transform extends B3Transform {
        private final FakeB3Vec3 position;
        private final FakeB3Quat rotation;

        FakeB3Transform() {
            this(new FakeB3Vec3(), new FakeB3Quat());
        }

        FakeB3Transform(FakeB3Vec3 position, FakeB3Quat rotation) {
            super((byte)0, (char)0);
            this.position = position;
            this.rotation = rotation;
        }

        @Override
        public B3Vec3 GetP() {
            return position;
        }

        @Override
        public B3Quat GetQ() {
            return rotation;
        }
    }
}
