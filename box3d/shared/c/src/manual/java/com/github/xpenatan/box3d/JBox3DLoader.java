package com.github.xpenatan.box3d;

import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;

public final class JBox3DLoader {
    private JBox3DLoader() {
    }

    public static void init(JParserLibraryLoaderListener listener) {
        if(listener != null) {
            listener.onLoad(true, null);
        }
    }
}
