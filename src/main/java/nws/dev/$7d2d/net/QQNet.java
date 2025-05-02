package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import nws.dev.$7d2d.command.CommandInfo;
import nws.dev.$7d2d.command.CommandRegistryNew;
import nws.dev.$7d2d.command.QQCommand;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.UserUaualConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class QQNet {
    private static HttpServer server = null;
    public static void listen() throws IOException {
        server = HttpServer.create(new InetSocketAddress(Config.I.listenPort()), 0);
        _Log.info("HTTP 服务器启动，正在监听端口 "+ Config.I.listenPort());
        server.createContext("/", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                _Log.debug(json);
                String response = "false";
                Gson gson = new Gson();
                QQData.Message message = gson.fromJson(json, QQData.Message.class);

                if (message != null) {
                    if (message.user_id.isEmpty()) return;
                    _Log.debug("收到来自 " + message.user_id + " 的消息");
                    String command = message.message.get(0).data().get("text");
                    if (command != null && !command.isEmpty()) {
                        if (command.contains(" ")) command = command.split(" ")[0];
                        _Log.debug("解析指令：" + command);
                        boolean qa = true;
                        ServerCore serverCore;
                        if (message.message_type.equals("private")) {
                            UserUaualConfig userUaualConfig = new UserUaualConfig(message.user_id);
                            serverCore = ServerCore.getServer(userUaualConfig.privateServer());
                        } else serverCore = ServerCore.getGroupServer(message.group_id);

                        if (CommandRegistryNew.getCommandMap().containsKey(command)) {
                            CommandInfo commandInfo = CommandRegistryNew.getCommandMap().get(command);

                            if (Permission.getPermission(message.user_id, serverCore).getPermission() <= commandInfo.permission().getPermission()) {
                                qa = false;
                                try {
                                    Class<?> commandClass = commandInfo.commandClass();
                                    QQCommand qqCommand;

                                    try {
                                        // 尝试调用接受 Message 参数的构造函数
                                        Constructor<?> constructor = commandClass.getDeclaredConstructor(QQData.Message.class, ServerCore.class);
                                        qqCommand = (QQCommand) constructor.newInstance(message, serverCore);
                                    } catch (NoSuchMethodException e) {
                                        // 如果没有接受 Message 参数的构造函数，则调用无参的构造函数
                                        qqCommand = (QQCommand) commandClass.getDeclaredConstructor().newInstance();
                                    }

                                    if (qqCommand.runCommand())response ="true"; // 执行命令

                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                         NoSuchMethodException e) {
                                    _Log.error(e.getMessage());
                                }
                            }
                        }
                        if (qa && serverCore != null) QAMsg(message, serverCore);
                    }
                    /*
                    QQAtCommand atCommand = new QQAtCommand(message);
                    boolean canRun = !atCommand.runCommand();
                    QQUsualCommand usualCommand = new QQUsualCommand(message);
                    if (canRun) canRun = !usualCommand.runCommand();
                    QQExCommand exCommand = new QQExCommand(message);
                    if (canRun) canRun = !exCommand.runCommand();
                    if (canRun) canRun = !QAMsg(message);
                    if (!canRun) {
                        response = "true";
                    }

                     */
                }
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 方法不被允许
            }
        });
        server.start();
    }


    private static boolean QAMsg(QQData.Message message,ServerCore serverCore) {
        String answer = serverCore.qa.getOrDefault(message.raw_message,"");
        if (answer.isEmpty())return false;
        QQHelper.easySendGroupReplyMsg(serverCore,message.group_id,message.message_id,answer);
        return true;
    }
    public static void stop(){
        if (server != null) server.stop(0);
    }
}
