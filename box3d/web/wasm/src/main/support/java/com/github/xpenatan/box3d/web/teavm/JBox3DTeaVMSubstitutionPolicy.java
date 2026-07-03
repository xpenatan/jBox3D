package com.github.xpenatan.box3d.web.teavm;

import org.teavm.extension.spi.substitution.SubstitutionPolicy;
import org.teavm.extension.spi.substitution.SubstitutionSink;

public class JBox3DTeaVMSubstitutionPolicy implements SubstitutionPolicy {
    @Override
    public void contribute(SubstitutionSink sink) {
        sink.substitutePackage("com.github.xpenatan.jParser.api", "gen.web.com.github.xpenatan.jParser.api");
        sink.substitutePackage("com.github.xpenatan.box3d", "gen.web.com.github.xpenatan.box3d");
    }
}
