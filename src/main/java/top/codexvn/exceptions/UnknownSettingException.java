package top.codexvn.exceptions;

public class UnknownSettingException extends RuntimeException {
    public UnknownSettingException(String filedName, String fileName) {
        super(String.format("Unknown setting: %s in file %s: ", filedName, fileName));
    }
}
