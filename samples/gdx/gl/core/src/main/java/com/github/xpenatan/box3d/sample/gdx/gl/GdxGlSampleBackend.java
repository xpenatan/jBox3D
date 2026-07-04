package com.github.xpenatan.box3d.sample.gdx.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.xpenatan.box3d.gdx.gl.GdxGlDebugRenderer;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleBackend;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleRenderer;

public final class GdxGlSampleBackend implements Box3DGdxSampleBackend {
    @Override
    public Skin createSkin(String skinPath) {
        return new Skin(Gdx.files.internal(skinPath));
    }

    @Override
    public Stage createStage(Viewport viewport) {
        return new Stage(viewport);
    }

    @Override
    public Drawable addUiDrawable(Skin skin, String fileName, String name, float minWidth, float minHeight) {
        Texture texture = new Texture(Gdx.files.internal("data/ui/" + fileName + ".png"));
        skin.add(name + "-texture", texture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        drawable.setMinWidth(minWidth);
        drawable.setMinHeight(minHeight);
        skin.add(name, drawable, Drawable.class);
        return drawable;
    }

    @Override
    public Box3DGdxSampleRenderer createRenderer() {
        return new GdxGlSampleRenderer(new GdxGlDebugRenderer());
    }

    @Override
    public void clearScreen() {
        Gdx.gl.glClearColor(0.04f, 0.045f, 0.06f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    @Override
    public void prepareUiRender() {
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    @Override
    public void dispose() {
    }
}
