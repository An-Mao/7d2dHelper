package nws.dev.$7d2d.command;

import nws.dev.$7d2d.config.CommandConfig;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.SingInConfig;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.system._Log;

import java.util.List;

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
    public boolean groupMsg() {

        return switch (command) {
            case "同意解封" ->agreeUnban();
            case "签到" -> {
                if (CommandConfig.I.isEnable("sign")) yield  false;
                if (target.isEmpty()) {
                    sendMsg("指令格式错误，正确格式：签到 @qq");
                    yield  false;
                }
                UserConfig config = new UserConfig(this.qq);
                if (config.isBind()) {
                    UserConfig c = new UserConfig(target);
                    if (c.isBind()) {
                        SingInConfig singInConfig = new SingInConfig(this.qq);
                        String s = singInConfig.sign(config.getSteamID(), c.getSteamID());
                        _Log.debug(s);
                        sendMsg(s);
                    } else sendMsg("对方未绑定账号");
                } else sendMsg("未绑定账号，请先绑定账号");
                yield  true;
            }
            case "同意跟档" -> {
                if (isAdmin()) {
                    if (target.isEmpty()) {
                        sendMsg("指令格式错误，正确格式：同意跟档 @qq");
                        yield false;
                    }else {
                        UserConfig config = new UserConfig(this.target);
                        if (config.isBind()) {
                            config.getDatas().canExtractSaveItem = true;
                            config.save();
                            sendMsg("已同意跟档申请");
                        } else sendMsg("对方未绑定账号");
                    }
                }
                yield  true;
            }
            case "查看跟档物品" -> {
                if (isAdmin()) {
                    if (target.isEmpty()) {
                        sendMsg("指令格式错误，正确格式：查看跟档物品 @qq");
                        yield false;
                    }else {
                        UserConfig config = new UserConfig(this.target);
                        if (config.isBind()) {
                            QQUsualCommand.playerSaveItem.remove(this.qq);
                            QQUsualCommand.playerSaveItemIndex.remove(this.qq);
                            UserConfig.RecordItem recordItem = config.getRecordItem();
                            if (recordItem.getDatas().isEmpty()) {
                                sendMsg("对方没有跟档物品");
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
                                sendMsg(s.toString());
                            }
                        } else sendMsg("对方未绑定账号");
                    }
                }
                yield  true;
            }
            default -> false;
        };
    }
    private boolean agreeUnban() {
        if (CommandConfig.I.isEnable("agreeUnban")) return false;
        if (target.isEmpty()) {
            sendMsg("指令格式错误，正确格式：同意解封@qq");
            return false;
        }
        _Log.info("同意解封");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            _Log.debug("已绑定账号");

            if (QQUsualCommand.banUser.containsKey(target)) {
                if (target.equals(this.qq)) {
                    _Log.debug("相同玩家");
                    sendMsg( "您不能同意自己的解封申请");
                    return true;
                }
                List<String> count = QQUsualCommand.banUser.get(target);
                if (count.contains(this.qq)) {
                    _Log.debug("已同意过此玩家的解封申请");
                    sendMsg( "您已同意过此玩家的解封申请");
                    return true;
                }
                count.add(this.qq);
                QQUsualCommand.banUser.put(target, count);
                if (count.size() >= Config.I.getDatas().unBanNum) {
                    QQUsualCommand.banUser.remove(target);
                    sendMsg( "已满足解封所需人数，即将解除封禁");
                    UserConfig b = new UserConfig(target);
                    if (KitNet.unBan(b.getSteamID())) {
                        QQHelper.easySendGroupAtMsg(message.group_id, target, "解封成功");
                    } else {
                        QQHelper.easySendGroupAtMsg(message.group_id, target, "解封失败，网络异常");
                    }
                } else {
                    _Log.debug("未满足解封所需人数");
                    sendMsg( "您已同意此玩家的解封申请");
                }
            } else {
                _Log.debug("未找到此玩家");
                sendMsg( "未找到此封禁玩家");
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
}
