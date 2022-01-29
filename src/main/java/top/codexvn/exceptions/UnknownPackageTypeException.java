package top.codexvn.exceptions;

public class UnknownPackageTypeException extends RuntimeException {
    public UnknownPackageTypeException(String message) {
        super(message);
    }

    public UnknownPackageTypeException() {
        super("Unknown package type");
    }
}
