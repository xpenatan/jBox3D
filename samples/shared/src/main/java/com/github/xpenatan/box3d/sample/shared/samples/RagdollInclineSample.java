package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene and timed motor transition from Ragdoll/Incline. */
final class RagdollInclineSample extends AbstractBox3DSample {
    private B3Mesh groundMesh;
    private ExactHuman human;
    private float time;
    private boolean motorized = true;

    RagdollInclineSample() {
        B3ShapeDef shapeDef = new B3ShapeDef();
        groundMesh = B3Mesh.CreateGrid(4, 4, 2.0f, 1, true);
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Quat inclineRotation = rotationZ(-0.2f * (float)Math.PI);
        B3Body incline = createBody(B3.StaticBody(), -10.0f, 2.0f, 0.0f, inclineRotation);
        dispose(incline.CreateMeshShape(shapeDef, groundMesh, unitScale), incline, inclineRotation);

        B3Body floor = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Vec3 floorScale = new B3Vec3(4.0f, 4.0f, 4.0f);
        dispose(floor.CreateMeshShape(shapeDef, groundMesh, floorScale), floorScale, floor,
                unitScale, shapeDef);

        human = ExactHuman.create(world(), -12.0f, 6.0f, 0.0f,
                10.0f, 2.0f, 0.7f, 1, false);
    }

    @Override
    public void step(float deltaSeconds) {
        if(time > 2.0f && motorized) {
            human.setJointFrictionTorque(0.5f);
            human.setJointSpringHertz(0.5f);
            motorized = false;
        }
        time += deltaSeconds;
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(groundMesh);
    }
}
