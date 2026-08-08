package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact release configuration from shared/benchmarks.c CreateConvexPile. */
final class BenchmarkConvexPileSample extends AbstractBox3DSample {
    BenchmarkConvexPileSample() {
        dispose(addGroundBox(250.0f));

        ConvexPileRandom random = new ConvexPileRandom(42);
        B3Vec3Array points = new B3Vec3Array(32);
        for(int i = 0; i < 32; i++) {
            float x;
            float y;
            float z;
            float lengthSquared;
            do {
                x = random.nextFloat();
                y = random.nextFloat();
                z = random.nextFloat();
                lengthSquared = x * x + y * y + z * z;
            }
            while(lengthSquared > 0.25f);
            float inverseLength = 2.0f / (float)Math.sqrt(lengthSquared);
            B3Vec3 point = new B3Vec3(x * inverseLength, y * inverseLength, z * inverseLength);
            points.SetValue(i, point);
            dispose(point);
        }
        B3Hull convex = B3Hull.CreateFromPoints(points, 32);
        dispose(points);

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        for(int layer = 0; layer < 80; layer++) {
            for(int z = 0; z < 8; z++) {
                for(int x = 0; x < 8; x++) {
                    B3Vec3 position = new B3Vec3((x - 4.0f) * 4.0f, 2.0f + 4.0f * layer,
                            (z - 4.0f) * 4.0f);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    dispose(body.CreateHullShape(shapeDef, convex), body, position);
                }
            }
        }
        dispose(shapeDef, bodyDef, convex);
    }

    private static final class ConvexPileRandom {
        private int state;

        private ConvexPileRandom(int state) {
            this.state = state;
        }

        private float nextFloat() {
            state = state * 2147001325 + 715136305;
            return (state & 0xffff) / 65535.0f - 0.5f;
        }
    }
}
