package com.github.xpenatan.box3d.gdx;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.JBox3DLoader;
import com.github.xpenatan.jParser.api.NativeObject;
import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GdxBox3DConverterTest {
    private static final float EPSILON = 0.00001f;

    @BeforeClass
    public static void loadBox3D() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<Throwable>();
        JBox3DLoader.init(new JParserLibraryLoaderListener() {
            @Override
            public void onLoad(boolean isSuccess, Throwable throwable) {
                if(!isSuccess) {
                    error.set(throwable != null ? throwable : new RuntimeException("Box3D JNI loader returned false"));
                }
                latch.countDown();
            }
        });
        assertTrue("Box3D JNI loader did not finish", latch.await(10, TimeUnit.SECONDS));
        if(error.get() != null) {
            throw new AssertionError("Box3D JNI loader failed", error.get());
        }
    }

    @Test
    public void convertsVectorsBothWaysIntoReusableOutputs() {
        B3Vec3 box3dSource = new B3Vec3(1.25f, -2.5f, 3.75f);
        Vector3 gdxOut = new Vector3();
        B3Vec3 box3dOut = new B3Vec3();

        try {
            assertSame(gdxOut, GdxBox3DConverter.toGdx(box3dSource, gdxOut));
            assertVector(1.25f, -2.5f, 3.75f, gdxOut);
            assertSame(box3dOut, GdxBox3DConverter.toBox3D(new Vector3(-4.0f, 5.0f, 6.0f), box3dOut));
            assertVector(-4.0f, 5.0f, 6.0f, box3dOut);
        }
        finally {
            dispose(box3dOut, box3dSource);
        }
    }

    @Test
    public void convertsQuaternionsBothWaysIntoReusableOutputs() {
        B3Quat box3dSource = new B3Quat(0.1f, 0.2f, 0.3f, 0.9f);
        Quaternion gdxOut = new Quaternion();
        B3Quat box3dOut = new B3Quat();

        try {
            assertSame(gdxOut, GdxBox3DConverter.toGdx(box3dSource, gdxOut));
            assertQuaternion(0.1f, 0.2f, 0.3f, 0.9f, gdxOut);
            assertSame(box3dOut,
                    GdxBox3DConverter.toBox3D(new Quaternion(-0.2f, 0.4f, -0.1f, 0.85f), box3dOut));
            assertQuaternion(-0.2f, 0.4f, -0.1f, 0.85f, box3dOut);
        }
        finally {
            dispose(box3dOut, box3dSource);
        }
    }

    @Test
    public void convertsRigidTransformsWithoutAllocatingOutputs() {
        float halfSqrt = (float)Math.sqrt(0.5);
        B3Vec3 sourcePosition = new B3Vec3(2.0f, 3.0f, 4.0f);
        B3Quat sourceRotation = new B3Quat(0.0f, 0.0f, halfSqrt, halfSqrt);
        B3Transform box3dSource = new B3Transform(sourcePosition, sourceRotation);
        Matrix4 gdxOut = new Matrix4();
        B3Transform box3dOut = new B3Transform();

        try {
            assertSame(gdxOut, GdxBox3DConverter.toGdx(box3dSource, gdxOut));
            Vector3 transformed = new Vector3(1.0f, 0.0f, 0.0f).mul(gdxOut);
            assertVector(2.0f, 4.0f, 4.0f, transformed);

            Vector3 position = new Vector3(-3.0f, 7.0f, 1.5f);
            Quaternion rotation = new Quaternion(0.25f, -0.5f, 0.1f, 0.75f);
            assertSame(box3dOut, GdxBox3DConverter.toBox3D(position, rotation, box3dOut));
            assertVector(position.x, position.y, position.z, box3dOut.GetP());
            assertQuaternion(rotation.x, rotation.y, rotation.z, rotation.w, box3dOut.GetQ());
        }
        finally {
            dispose(box3dOut, box3dSource, sourceRotation, sourcePosition);
        }
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

    private static void dispose(NativeObject... objects) {
        for(NativeObject object : objects) {
            if(object != null && object.native_hasOwnership() && !object.isDisposed()) {
                object.dispose();
            }
        }
    }
}
