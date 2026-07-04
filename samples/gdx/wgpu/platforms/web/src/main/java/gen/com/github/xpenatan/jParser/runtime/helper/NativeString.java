package gen.com.github.xpenatan.jparser.runtime.helper;

public class NativeString extends gen.web.com.github.xpenatan.jparser.runtime.helper.NativeString {
    public static final NativeString NULL = native_new();

    public static NativeString native_new() {
        return new NativeString((byte)1, (char)1);
    }

    public NativeString() {
        super();
    }

    protected NativeString(byte b, char c) {
        super(b, c);
    }
}
