# Automatic Feature Processor

## Project's goal

This project aims to facilitate the creation of native-image.properties files for projects that used the graalvm @AutomaticFeature annotation that was deprecated.

## How to use?

The project is made up of a dependency that basically contains the @AutomaticFeature annotation and an annotation-processor that generates the native-image.properties file for the specified classes. Let's look at an example of use:

1. Add the dependency below to your pom.xml

```xml
<dependency>
    <groupId>vvsantos.automatic.feature</groupId>
    <artifactId>commons</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

2. Add annotation processor to your build configuration

_use [this link as example](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html#annotationProcessorPaths)_

```xml

<path>
    <groupId>vvsantos.automatic.feature</groupId>
    <artifactId>annotation-processor</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</path>
```

3. Add the @AutomaticFeature annotation to your classes that implement graalvm's Feature interface

```java
package test;

import automatic.feature.commons.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

@AutomaticFeature
public class FeatureTwo implements Feature {
}
```

4. Compile your project and enjoy automatic feature registration

## Customizing output directory

By default, the output directory of the native-image.properties file will be target/classes/META-INF/native-image/{package of the annotated class}, but this can be customized by adding the generatePackage attribute to the annotation, as shown in the example below:

```java
package test;

import automatic.feature.commons.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

@AutomaticFeature(generatePackage = "my.annotation.for.native")
public class FeatureThree implements Feature {
}
```

Checklist

- [x] Separate processor and annotations
- [x] Verify if classes with annotations extends graalvm feature
- [ ] Create complete documentation
- [ ] Add Javadoc
- [ ] Publish to Maven Central
- [ ] Add Sonar 
- [ ] Add license info