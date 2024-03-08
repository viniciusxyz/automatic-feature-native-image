package test;

import io.github.viniciusxyz.automatic.feature.commons.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

@AutomaticFeature(generatePackage = "my.annotation.for.native")
public class FeatureThree implements Feature {
}
