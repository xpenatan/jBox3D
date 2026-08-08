package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Compound;
import com.github.xpenatan.box3d.B3CompoundDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

final class CompoundHullsSample extends AbstractBox3DSample {
    private static final int COUNT = 20;

    private B3Compound compound;

    CompoundHullsSample() {
        SampleRandom random = new SampleRandom();
        B3CompoundDef compoundDef = new B3CompoundDef(0, COUNT, 0, 0);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        B3Hull[] hulls = new B3Hull[COUNT];
        for(int i = 0; i < COUNT; i++) {
            float extentX = random.nextFloat(0.1f, 0.5f);
            float extentY = random.nextFloat(0.1f, 0.5f);
            float extentZ = random.nextFloat(0.1f, 0.5f);
            B3Vec3 position = new B3Vec3(random.nextFloat(-10.0f, 10.0f), random.nextFloat(-10.0f, 10.0f),
                    random.nextFloat(-10.0f, 10.0f));
            B3Quat rotation = random.nextQuaternion();
            B3Transform transform = new B3Transform(position, rotation);
            hulls[i] = B3Hull.CreateBox(extentX, extentY, extentZ);
            compoundDef.AddHull(hulls[i], transform, material);
            dispose(transform, rotation, position);
        }
        compound = B3Compound.CreateFromDef(compoundDef);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateBakedCompoundShape(shapeDef, compound));
        for(B3Hull hull : hulls) {
            dispose(hull);
        }
        dispose(shapeDef, material, compoundDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(compound);
    }
}
