package com.github.xpenatan.box3d.sample.gdx.android;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleApplication;

public final class Box3DGdxAndroidActivity extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySampleOptions();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useGyroscope = false;
        config.useImmersiveMode = true;
        config.useWakelock = true;
        config.useGL30 = true;
        initialize(new Box3DGdxSampleApplication(exitAfterFrames()), config);
    }

    private void applySampleOptions() {
        Intent intent = getIntent();
        if(intent == null) {
            return;
        }
        setSystemPropertyFromExtra(intent, "jbox3d.sample.sample");
        setSystemPropertyFromExtra(intent, "jbox3d.sample.sampleIndex");
        setSystemPropertyFromExtra(intent, "jbox3d.sample.validateAll");
        setSystemPropertyFromExtra(intent, "jbox3d.sample.autoThrowAfterFrames");
        setSystemPropertyFromExtra(intent, "jbox3d.sample.openSamplesMenu");
    }

    private void setSystemPropertyFromExtra(Intent intent, String key) {
        String value = intent.getStringExtra(key);
        if(value != null && value.trim().length() > 0) {
            System.setProperty(key, value.trim());
        }
    }

    private long exitAfterFrames() {
        Intent intent = getIntent();
        String value = intent != null ? intent.getStringExtra("jbox3d.sample.exitAfterFrames") : null;
        if(value == null || value.trim().isEmpty()) {
            return 0L;
        }
        return Long.parseLong(value.trim());
    }
}
