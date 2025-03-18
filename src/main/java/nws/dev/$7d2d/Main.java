package nws.dev.$7d2d;

import com.google.gson.Gson;
import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.*;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.event.Events;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.*;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static final String Version = "25.03.1000";
    public static void main(String[] args) throws IOException {
        init();
    }
    private static void test(){
        String test = """
            {"self_id":1977970939,"user_id":751898988,"time":1741425113,"message_id":1407333403,"message_seq":58072,"message_type":"group","sender":{"user_id":751898988,"nickname":"猫","card":"","role":"owner","title":""},"raw_message":"签到","font":14,"sub_type":"normal","message":[{"type":"text","data":{"text":"签到"}}],"message_format":"array","post_type":"message","group_id":164447436}
            """;

        //{"self_id":1977970939,"user_id":751898988,"time":1741425113,"message_id":1407333403,"message_seq":58072,"message_type":"group","sender":{"user_id":751898988,"nickname":"猫","card":"","role":"owner","title":""},"raw_message":"签到[CQ:at,qq=1977970939,name=nekowq]","font":14,"sub_type":"normal","message":[{"type":"text","data":{"text":"签到"}},{"type":"at","data":{"qq":"1977970939","name":"nekowq"}}],"message_format":"array","post_type":"message","group_id":164447436}
        Gson gson = new Gson();
        QQData.Message message = gson.fromJson(test, QQData.Message.class);
        _Log.info(String.valueOf(QQNet.usualMsg(message)));

    }
    public static void init() throws IOException{
        _Log.info("当前版本："+Version);
        //test();
        _Log.info("开始初始化配置文件");
        _Log.info("签到奖励",DailyRewardsConfig.I.getDatas().toString());
        _Log.info("问答",QA.I.getDatas().keySet().toString());
        loginUser();
        Heartbeat.start();
        Events.event.start();
        _Log.info( "AC登录状态："+ACNet.I.isLogin());
        QQNet.listen();
        _Log.info("开始启动对"+ Config.I.getDatas().listenPort +"端口消息的监听");
        Thread inputThread = runInputThread();
        inputThread.start();


        //KitNet.getBan("76561198193776909");
    }

    private static void loginUser(){
        _Log.info("开始登录Ket");
        if (KitNet.loginUser()){
            _Log.info("Ket登录成功：accessToken="+ KitNet.getToken());
        }else {
            _Log.error("Ket登录失败");
        }
        _Log.info("开始登录Bot");
        if (BotNet.loginUser()){
            _Log.info("Bot登录成功：session_key="+ BotNet.getToken());
        }else {
            _Log.error("Bot登录失败");
        }
    }
    private static Thread runInputThread() {
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine();
                    switch (userInput){
                        case "运行kit" -> QQNet.runKitExe();
                        case "登录kit" -> loginUser();
                        case "清理服务器" -> ServerCommand.restart();
                        case "重启服务器" -> {
                            if (QQNet.restartThread.isAlive()){
                                _Log.warn("重启进程运行中，请勿多次发起");
                            }else {
                                if (System.currentTimeMillis() - QQNet.wt < 60000) {
                                    _Log.warn( "即将完全重启服务器");
                                    QQNet.restartThread.start();
                                }else {
                                    _Log.warn( "如果您想完全重启服务器，请在60秒内再发一次此指令");
                                    QQNet.wt = System.currentTimeMillis();
                                }
                            }
                        }
                        case "重新加载配置" -> ServerCommand.reloadConfig();
                        case "切换调试模式" -> Config.I.getDatas().isDebug = !Config.I.getDatas().isDebug;

                        case "申请白名单" -> {
                            _Log.info("申请白名单");
                            ACItemsData data = ACItemsConfig.I.get("小小武器5星");
                            if (data == null) {
                                _Log.debug("未找到此白名单");
                            } else {
                                _Log.debug("白名单检测成功");
                                if (ACNet.I.addWhite("EOS_0002test", data.getFormatItems())) {
                                    _Log.debug("白名单添加成功");
                                } else {
                                    _Log.debug("白名单添加失败");
                                }

                            }

                        }
                    }
                }
            }
        });
        inputThread.setDaemon(true);
        return inputThread;
    }

}