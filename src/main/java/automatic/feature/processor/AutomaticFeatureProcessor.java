package automatic.feature.processor;

import automatic.feature.AutomaticFeature;
import automatic.feature.exception.FileGenerationException;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
public class AutomaticFeatureProcessor extends AbstractProcessor {

    private final String PATTERN_FILE = "Args=--features=%s";
    private final List<String> LOCATION_BASE = new ArrayList<>(Arrays.asList("target", "classes", "META-INF", "native-image"));
    private final String FILE_NAME = "native-image.properties";

    private final Map<String, List<String>> packageNativeFileMap = HashMap.newHashMap(1);

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(AutomaticFeature.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_21;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var elements = roundEnv.getElementsAnnotatedWith(AutomaticFeature.class);

        if (elements.isEmpty())
            return false;

        for (Element element : elements) {
            var nameClass = element.asType().toString();
            var elementPackage = packageNameAsFilePath(element);
            var listRef = packageNativeFileMap.get(elementPackage);
            if (listRef == null)
                packageNativeFileMap.put(elementPackage, new ArrayList<>(List.of(nameClass)));
            else
                listRef.add(nameClass);
        }

        for (Map.Entry<String, List<String>> entry : packageNativeFileMap.entrySet()) {
            var fileLocationPath = fileLocationPath(entry.getKey());
            var fileContent = fileContent(entry.getValue());
            generateNativeImagePropertiesFile(fileLocationPath, fileContent);
        }
        return false;
    }

    private String packageNameAsFilePath(Element element) {
        var packageName = element.getAnnotation(AutomaticFeature.class).generatePackage();
        if (packageName != null && !packageName.isEmpty())
            return packageName.replace(".", File.separator);

        return processingEnv.getElementUtils().getPackageOf(element).toString().replace(".", File.separator);
    }

    private String fileContent(List<String> classes) {
        var features = String.join(",", classes);
        return String.format(PATTERN_FILE, features);
    }

    private File fileLocationPath(String packageAsDirectory) {
        var basePath = String.join(File.separator, LOCATION_BASE).concat(File.separator).concat(packageAsDirectory);
        return new File(String.join(File.separator, basePath));
    }

    private File fileLocation(File basePath) {
        var fileDir = String.join(File.separator, basePath.getPath(), FILE_NAME);
        return new File(fileDir);
    }

    private void generateNativeImagePropertiesFile(File fileLocationPath, String fileContent) {
        try {
            if(!fileLocationPath.exists())
                fileLocationPath.mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation(fileLocationPath)))) {
                writer.write(fileContent);
            }
        } catch (IOException e) {
            throw new FileGenerationException(e);
        }
    }
}
