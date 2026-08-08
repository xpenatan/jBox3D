package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Compound;
import com.github.xpenatan.box3d.B3CompoundDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

final class CompoundSpheresSample extends AbstractBox3DSample {
    private static final int COUNT = 20;

    private B3Compound compound;

    CompoundSpheresSample() {
        SampleRandom random = new SampleRandom();
        B3CompoundDef compoundDef = new B3CompoundDef(0, 0, 0, COUNT);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        for(int i = 0; i < COUNT; i++) {
            B3Vec3 center = new B3Vec3(random.nextFloat(-10.0f, 10.0f), random.nextFloat(-10.0f, 10.0f),
                    random.nextFloat(-10.0f, 10.0f));
            B3Sphere sphere = new B3Sphere(center, random.nextFloat(0.1f, 0.5f));
            compoundDef.AddSphere(sphere, material);
            dispose(sphere, center);
        }
        compound = B3Compound.CreateFromDef(compoundDef);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateBakedCompoundShape(shapeDef, compound));
        dispose(shapeDef, material, compoundDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(compound);
    }
}
