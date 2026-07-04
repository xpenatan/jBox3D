package com.github.xpenatan.box3d.sample.gdx.wgpu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.xpenatan.box3d.gdx.wgpu.GdxWgpuDebugRenderer;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleBackend;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleRenderer;
import com.monstrous.gdx.webgpu.graphics.WgTexture;
import com.monstrous.gdx.webgpu.graphics.utils.WgScreenUtils;
import com.monstrous.gdx.webgpu.scene2d.WgSkin;
import com.monstrous.gdx.webgpu.scene2d.WgStage;
import com.monstrous.gdx.webgpu.wrappers.RenderPassBuilder;
import com.monstrous.gdx.webgpu.wrappers.WebGPURenderPass;

public final class GdxWgpuSampleBackend implements Box3DGdxSampleBackend {
    @Override
    public Skin createSkin(String skinPath) {
        return new WgSkin(Gdx.files.internal(skinPath));
    }

    @Override
    public Stage createStage(Viewport viewport) {
        return new WgStage(viewport);
    }

    @Override
    public Drawable addUiDrawable(Skin skin, String fileName, String name, float minWidth, float minHeight) {
        WgTexture texture = new WgTexture(Gdx.files.internal("data/ui/" + fileName + ".png"));
        skin.add(name + "-texture", texture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        drawable.setMinWidth(minWidth);
        drawable.setMinHeight(minHeight);
        skin.add(name, drawable, Drawable.class);
        return drawable;
    }

    @Override
    public Box3DGdxSampleRenderer createRenderer() {
        return new GdxWgpuSampleRenderer(new GdxWgpuDebugRenderer());
    }

    @Override
    public void clearScreen() {
        WgScreenUtils.clear(0.04f, 0.045f, 0.06f, 1.0f, true);
    }

    @Override
    public void prepareUiRender() {
        WebGPURenderPass pass = RenderPassBuilder.create("box3d-ui-depth-clear", null, true);
        pass.end();
    }

    @Override
    public void dispose() {
    }
}
