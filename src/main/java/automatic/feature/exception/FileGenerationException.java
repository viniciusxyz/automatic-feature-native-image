package automatic.feature.exception;

public class FileGenerationException extends RuntimeException {
    public FileGenerationException(Exception e) {
        super(e);
    }
}
