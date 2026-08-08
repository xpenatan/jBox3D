package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of {@code BenchmarkChains}. */
final class BenchmarkChainsSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 25;
    private final long[] shapeIds = new long[GRID_COUNT * GRID_COUNT];
    private final SampleRandom random = new SampleRandom();
    private B3Mesh mesh;
    private float noiseX;
    private float noiseY;
    private float noiseZ;

    BenchmarkChainsSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        mesh = B3Mesh.CreateWave(80, 80, 1.0f, 0.5f, 0.05f, 0.01f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, mesh, unitScale), ground, unitScale);

        float linkRadius = 0.125f;
        float linkExtent = 0.25f;
        B3Vec3 center1 = new B3Vec3(0.0f, -linkExtent, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, linkExtent, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, linkRadius);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetEnableSleep(false);
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        jointDef.SetLocalPositionA(center1);
        jointDef.SetLocalPositionB(center2);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(1.0f);
        jointDef.SetDampingRatio(0.7f);
        jointDef.SetEnableMotor(true);
        jointDef.SetMaxMotorTorque(1.0f);
        B3Vec3 position = new B3Vec3();
        int shapeIndex = 0;
        float x = -GRID_COUNT;
        for(int rowIndex = 0; rowIndex < GRID_COUNT; ++rowIndex) {
            float z = -GRID_COUNT;
            for(int columnIndex = 0; columnIndex < GRID_COUNT; ++columnIndex) {
                long previousBodyId = 0L;
                for(int i = 0; i < 4; ++i) {
                    position.Set(x, (1.0f - 2.0f * i) * linkExtent + 3.0f, z);
                    bodyDef.SetPosition(position);
                    bodyDef.SetType(i == 0 ? B3.StaticBody() : B3.DynamicBody());
                    B3Body body = world().CreateBody(bodyDef);
                    long bodyId = body.GetId();
                    B3Shape shape = body.CreateCapsuleShape(shapeDef, capsule);
                    if(i == 3) {
                        shapeIds[shapeIndex++] = shape.GetId();
                    }
                    if(i > 0) {
                        jointDef.SetBodyIdA(previousBodyId);
                        jointDef.SetBodyIdB(bodyId);
                        dispose(world().CreateSphericalJoint(jointDef));
                    }
                    previousBodyId = bodyId;
                    dispose(shape, body);
                }
                z += 2.0f;
            }
            x += 2.0f;
        }
        dispose(position, jointDef, bodyDef, capsule, center2, center1, shapeDef);
    }

    @Override
    public void step(float deltaSeconds) {
        B3Vec3 wind = new B3Vec3(20.0f * (1.0f + noiseX), 20.0f * noiseY, 20.0f * noiseZ);
        for(long shapeId : shapeIds) {
            B3Shape shape = new B3Shape(shapeId);
            shape.ApplyWind(wind, 1.0f, 1.0f, 20.0f, false);
            dispose(shape);
        }
        noiseX = 0.95f * noiseX + 0.05f * random.nextFloat(-0.3f, 0.3f);
        noiseY = 0.95f * noiseY + 0.05f * random.nextFloat(-0.3f, 0.3f);
        noiseZ = 0.95f * noiseZ + 0.05f * random.nextFloat(-0.3f, 0.3f);
        dispose(wind);
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
