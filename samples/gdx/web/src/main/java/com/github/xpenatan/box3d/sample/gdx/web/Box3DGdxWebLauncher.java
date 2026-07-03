package com.github.xpenatan.box3d.sample.gdx.web;

import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import java.util.ArrayList;
import org.teavm.jso.JSBody;

public final class Box3DGdxWebLauncher {
    private Box3DGdxWebLauncher() {
    }

    public static void main(String[] args) {
        args = mergeArgs(args, queryArgs());
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=", "0"));
        applySampleOptions(args);

        WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        config.showDownloadLogs = true;
        config.useGL30 = true;

        new WebApplication(new Box3DGdxSampleApplication(exitAfterFrames), config);
    }

    private static void applySampleOptions(String[] args) {
        setPropertyFromOption(args, "--sample=", "jbox3d.sample.sample");
        setPropertyFromOption(args, "--sample-index=", "jbox3d.sample.sampleIndex");
        setPropertyFromOption(args, "--validate-all=", "jbox3d.sample.validateAll");
        setPropertyFromOption(args, "--auto-throw-after-frames=", "jbox3d.sample.autoThrowAfterFrames");
    }

    private static void setPropertyFromOption(String[] args, String prefix, String property) {
        String value = option(args, prefix, "");
        if(value.length() > 0) {
            System.setProperty(property, value);
        }
    }

    private static String option(String[] args, String prefix, String fallback) {
        if(args == null) {
            return fallback;
        }
        for(String arg : args) {
            if(arg != null && arg.startsWith(prefix)) {
                return arg.substring(prefix.length());
            }
        }
        return fallback;
    }

    private static String[] mergeArgs(String[] first, String[] second) {
        int firstLength = first != null ? first.length : 0;
        int secondLength = second != null ? second.length : 0;
        String[] result = new String[firstLength + secondLength];
        for(int i = 0; i < firstLength; i++) {
            result[i] = first[i];
        }
        for(int i = 0; i < secondLength; i++) {
            result[firstLength + i] = second[i];
        }
        return result;
    }

    private static String[] queryArgs() {
        String query = locationSearch();
        if(query == null || query.length() <= 1) {
            return new String[0];
        }
        ArrayList<String> args = new ArrayList<String>();
        String[] pairs = query.substring(1).split("&");
        for(int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            int split = pair.indexOf('=');
            String name = decode(split >= 0 ? pair.substring(0, split) : pair);
            String value = decode(split >= 0 ? pair.substring(split + 1) : "true");
            addQueryArg(args, name, value);
        }
        return args.toArray(new String[args.size()]);
    }

    private static void addQueryArg(ArrayList<String> args, String name, String value) {
        if("sample".equals(name)) {
            args.add("--sample=" + value);
        }
        else if("sampleIndex".equals(name) || "sample-index".equals(name)) {
            args.add("--sample-index=" + value);
        }
        else if("validateAll".equals(name) || "validate-all".equals(name)) {
            args.add("--validate-all=" + value);
        }
        else if("autoThrowAfterFrames".equals(name) || "auto-throw-after-frames".equals(name)) {
            args.add("--auto-throw-after-frames=" + value);
        }
        else if("exitAfterFrames".equals(name) || "exit-after-frames".equals(name)) {
            args.add("--exit-after-frames=" + value);
        }
    }

    @JSBody(params = {"value"}, script = "return decodeURIComponent(String(value).replace(/\\+/g, ' '));")
    private static native String decode(String value);

    @JSBody(script = "return typeof window !== 'undefined' && window.location ? window.location.search : '';")
    private static native String locationSearch();
}
