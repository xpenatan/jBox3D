package com.github.xpenatan.box3d.sample.shared.samples;

public final class SampleAssets {
    public interface Reader {
        String readUtf8(String path);
    }

    private static Reader reader;

    private SampleAssets() {
    }

    public static void setReader(Reader value) {
        if(value == null) {
            throw new IllegalArgumentException("Sample asset reader cannot be null");
        }
        reader = value;
    }

    static String readUtf8(String path) {
        if(reader == null) {
            throw new IllegalStateException("Sample asset reader is not configured");
        }
        return reader.readUtf8(path);
    }
}
