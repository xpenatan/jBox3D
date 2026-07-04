package com.github.xpenatan.box3d.sample.gdx.wgpu;

import com.badlogic.gdx.graphics.Camera;
import com.github.xpenatan.box3d.B3DebugDrawEm;
import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.box3d.gdx.wgpu.GdxWgpuDebugRenderer;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleRenderer;

public final class GdxWgpuSampleRenderer implements Box3DGdxSampleRenderer {
    private final GdxWgpuDebugRenderer renderer;

    public GdxWgpuSampleRenderer(GdxWgpuDebugRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void render(B3World world, Camera camera) {
        renderer.render(world, camera);
    }

    @Override
    public void clearShapeCache() {
        renderer.clearShapeCache();
    }

    @Override
    public void setEnabled(boolean enabled) {
        renderer.setEnabled(enabled);
    }

    @Override
    public void setDrawSolidShapes(boolean drawSolidShapes) {
        renderer.setDrawSolidShapes(drawSolidShapes);
    }

    @Override
    public void setDrawWireframe(boolean drawWireframe) {
        renderer.setDrawWireframe(drawWireframe);
    }

    @Override
    public void setShadowsEnabled(boolean shadowsEnabled) {
        renderer.setShadowsEnabled(shadowsEnabled);
    }

    @Override
    public void setShadowBias(float shadowBias) {
        renderer.setShadowBias(shadowBias);
    }

    @Override
    public B3DebugDrawEm debugDraw() {
        return renderer;
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
