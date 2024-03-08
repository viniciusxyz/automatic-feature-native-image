package test;

import automatic.feature.commons.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

import java.io.Closeable;
import java.io.IOException;

@AutomaticFeature(generatePackage = "my.annotation.for.native.five")
public class FeatureFive implements Feature, Closeable {
    @Override
    public void close() throws IOException {
        System.out.println("Closing");
    }
}