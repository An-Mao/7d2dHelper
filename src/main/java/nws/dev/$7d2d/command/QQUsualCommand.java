package nws.dev.$7d2d.command;

import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

public abstract class QQUsualCommand extends QQCommand{
    private final String msg;
    public QQUsualCommand(String c, QQData.Message message, ServerCore serverCore){
        super(c,message,serverCore);
        this.msg = message.raw_message;
    }
}
