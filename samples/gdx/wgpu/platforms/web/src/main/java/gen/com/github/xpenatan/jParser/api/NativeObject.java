package gen.com.github.xpenatan.jParser.api;

public class NativeObject extends gen.web.com.github.xpenatan.jParser.api.NativeObject {
    public static final NativeObject NULL = native_new();

    public static NativeObject native_new() {
        return new NativeObject((byte)0, (char)0);
    }

    public NativeObject() {
        super();
    }

    @Deprecated
    public NativeObject(byte b, char c) {
        super(b, c);
    }
}
