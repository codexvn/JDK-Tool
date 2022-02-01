import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesTest {
    @Test
    void tempFile() throws IOException, InterruptedException {
        List<Path> paths = Files.walk(Path.of("C:\\Users\\codexvn\\.jdk\\"), 1).toList();
        boolean b = paths.size() == 2;
    }
}
