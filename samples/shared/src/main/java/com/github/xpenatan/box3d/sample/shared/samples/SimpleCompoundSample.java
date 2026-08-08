package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Compound;
import com.github.xpenatan.box3d.B3CompoundDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

final class SimpleCompoundSample extends AbstractBox3DSample {
    private B3Compound compound;

    SimpleCompoundSample() {
        B3CompoundDef compoundDef = new B3CompoundDef(0, 1, 0, 0);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        B3Hull box = B3Hull.CreateBox(4.0f, 0.5f, 4.0f);
        B3Vec3 localPosition = new B3Vec3(1.0f, -0.5f, 0.0f);
        B3Quat localRotation = new B3Quat();
        B3Transform localTransform = new B3Transform(localPosition, localRotation);
        compoundDef.AddHull(box, localTransform, material);
        compound = B3Compound.CreateFromDef(compoundDef);

        B3Quat bodyRotation = rotationY(0.25f * (float)Math.PI);
        B3BodyDef bodyDef = bodyDef(B3.StaticBody(), 2.0f, -1.0f, 0.0f, bodyRotation);
        B3Body ground = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateBakedCompoundShape(shapeDef, compound));
        world().SetContactRecycleDistance(0.0f);

        addDynamicSphere(0.0f, 2.0f, 0.0f, 0.25f);
        dispose(shapeDef, bodyDef, bodyRotation, localTransform, localRotation, localPosition, box, material,
                compoundDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(compound);
    }
}
