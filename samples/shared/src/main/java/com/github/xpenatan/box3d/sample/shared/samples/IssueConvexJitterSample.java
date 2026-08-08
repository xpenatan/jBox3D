package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;

/** Exact reproduction data from Issues/Convex Jitter. */
final class IssueConvexJitterSample extends AbstractBox3DSample {
    private static final float SCALE = 0.01f;

    IssueConvexJitterSample() {
        dispose(addGroundBox(10.0f));

        B3Quat staticRotation = new B3Quat(0.0f, -0.707106769f, 0.0f, 0.707106769f);
        B3Body staticBody = createBody(B3.StaticBody(), SCALE * -459.292877f,
                SCALE * 1.00115335f + 2.0f, SCALE * 217.398331f, staticRotation);
        B3Hull staticHull = transformedHull(new float[][] {
                { -44.8770714f, -91.6598053f, -1.92012548f },
                { -92.5001831f, 51.0151291f, 15.8006573f },
                { -91.0282211f, -9.44371605f, 15.6148796f },
                { 90.2375641f, 77.3870087f, 15.9356089f },
                { -85.5353241f, 91.3750992f, -1.36629653f },
                { 88.9092178f, -87.2975464f, -1.86754704f },
                { 83.7932816f, -89.8572235f, 15.4168339f },
                { 87.0243988f, 88.9776535f, -1.32423306f },
                { -91.6564941f, -85.4949493f, 15.3782759f },
                { -90.2922516f, -87.2074127f, -1.92012548f },
                { -87.2944870f, 89.9510498f, 15.9215889f },
                { 79.2338104f, 89.9690781f, 15.9724140f },
                { -91.6744461f, 81.0823212f, -1.39959598f },
                { 90.3452759f, -76.4459610f, 15.4588966f },
                { -87.4021912f, -89.2263107f, 15.3677588f },
                { 76.3258057f, 92.0059967f, 1.82873762f }
        });
        addHullShape(staticBody, staticHull, 1.0f, 0.6f, 0.0f, 0.0f);

        B3Quat dynamicRotation = new B3Quat(0.0f, -0.00152086187f, 0.0f, 0.999998868f);
        B3Body dynamicBody = createBody(B3.DynamicBody(), SCALE * -402.321838f,
                SCALE * 16.8169250f + 2.0f, SCALE * 157.310364f, dynamicRotation);
        B3Hull dynamicHull = transformedHull(new float[][] {
                { 29.5000000f, 17.1488495f, 0.175081104f },
                { 29.5000000f, -17.2990532f, 0.125000000f },
                { 29.4840164f, -17.3057766f, 24.0200863f },
                { 29.4840164f, 17.1648350f, 24.1781254f },
                { -29.1345520f, 17.5529804f, 0.125000000f },
                { -29.1345520f, 17.5529804f, 23.7899799f },
                { -29.1441040f, 16.9679585f, 24.3750000f },
                { -29.1345520f, -17.2990532f, 24.3750000f },
                { -29.1345520f, -17.2990532f, 0.175081253f },
                { 29.0720215f, 17.5529785f, 0.125000000f },
                { 29.0859070f, 17.5629406f, 23.8120594f },
                { 29.1401348f, -17.2990532f, 24.3750000f },
                { 29.1123581f, 16.9722290f, 24.4027710f },
                { 29.3944912f, 17.2543602f, 24.1206398f },
                { -29.1345520f, -17.2990532f, 24.0759430f },
                { -29.1345520f, -16.9722252f, 24.4027710f },
                { 29.1123619f, -16.9722271f, 24.4027729f },
                { 29.5000000f, 17.3429642f, 24.0000000f }
        });
        addHullShape(dynamicBody, dynamicHull, 1.0f, 0.6f, 0.0f, 0.1f);

        dispose(dynamicHull, dynamicBody, dynamicRotation, staticHull, staticBody, staticRotation);
    }

    private B3Hull transformedHull(float[][] source) {
        float[][] points = new float[source.length][3];
        for(int i = 0; i < source.length; i++) {
            points[i][0] = SCALE * source[i][0];
            points[i][1] = SCALE * source[i][2];
            points[i][2] = SCALE * source[i][1];
        }
        return createHull(points);
    }
}
