package dev.anye.sdtd.helper.command;

import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

public abstract class QQUsualCommand extends QQCommand{
    private final String msg;
    public QQUsualCommand(String c, QQData.Message message, ServerCore serverCore){
        super(c,message,serverCore);
        this.msg = message.raw_message;
    }

    @Override
    public boolean runCommand() {
        return super.runCommand() && this.msg.equals(this.c);
    }
}
