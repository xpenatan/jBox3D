package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

final class SensorHitsSample extends AbstractBox3DSample {
    private final SampleRandom random = new SampleRandom();
    private B3Body kinematicSensorBody;
    private B3Joint dynamicSensorJoint;
    private B3Body projectile;
    private B3Mesh gridMesh;

    SensorHitsSample() {
        addGroundBox(10.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        addBoxShape(ground, 0.1f, 5.0f, 5.0f, 10.0f, 5.0f, 0.0f, null,
                1.0f, 0.6f, 0.0f, 0.0f);
        gridMesh = B3Mesh.CreateGrid(2, 2, 5.0f, 0, true);
        B3Quat sensorRotation = rotationZ(0.5f * (float)Math.PI);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);

        B3Body staticSensor = createBody(B3.StaticBody(), -4.0f, 6.0f, 0.0f, sensorRotation);
        attachMeshSensor(staticSensor, scale);

        B3BodyDef kinematicDef = bodyDef(B3.KinematicBody(), 0.0f, 6.0f, 0.0f, sensorRotation);
        B3Vec3 velocity = new B3Vec3(0.5f, 0.0f, 0.0f);
        kinematicDef.SetLinearVelocity(velocity);
        kinematicSensorBody = world().CreateBody(kinematicDef);
        attachMeshSensor(kinematicSensorBody, scale);

        B3Body dynamicSensor = createBody(B3.DynamicBody(), 4.0f, 1.0f, 0.0f, null);
        B3ShapeDef sensorDef = new B3ShapeDef();
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        B3Vec3 center1 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 9.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.1f);
        dispose(dynamicSensor.CreateCapsuleShape(sensorDef, capsule));

        B3Vec3 pivot = new B3Vec3(4.0f, 7.0f, 0.0f);
        B3Vec3 localA = ground.GetLocalPoint(pivot);
        B3Vec3 localB = dynamicSensor.GetLocalPoint(pivot);
        B3PrismaticJointDef jointDef = new B3PrismaticJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(dynamicSensor.GetId());
        jointDef.SetLocalPositionA(localA);
        jointDef.SetLocalPositionB(localB);
        jointDef.SetEnableMotor(true);
        jointDef.SetMaxMotorForce(1000.0f);
        jointDef.SetMotorSpeed(0.5f);
        dynamicSensorJoint = world().CreatePrismaticJoint(jointDef);

        dispose(jointDef, localB, localA, pivot, capsule, center2, center1, sensorDef, dynamicSensor,
                velocity, kinematicDef, staticSensor, scale, sensorRotation, ground);
        launch();
    }

    @Override
    public void step(float deltaSeconds) {
        B3Vec3 position = kinematicSensorBody.GetPosition();
        if(position.GetX() > 1.0f) {
            setLinearVelocity(kinematicSensorBody, -0.5f);
        }
        else if(position.GetX() < -1.0f) {
            setLinearVelocity(kinematicSensorBody, 0.5f);
        }
        dispose(position);

        float translation = dynamicSensorJoint.GetPrismaticTranslation();
        if(translation > 1.0f) {
            dynamicSensorJoint.SetPrismaticMotorSpeed(-0.5f);
        }
        else if(translation < -1.0f) {
            dynamicSensorJoint.SetPrismaticMotorSpeed(0.5f);
        }
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(gridMesh, projectile, dynamicSensorJoint, kinematicSensorBody);
    }

    private void attachMeshSensor(B3Body body, B3Vec3 scale) {
        B3ShapeDef sensorDef = new B3ShapeDef();
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        dispose(body.CreateMeshShape(sensorDef, gridMesh, scale), sensorDef);
    }

    private static void setLinearVelocity(B3Body body, float x) {
        B3Vec3 velocity = new B3Vec3(x, 0.0f, 0.0f);
        body.SetLinearVelocity(velocity);
        dispose(velocity);
    }

    private void launch() {
        if(projectile != null && projectile.IsValid()) {
            projectile.Destroy();
            dispose(projectile);
        }
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), -26.7f, 6.0f, 0.0f, null);
        bodyDef.SetIsBullet(true);
        B3Vec3 velocity = new B3Vec3(random.nextFloat(200.0f, 300.0f), 0.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);
        projectile = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.8f, 0.0f, 0.01f);
        shapeDef.SetEnableSensorEvents(true);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.25f);
        dispose(projectile.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef, velocity, bodyDef);
    }
}
