package com.github.xpenatan.box3d.sample.gdx;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface Box3DGdxSampleBackend extends Disposable {
    Skin createSkin(String skinPath);

    Stage createStage(Viewport viewport);

    Drawable addUiDrawable(Skin skin, String fileName, String name, float minWidth, float minHeight);

    Box3DGdxSampleRenderer createRenderer();

    void clearScreen();

    void prepareUiRender();
}
