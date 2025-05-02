package nws.dev.$7d2d.register;

import nws.dev.$7d2d.command.QQCommand;

public class RegisterationFactory {
    private static final Registry<QQCommand> COMMAND_REGISTRY = new Registry<>();

    public static Registry<QQCommand> getCommandRegistry() {
        return COMMAND_REGISTRY;
    }
    public static void registerCommand(QQCommand command){
        COMMAND_REGISTRY.register(command);
    }
}
