# Automatic Feature Processor

## Project's goal

This project aims to facilitate the creation of native-image.properties files for projects that used the graalvm @AutomaticFeature annotation that was deprecated.

## How to use?

The project is made up of a dependency that basically contains the @AutomaticFeature annotation and an annotation-processor that generates the native-image.properties file for the specified classes. Let's look at an example of use:

1. Add the dependency below to your pom.xml

```xml
<dependency>
    <groupId>io.github.viniciusxyz.automatic.feature</groupId>
    <artifactId>commons</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. Add annotation processor to your build configuration

_use [this link as example](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html#annotationProcessorPaths)_

```xml

<path>
    <groupId>io.github.viniciusxyz.automatic.feature</groupId>
    <artifactId>annotation-processor</artifactId>
    <version>1.0.0</version>
</path>
```

3. Add the @AutomaticFeature annotation to your classes that implement graalvm's Feature interface

```java
package test;

import io.github.viniciusxyz.automatic.feature.commons.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

@AutomaticFeature
public class FeatureTwo implements Feature {
}
```

4. Compile your project and a directory that uses the same path as the class package will be created within /META-INF/native-image.properties and the contents of the file will have the feature declaration.
For the class described in item 3, for example, the following path will be generated:

```
/META-INF/native-image/test/native-image.properties
```

With the following content:

```properties
Args=--features=test.FeatureTwo
```

5. Enjoy :)

## Customizing output directory

By default, the output directory of the native-image.properties file will be target/classes/META-INF/native-image/{package of the annotated class}, but this can be customized by adding the generatePackage attribute to the annotation, as shown in the example below:

```java
package test;

import io.github.viniciusxyz.automatic.feature.commons.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;

@AutomaticFeature(generatePackage = "my.annotation.for.native")
public class FeatureThree implements Feature {
}
```