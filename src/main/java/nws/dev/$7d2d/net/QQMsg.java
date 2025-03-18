package nws.dev.$7d2d.net;

import nws.dev.$7d2d.config.SingInConfig;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.system._Log;

public class QQMsg {
    public static boolean isSignedInPlus(QQData.Message message){
        //签到[CQ:at,steamid=1977970939,name=nekowq]
        if (message.message.size() == 2) {
            QQData.Message.Msg msg = message.message.get(0);
            if (msg.type().equals("text") && msg.data().containsKey("text") && msg.data().get("text").equals("签到")) {

                msg = message.message.get(1);
                if (msg.type().equals("at") && msg.data().containsKey("qq")) {
                    UserConfig config = new UserConfig(message.user_id);
                    if (config.isBind()) {
                        String qq = msg.data().get("qq");
                        UserConfig c = new UserConfig(qq);
                        if (c.isBind()) {
                            SingInConfig singInConfig = new SingInConfig(message.user_id);
                            String s = singInConfig.sign(config.getSteamID(), c.getSteamID());
                            _Log.debug(s);
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, s);


                            /*
                            if (BotNet.sign(c.getSteamID())) {
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "签到成功");
                            } else {
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "签到失败，请稍后再试");
                            }

                             */
                        } else QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "对方未绑定账号");
                    } else QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                    return true;
                }
            }
        }
        return false;
    }
}
