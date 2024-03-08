package automatic.feature.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class AutomaticFeatureProcessorTest {

    private static final String NATIVE_ONE = "Args=--features=test.FeatureOne,test.FeatureTwo";

    private static final String NATIVE_TWO = "Args=--features=test.FeatureThree,test.FeatureFour";

    private static final String NATIVE_THREE = "Args=--features=test.FeatureFive";

    private static final File NATIVE_FILE_ONE = new File("target/classes/META-INF/native-image/test/native-image.properties");
    private static final File NATIVE_FILE_TWO = new File("target/classes/META-INF/native-image/my/annotation/for/native/native-image.properties");
    private static final File NATIVE_FILE_THREE = new File("target/classes/META-INF/native-image/my/annotation/for/native/five/native-image.properties");

    private final Map<File, String> mapTesting = new HashMap<>() {{
        put(NATIVE_FILE_ONE, NATIVE_ONE);
        put(NATIVE_FILE_TWO, NATIVE_TWO);
        put(NATIVE_FILE_THREE, NATIVE_THREE);
    }};

    @Test
    void shouldGenerateFileCorrectly() throws IOException {
        JavaFileObject featureOne = JavaFileObjects.forResource("test/FeatureOne.java");
        JavaFileObject featureTwo = JavaFileObjects.forResource("test/FeatureTwo.java");
        JavaFileObject featureThree = JavaFileObjects.forResource("test/FeatureThree.java");
        JavaFileObject featureFour = JavaFileObjects.forResource("test/FeatureFour.java");
        JavaFileObject featureFive = JavaFileObjects.forResource("test/FeatureFive.java");
        JavaFileObject withoutFeatureOne = JavaFileObjects.forResource("test/WithoutFeatureOne.java");
        Compilation compilation = javac()
                .withProcessors(new AutomaticFeatureProcessor())
                .compile(featureOne, featureTwo, featureThree, featureFour, featureFive, withoutFeatureOne);
        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningContaining("Resource not generated for class test.WithoutFeatureOne as it does not implement Feature interface");

        for (Map.Entry<File, String> fileData : mapTesting.entrySet()) {
            Assertions.assertThat(fileData.getKey()).exists();
            var readContent = new String(Files.readAllBytes(Path.of(fileData.getKey().toURI())));
            Assertions.assertThat(readContent).isEqualTo(fileData.getValue());
        }

    }
}
