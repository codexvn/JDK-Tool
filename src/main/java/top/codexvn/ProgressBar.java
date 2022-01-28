package top.codexvn;

public interface ProgressBar {
    default void start() {
    }

    default void progress(long totalSize,long progressSize) {
    }

    default void finish() {
    }
}
