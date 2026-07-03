package com.github.xpenatan.box3d;

import com.github.xpenatan.jParser.loader.JParserLibraryLoader;
import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;
import com.github.xpenatan.jparser.runtime.RuntimeLoader;

public class JBox3DLoader {

    /*[-JNI;-NATIVE]
        #include "jBox3D.h"
    */

    /*[-FFM;-NATIVE]
        #include "jBox3D.h"
    */

    public static void init(JParserLibraryLoaderListener listener) {
        RuntimeLoader.init(new JParserLibraryLoaderListener() {
            @Override
            public void onLoad(boolean idl_isSuccess, Throwable idl_t) {
                if(idl_isSuccess) {
                    JParserLibraryLoader.load("box3d", listener);
                }
                else {
                    listener.onLoad(false, idl_t);
                }
            }
        });
    }
}
