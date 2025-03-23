package nws.dev.$7d2d.command;

import nws.dev.$7d2d.config.SingInConfig;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.system._Log;

public class QQAtCommand extends QQCommand {
    private final String command;
    private final String target;
    public QQAtCommand(QQData.Message message) {
        super(message);
        if (message.message.size() == 2) {
            QQData.Message.Msg msg = message.message.get(0);
            if (msg.type().equals("text") && msg.data().containsKey("text") ) {
                this.command = msg.data().get("text");
                msg = message.message.get(1);
                if (msg.type().equals("at") && msg.data().containsKey("qq")) {
                    this.target = msg.data().get("qq");
                }else this.target = "";
            }else {
                this.command = "";
                this.target = "";
            }
        }else {
            this.command = "";
            this.target = "";
        }
    }

    @Override
    public boolean check() {
        return switch (command) {
            case "签到" -> {
                if (target.isEmpty()) {
                    sendGroupReplyMsg("指令格式错误，正确格式：签到 @qq");
                    yield  false;
                }
                UserConfig config = new UserConfig(this.qq);
                if (config.isBind()) {
                    UserConfig c = new UserConfig(target);
                    if (c.isBind()) {
                        SingInConfig singInConfig = new SingInConfig(this.qq);
                        String s = singInConfig.sign(config.getSteamID(), c.getSteamID());
                        _Log.debug(s);
                        sendGroupReplyMsg(s);
                    } else sendGroupReplyMsg("对方未绑定账号");
                } else sendGroupReplyMsg("未绑定账号，请先绑定账号");
                yield  true;
            }
            case "同意跟档" -> {
                if (isAdmin()) {
                    if (target.isEmpty()) {
                        sendGroupReplyMsg("指令格式错误，正确格式：同意跟档 @qq");
                        yield false;
                    }else {
                        UserConfig config = new UserConfig(this.target);
                        if (config.isBind()) {
                            config.getDatas().canExtractSaveItem = true;
                            config.save();
                            sendGroupReplyMsg("已同意跟档申请");
                        } else sendGroupReplyMsg("对方未绑定账号");
                    }
                }
                yield  true;
            }
            case "查看跟档物品" -> {
                if (isAdmin()) {
                    if (target.isEmpty()) {
                        sendGroupReplyMsg("指令格式错误，正确格式：查看跟档物品 @qq");
                        yield false;
                    }else {
                        UserConfig config = new UserConfig(this.target);
                        if (config.isBind()) {
                            QQUsualCommand.playerSaveItem.remove(this.qq);
                            QQUsualCommand.playerSaveItemIndex.remove(this.qq);
                            UserConfig.RecordItem recordItem = config.getRecordItem();
                            if (recordItem.getDatas().isEmpty()) {
                                sendGroupReplyMsg("对方没有跟档物品");
                            }else {
                                StringBuilder s = new StringBuilder("对方跟档物品:");
                                int[] c = {0};
                                recordItem.getDatas().forEach((itemData) ->{
                                    if (c[0] < 20) {
                                        c[0]++;
                                        s.append("\\n").append(OtherHelper.removeColorCodes(itemData.n())).append(" x ").append(itemData.c());
                                    }
                                });
                                if (recordItem.getDatas().size() > 20){
                                    QQUsualCommand.playerSaveItem.put(this.qq,recordItem.getDatas());
                                    QQUsualCommand.playerSaveItemIndex.put(this.qq,20);
                                    s.append("\\n...\\n发送【下一页】继续查看");
                                }
                                sendGroupReplyMsg(s.toString());
                            }
                        } else sendGroupReplyMsg("对方未绑定账号");
                    }
                }
                yield  true;
            }
            default -> false;
        };
    }
}
