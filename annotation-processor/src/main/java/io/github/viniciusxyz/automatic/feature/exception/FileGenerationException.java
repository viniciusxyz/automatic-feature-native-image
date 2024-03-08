package io.github.viniciusxyz.automatic.feature.exception;

/**
 * Exception thrown when there is a problem creating directories or files
 */
public class FileGenerationException extends RuntimeException {
    public FileGenerationException(Exception e) {
        super(e);
    }
}
