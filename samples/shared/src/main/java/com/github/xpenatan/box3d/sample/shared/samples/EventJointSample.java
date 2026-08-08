package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3JointEvent;
import com.github.xpenatan.box3d.B3JointEvents;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WeldJointDef;

final class EventJointSample extends AbstractBox3DSample {
    private static final float FORCE_THRESHOLD = 3000.0f;
    private static final float TORQUE_THRESHOLD = 10000.0f;

    private final B3Joint[] joints = new B3Joint[6];

    EventJointSample() {
        addGroundBox(20.0f);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetEnableSleep(false);
        B3Hull box = B3Hull.CreateBox(1.0f, 1.0f, 0.5f);
        B3ShapeDef shapeDef = new B3ShapeDef();

        float x = -12.5f;
        B3Body body = createBody(bodyDef, x, 10.0f, box, shapeDef);
        B3Vec3 worldPivotA = newVector(x, 13.0f, 0.0f);
        B3Vec3 worldPivotB = newVector(x, 11.0f, 0.0f);
        B3Vec3 pivotA = ground.GetLocalPoint(worldPivotA);
        B3Vec3 pivotB = body.GetLocalPoint(worldPivotB);
        B3DistanceJointDef distanceDef = new B3DistanceJointDef();
        configure(distanceDef, ground, body, pivotA, pivotB);
        distanceDef.SetLength(2.0f);
        joints[0] = world().CreateDistanceJoint(distanceDef);
        dispose(distanceDef, pivotB, pivotA, worldPivotB, worldPivotA, body);

        x += 10.0f;
        body = createBody(bodyDef, x, 10.0f, box, shapeDef);
        B3Vec3 worldPivot = newVector(x - 1.0f, 10.0f, 0.0f);
        pivotA = ground.GetLocalPoint(worldPivot);
        pivotB = body.GetLocalPoint(worldPivot);
        B3PrismaticJointDef prismaticDef = new B3PrismaticJointDef();
        configure(prismaticDef, ground, body, pivotA, pivotB);
        joints[2] = world().CreatePrismaticJoint(prismaticDef);
        dispose(prismaticDef, pivotB, pivotA, worldPivot, body);

        x += 5.0f;
        body = createBody(bodyDef, x, 10.0f, box, shapeDef);
        worldPivot = newVector(x - 1.0f, 10.0f, 0.0f);
        pivotA = ground.GetLocalPoint(worldPivot);
        pivotB = body.GetLocalPoint(worldPivot);
        B3RevoluteJointDef revoluteDef = new B3RevoluteJointDef();
        configure(revoluteDef, ground, body, pivotA, pivotB);
        joints[3] = world().CreateRevoluteJoint(revoluteDef);
        dispose(revoluteDef, pivotB, pivotA, worldPivot, body);

        x += 5.0f;
        body = createBody(bodyDef, x, 10.0f, box, shapeDef);
        worldPivot = newVector(x - 1.0f, 10.0f, 0.0f);
        pivotA = ground.GetLocalPoint(worldPivot);
        pivotB = body.GetLocalPoint(worldPivot);
        B3WeldJointDef weldDef = new B3WeldJointDef();
        configure(weldDef, ground, body, pivotA, pivotB);
        weldDef.SetAngularHertz(2.0f);
        weldDef.SetAngularDampingRatio(0.5f);
        joints[4] = world().CreateWeldJoint(weldDef);
        dispose(weldDef, pivotB, pivotA, worldPivot, body);

        dispose(shapeDef, box, bodyDef, ground);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        B3JointEvents events = world().GetJointEvents();
        for(int i = 0; i < events.GetCount(); i++) {
            B3JointEvent event = events.GetEvent(i);
            long id = event.GetJointId();
            for(int jointIndex = 0; jointIndex < joints.length; jointIndex++) {
                B3Joint joint = joints[jointIndex];
                if(joint != null && joint.GetId() == id && joint.IsValid()) {
                    joint.Destroy(true);
                    break;
                }
            }
            dispose(event);
        }
        dispose(events);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(joints);
    }

    private B3Body createBody(B3BodyDef bodyDef, float x, float y, B3Hull box, B3ShapeDef shapeDef) {
        B3Vec3 position = newVector(x, y, 0.0f);
        bodyDef.SetPosition(position);
        B3Body body = world().CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, box), position);
        return body;
    }

    private static B3Vec3 newVector(float x, float y, float z) {
        return new B3Vec3(x, y, z);
    }

    private static void configure(B3DistanceJointDef def, B3Body a, B3Body b, B3Vec3 localA, B3Vec3 localB) {
        def.SetBodyIdA(a.GetId());
        def.SetBodyIdB(b.GetId());
        def.SetLocalPositionA(localA);
        def.SetLocalPositionB(localB);
        def.SetForceThreshold(FORCE_THRESHOLD);
        def.SetTorqueThreshold(TORQUE_THRESHOLD);
        def.SetCollideConnected(true);
    }

    private static void configure(B3PrismaticJointDef def, B3Body a, B3Body b, B3Vec3 localA, B3Vec3 localB) {
        def.SetBodyIdA(a.GetId());
        def.SetBodyIdB(b.GetId());
        def.SetLocalPositionA(localA);
        def.SetLocalPositionB(localB);
        def.SetForceThreshold(FORCE_THRESHOLD);
        def.SetTorqueThreshold(TORQUE_THRESHOLD);
        def.SetCollideConnected(true);
    }

    private static void configure(B3RevoluteJointDef def, B3Body a, B3Body b, B3Vec3 localA, B3Vec3 localB) {
        def.SetBodyIdA(a.GetId());
        def.SetBodyIdB(b.GetId());
        def.SetLocalPositionA(localA);
        def.SetLocalPositionB(localB);
        def.SetForceThreshold(FORCE_THRESHOLD);
        def.SetTorqueThreshold(TORQUE_THRESHOLD);
        def.SetCollideConnected(true);
    }

    private static void configure(B3WeldJointDef def, B3Body a, B3Body b, B3Vec3 localA, B3Vec3 localB) {
        def.SetBodyIdA(a.GetId());
        def.SetBodyIdB(b.GetId());
        def.SetLocalPositionA(localA);
        def.SetLocalPositionB(localB);
        def.SetForceThreshold(FORCE_THRESHOLD);
        def.SetTorqueThreshold(TORQUE_THRESHOLD);
        def.SetCollideConnected(true);
    }
}
