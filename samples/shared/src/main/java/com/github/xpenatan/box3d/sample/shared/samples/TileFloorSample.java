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

final class TileFloorSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 50;
    private static final float HALF_WIDTH = 4.0f;

    private B3Compound compound;

    TileFloorSample() {
        SampleRandom random = new SampleRandom();
        B3CompoundDef compoundDef = new B3CompoundDef(0, GRID_COUNT * GRID_COUNT, 0, 0);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        B3Hull tile = B3Hull.CreateBox(HALF_WIDTH, 0.5f * HALF_WIDTH, HALF_WIDTH);
        B3Quat rotation = new B3Quat();
        B3Vec3 position = new B3Vec3();
        B3Transform transform = new B3Transform(position, rotation);
        for(int i = 0; i < GRID_COUNT; i++) {
            for(int j = 0; j < GRID_COUNT; j++) {
                B3Vec3 tilePosition = new B3Vec3((2.0f * i - GRID_COUNT) * HALF_WIDTH,
                        random.nextFloat(-0.5f, 0.25f) * HALF_WIDTH,
                        (2.0f * j - GRID_COUNT) * HALF_WIDTH);
                transform.SetP(tilePosition);
                compoundDef.AddHull(tile, transform, material);
                dispose(tilePosition);
            }
        }
        compound = B3Compound.CreateFromDef(compoundDef);
        B3BodyDef bodyDef = bodyDef(B3.StaticBody(), -2.0f, 1.0f, -3.0f, null);
        B3Body ground = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateBakedCompoundShape(shapeDef, compound));
        addDynamicSphere(3.0f, 12.0f, 0.0f, 0.25f);
        dispose(shapeDef, bodyDef, transform, position, rotation, tile, material, compoundDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(compound);
    }
}
