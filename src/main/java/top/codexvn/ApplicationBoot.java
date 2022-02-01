package top.codexvn;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import top.codexvn.command.*;

@Command(
    name="jtl",
    subcommands = {
        ListCommand.class,
        InstallCommand.class,
        UpgradeCommand.class,
        UpdateCommand.class,
        HelpCommand.class,
    }
)
public class ApplicationBoot {
    public static void main(String... args) {
        int exitCode = new CommandLine(new ApplicationBoot())
            .execute(args);
        System.exit(exitCode);
    }
}