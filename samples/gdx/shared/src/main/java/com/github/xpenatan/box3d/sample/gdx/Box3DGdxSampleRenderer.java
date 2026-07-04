package com.github.xpenatan.box3d.sample.gdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.github.xpenatan.box3d.B3DebugDrawEm;
import com.github.xpenatan.box3d.B3World;

public interface Box3DGdxSampleRenderer extends Disposable {
    void render(B3World world, Camera camera);

    void clearShapeCache();

    void setEnabled(boolean enabled);

    void setDrawSolidShapes(boolean drawSolidShapes);

    void setDrawWireframe(boolean drawWireframe);

    void setShadowsEnabled(boolean shadowsEnabled);

    void setShadowBias(float shadowBias);

    B3DebugDrawEm debugDraw();
}
