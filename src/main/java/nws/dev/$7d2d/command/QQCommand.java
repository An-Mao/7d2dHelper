package nws.dev.$7d2d.command;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.server.ServerCore;

public abstract class QQCommand implements ICommandCore {
    protected final String c;
    protected final QQData.Message message;
    protected final String qq;
    protected final String group;
    protected final String msgId;
    protected final boolean isGroup;
    protected final String msg;

    protected final ServerCore server;

    public QQCommand(String c, QQData.Message message,ServerCore serverCore) {
        this.c = c;
        this.message = message;
        this.qq = message.user_id;
        this.isGroup = !message.message_type.equals("private");

        this.group = message.group_id;
        this.msgId = message.message_id;
        if (this.message.message.size() == 1 && this.message.message.get(0).type().equals("text")) {
            this.msg = this.message.message.get(0).data().get("text");
        }else {
            this.msg = "";
        }
        this.server = serverCore;
    }

    public void sendFormatMsg(String key, Object... objects){
        String msg = server.getTranslate(key);
        if (msg.isEmpty()) return;
        if (isGroup && isEnableGroup()) {
            $7DTD._Log.debug("发送群聊消息");
            QQHelper.easySendGroupReplyMsg(server,this.group, msgId, String.format(msg,objects));
        }else {
            $7DTD._Log.debug("发送私聊消息");
            QQHelper.easySendMsg(this.qq, String.format(msg,objects));
        }

    }

    public void sendMsg(String msg) {
        sendFormatMsg(msg);
        /*
        if (isGroup && isEnableGroup()) {
            $7DTD._Log.debug("发送群聊消息");
            QQHelper.easySendGroupReplyMsg(server,this.group, msgId, msg);
        }else {
            $7DTD._Log.debug("发送私聊消息");
            QQHelper.easySendMsg(this.qq, msg);
        }

         */
    }
    public void sendAtMsg(String qq,String msg,Object... args){
        QQHelper.easySendGroupAtMsg(message.group_id, qq, String.format(server.getTranslate(msg),args));
    }

    @Override
    public boolean runCommand() {
        if (server == null) return false;
        if (c.isEmpty() || !server.commandIsEnable(this.c))return false;
        if (isGroup && isEnableGroup())return groupMsg();
        return privateMsg();
    }

    public boolean isEnableGroup() {
        return server.serverData.qqGroup().contains(message.group_id);
    }


    abstract public boolean groupMsg();
    abstract public boolean privateMsg();


    public boolean isAdmin() {
        return server.serverData.adminQQ().contains(this.qq);
    }



}
