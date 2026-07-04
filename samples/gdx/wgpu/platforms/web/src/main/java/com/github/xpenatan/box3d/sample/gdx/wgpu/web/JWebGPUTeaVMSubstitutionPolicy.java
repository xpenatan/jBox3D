package com.github.xpenatan.box3d.sample.gdx.wgpu.web;

import org.teavm.extension.spi.substitution.SubstitutionPolicy;
import org.teavm.extension.spi.substitution.SubstitutionSink;

public class JWebGPUTeaVMSubstitutionPolicy implements SubstitutionPolicy {
    @Override
    public void contribute(SubstitutionSink sink) {
        sink.substitutePackage("com.github.xpenatan.webgpu", "gen.com.github.xpenatan.webgpu");
    }
}
