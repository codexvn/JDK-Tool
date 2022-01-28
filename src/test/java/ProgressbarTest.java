import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProgressbarTest {
    @Test
    void name() throws InterruptedException {
        ProgressBarBuilder pbb = new ProgressBarBuilder()
            .setTaskName("下载")
            .setStyle(ProgressBarStyle.ASCII);
        for (var i : ProgressBar.wrap(List.of(1, 2, 3, 4, 5, 6), pbb)) {
            TimeUnit.SECONDS.sleep(1);  // simulate some work
        }
    }
}