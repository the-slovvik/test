package pl.slovvik.logprocessor;

public enum FileSize {

    GB(1024 * 1024 * 1024),
    MB(1024 * 1024);

    private final long size;

    FileSize(long size) {
        this.size = size;
    }

    long calculateSize(int size) {
        return this.size * size;
    }
}
