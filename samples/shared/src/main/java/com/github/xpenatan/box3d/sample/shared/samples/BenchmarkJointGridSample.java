package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of {@code CreateJointGrid} in {@code shared/benchmarks.c}. */
final class BenchmarkJointGridSample extends AbstractBox3DSample {
    BenchmarkJointGridSample() {
        world().EnableSleeping(false);
        int n = 100;
        long[] bodies = new long[n * n];
        int index = 0;

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Filter filter = shapeDef.GetFilter();
        filter.SetCategoryBits(2L);
        filter.SetMaskBits(0xFFFF_FFFDL);
        shapeDef.SetFilter(filter);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.4f);
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        B3Vec3 localA = new B3Vec3();
        B3Vec3 localB = new B3Vec3();
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetEnableSleep(false);
        B3Vec3 position = new B3Vec3();

        for(int k = 0; k < n; ++k) {
            for(int i = 0; i < n; ++i) {
                bodyDef.SetType(i == 0 ? B3.StaticBody() : B3.DynamicBody());
                position.Set(k, -i, 0.0f);
                bodyDef.SetPosition(position);
                B3Body body = world().CreateBody(bodyDef);
                long bodyId = body.GetId();
                dispose(body.CreateSphereShape(shapeDef, sphere));

                if(i > 0) {
                    jointDef.SetBodyIdA(bodies[index - 1]);
                    jointDef.SetBodyIdB(bodyId);
                    localA.Set(0.0f, -0.5f, 0.0f);
                    localB.Set(0.0f, 0.5f, 0.0f);
                    jointDef.SetLocalPositionA(localA);
                    jointDef.SetLocalPositionB(localB);
                    dispose(world().CreateSphericalJoint(jointDef));
                }
                if(k > 0) {
                    jointDef.SetBodyIdA(bodies[index - n]);
                    jointDef.SetBodyIdB(bodyId);
                    localA.Set(0.5f, 0.0f, 0.0f);
                    localB.Set(-0.5f, 0.0f, 0.0f);
                    jointDef.SetLocalPositionA(localA);
                    jointDef.SetLocalPositionB(localB);
                    dispose(world().CreateSphericalJoint(jointDef));
                }
                bodies[index++] = bodyId;
                dispose(body);
            }
        }
        dispose(position, bodyDef, localB, localA, jointDef, sphere, center, filter, shapeDef);
    }
}
