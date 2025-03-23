package nws.dev.$7d2d.command;

import nws.dev.$7d2d.config.ACItemsConfig;
import nws.dev.$7d2d.config.ACItemsData;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.ACNet;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.system._Log;

import java.util.ArrayList;
import java.util.List;

public class QQExCommand extends QQCommand {
    private final String command;
    private final String[] args;

    public QQExCommand(QQData.Message message) {
        super(message);
        if (message.raw_message.contains(" ")) {
            this.args = message.raw_message.split(" ");
            this.command = this.args[0];

        } else {
            this.args = new String[0];
            this.command = "";
        }
    }

    @Override
    public boolean check() {
        if (command.isEmpty()) return false;
        return switch (command) {
            case "领取礼包" -> {
                if (argCheck(2)) {
                    sendGroupReplyMsg("指令格式错误，正确格式：领取礼包 礼包名称");
                    yield false;
                }
                _Log.info("领取礼包");
                UserConfig config = new UserConfig(this.qq);
                if (config.isBind()) {
                    String r = args[1];
                    _Log.debug("已绑定账号");
                    if (config.isReward(r))
                        sendGroupReplyMsg("您已领取过【" + r + "】礼包");
                    else {
                        sendGroupReplyMsg( BotNet.giveReward(config, r));
                    }
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg("未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "查信息" -> {
                if (argCheck(2)) {
                    sendGroupReplyMsg("指令格式错误，正确格式：查信息 玩家名称");
                    yield false;
                }
                _Log.info("查看玩家信息");
                BotData.PlayerInfo info = BotNet.getOnlinePlayerByName(args[1]);
                if (info == null) {
                    _Log.debug("未找到玩家");
                    sendGroupReplyMsg("未找到玩家，请确认玩家是否在线");
                } else {
                    sendGroupReplyMsg(info.toString());
                }
                yield true;
            }
            case "申请白名单" -> {
                if (argCheck(2)) {
                    sendGroupReplyMsg("指令格式错误，正确格式：申请白名单 白名单名称");
                    yield false;
                }
                _Log.info("申请白名单");
                UserConfig config = new UserConfig(this.qq);
                if (config.isBind()) {
                    _Log.debug("已绑定账号");
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    if (info == null) {
                        _Log.debug("未找到玩家");
                        sendGroupReplyMsg( "未找到玩家，请确认玩家是否在线");
                    } else {
                        ACItemsData data = ACItemsConfig.I.get(args[1]);
                        if (data == null) {
                            _Log.debug("未找到此白名单");
                            sendGroupReplyMsg( "未找到此白名单，请确认此白名单是否存在");
                        } else {
                            if (data.allNeed() ? info.point() >= data.point() && info.level() >= data.level() : info.point() >= data.point() || info.level() >= data.level()) {
                                _Log.debug("白名单检测成功");
                                if (ACNet.I.addWhite(info.userid(), data.getFormatItems())) {
                                    sendGroupReplyMsg( "白名单添加成功");
                                } else {
                                    sendGroupReplyMsg( "白名单添加失败，网络异常");
                                }
                            } else {
                                sendGroupReplyMsg( "白名单添加失败，你未满足白名单要求：" + "\\n需要等级：" + data.level() + "\\n需要积分：" + data.point() + "\\n" + (data.allNeed() ? "需要等级和积分全部满足" : "需要等级或积分任一满足"));

                            }

                        }
                    }
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "查询白名单" -> {
                if (argCheck(2)) {
                    sendGroupReplyMsg("指令格式错误，正确格式：查询白名单 白名单名称");
                    yield false;
                }
                _Log.info("查询白名单");
                ACItemsData data = ACItemsConfig.I.get(args[1]);
                if (data == null) {
                    _Log.debug("未找到此白名单");
                    sendGroupReplyMsg( "未找到此白名单，请确认此白名单是否存在");
                } else {
                    _Log.debug("白名单查询成功");
                    sendGroupReplyMsg( data.toString());
                }
                yield true;
            }
            case "同意解封" -> {
                if (argCheck(2)) {
                    sendGroupReplyMsg("指令格式错误，正确格式：同意解封 qq");
                    yield false;
                }
                _Log.info("同意解封");
                UserConfig config = new UserConfig(this.qq);
                if (config.isBind()) {
                    _Log.debug("已绑定账号");

                    if (QQUsualCommand.banUser.containsKey(args[1])) {
                        if (args[1].equals(this.qq)) {
                            _Log.debug("相同玩家");
                            sendGroupReplyMsg( "您不能同意自己的解封申请");
                            yield true;
                        }
                        List<String> count = QQUsualCommand.banUser.get(args[1]);
                        if (count.contains(this.qq)) {
                            _Log.debug("已同意过此玩家的解封申请");
                            sendGroupReplyMsg( "您已同意过此玩家的解封申请");
                            yield true;
                        }
                        count.add(this.qq);
                        QQUsualCommand.banUser.put(args[1], count);
                        if (count.size() >= Config.I.getDatas().unBanNum) {
                            QQUsualCommand.banUser.remove(args[1]);
                            sendGroupReplyMsg( "已满足解封所需人数，即将解除封禁");
                            UserConfig b = new UserConfig(args[1]);
                            if (KitNet.unBan(b.getSteamID())) {
                                QQHelper.easySendGroupAtMsg(message.group_id, args[1], "解封成功");
                            } else {
                                QQHelper.easySendGroupAtMsg(message.group_id, args[1], "解封失败，网络异常");
                            }
                        } else {
                            _Log.debug("未满足解封所需人数");
                            sendGroupReplyMsg( "您已同意此玩家的解封申请");
                        }
                    } else {
                        _Log.debug("未找到此玩家");
                        sendGroupReplyMsg( "未找到此封禁玩家");
                    }
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "提高跟档物品数量" -> {
                if (argCheck(2)) {
                    sendGroupReplyMsg("指令格式错误，正确格式：提高跟档物品数量 数量");
                    yield false;
                }
                _Log.info("提高跟档物品数量");
                UserConfig config = new UserConfig(this.qq);
                if (config.isBind()) {
                    _Log.debug("已绑定账号");
                    if (config.getRecordItemLimit() >= Config.I.getDatas().recordItemLimit){
                        _Log.debug("达到上限");
                        sendGroupReplyMsg( "您的跟档物品数量已达到上限");
                        yield true;
                    }
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    if (info == null) {
                        sendGroupReplyMsg("请在线后再试");
                        yield true;
                    }
                    int n = Integer.parseInt(args[1]);
                    if (config.getRecordItemLimit() + n >= Config.I.getDatas().recordItemLimit) n = Config.I.getDatas().recordItemLimit - config.getRecordItemLimit();
                    int p  = n * Config.I.getDatas().recordItemPoint;
                    if (p <= 0) {
                        sendGroupReplyMsg("数据错误，请重新尝试");
                        yield true;
                    }
                    if (info.point() < p){
                        sendGroupReplyMsg("您的积分不足");
                        yield true;
                    }
                    if(BotNet.send_point(config.getSteamID(), -p)){
                        config.getDatas().recordItemLimit = config.getRecordItemLimit() + n;
                        config.save();
                        sendGroupReplyMsg( "已增加"+n+"个跟档物品数量，当前数量"+config.getRecordItemLimit());
                    }else sendGroupReplyMsg("添加失败，请重新尝试");
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            default -> false;
        };
    }

    public boolean argCheck(int length) {
        return this.args.length == length;
    }
}
