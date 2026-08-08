package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Compound;
import com.github.xpenatan.box3d.B3CompoundDef;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3SurfaceMaterialArray;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

final class CompoundMeshTileSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 2;
    private static final float HALF_WIDTH = 4.0f;

    private B3Compound compound;

    CompoundMeshTileSample() {
        SampleRandom random = new SampleRandom();
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 extents = new B3Vec3(HALF_WIDTH, 0.5f * HALF_WIDTH, HALF_WIDTH);
        B3Mesh box = B3Mesh.CreateBox(center, extents, true);
        B3SurfaceMaterialArray materials = new B3SurfaceMaterialArray(1);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        materials.SetValue(0, material);
        B3CompoundDef compoundDef = new B3CompoundDef(0, 0, GRID_COUNT * GRID_COUNT, 0);
        B3Quat rotation = new B3Quat();
        B3Vec3 position = new B3Vec3();
        B3Transform transform = new B3Transform(position, rotation);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        for(int i = 0; i < GRID_COUNT; i++) {
            for(int j = 0; j < GRID_COUNT; j++) {
                B3Vec3 tilePosition = new B3Vec3((2.0f * i - GRID_COUNT) * HALF_WIDTH,
                        random.nextFloat(-0.5f, 0.25f) * HALF_WIDTH,
                        (2.0f * j - GRID_COUNT) * HALF_WIDTH);
                transform.SetP(tilePosition);
                compoundDef.AddMesh(box, transform, scale, materials);
                dispose(tilePosition);
            }
        }
        compound = B3Compound.CreateFromDef(compoundDef);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateBakedCompoundShape(shapeDef, compound));
        dispose(shapeDef, scale, transform, position, rotation, compoundDef, material, materials, box, extents,
                center);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(compound);
    }
}
