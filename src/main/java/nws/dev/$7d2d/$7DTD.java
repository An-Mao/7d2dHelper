package nws.dev.$7d2d;

import nws.dev.$7d2d.config.*;
import nws.dev.$7d2d.event.Events;
import nws.dev.$7d2d.net.*;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;

public class $7DTD {
    public static final String Version = "25.04.2900";
    public static void init() {
        try {
            _Log.info("当前版本：" + Version);
            outputConfigInfo();
            loginUser();
            Heartbeat.start();
            Events.event.start();
            _Log.info("AC登录状态：" + ACNet.I.isLogin());
            _Log.info("开始启动对" + Config.I.listenPort() + "端口消息的监听");
            QQNet.listen();
        } catch (IOException e) {
            _Log.error(e.getMessage());
        }
    }

    private static void outputConfigInfo() {
        _Log.debug("开始初始化配置文件");
        _Log.debug("签到奖励", DailyRewardsConfig.I.getDatas().toString());
        _Log.debug("问答", QA.I.getDatas().keySet().toString());
        _Log.debug("命令", CommandConfig.I.getDatas().toString());
        _Log.debug("开始读取游戏文件");
        _Log.debug("已读取物品数量：" + GameInfo.I.getItems().size());
        _Log.debug("已读取配方数量：" + GameInfo.I.getRecipes().size());
        _Log.debug("已读取语言数量：" + GameInfo.I.getLanguage().size());
    }

    public static void loginUser() {
        _Log.info("开始登录Ket");
        if (KitNet.loginUser()) {
            _Log.info("Ket登录成功：accessToken=" + KitNet.getToken());
        } else {
            _Log.error("Ket登录失败");
        }
        _Log.info("开始登录Bot");
        BotNet.Servers.forEach((s, botNet) -> {
            if (botNet.loginUser()) {
                _Log.info(s + " Bot登录成功：session_key=" + botNet.getToken());
            } else {
                _Log.error(s + " Bot登录失败");
            }
        });
    }

}
