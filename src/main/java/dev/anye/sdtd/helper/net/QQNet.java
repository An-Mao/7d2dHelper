package dev.anye.sdtd.helper.net;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import dev.anye.core.javascript._EasyJS;
import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.DataTable;
import dev.anye.sdtd.helper.command.CommandRegistryNew;
import dev.anye.sdtd.helper.command.QQCommand;
import dev.anye.sdtd.helper.config.Config;
import dev.anye.sdtd.helper.config.UserUaualConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.helper.QQHelper;
import dev.anye.sdtd.helper.server.ServerCore;
import dev.anye.sdtd.helper.server.ServerList;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class QQNet {
    private static HttpServer server = null;
    public static void listen() throws IOException {
        server = HttpServer.create(new InetSocketAddress(Config.I.listenPort()), 0);
        $7DTD._Log.info("HTTP 服务器启动，正在监听端口 "+ Config.I.listenPort());

        server.createContext("/", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                $7DTD._Log.debug(json);
                AtomicReference<String> response = new AtomicReference<>("false");
                Gson gson = new Gson();
                QQData.Message message = gson.fromJson(json, QQData.Message.class);
                checkMsg(message,response);
                exchange.sendResponseHeaders(200, response.get().getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.get().getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 方法不被允许
            }
        });
        server.start();
    }
    public static void checkMsg(QQData.Message message,AtomicReference<String> response) {
        if (message == null || message.user_id.isEmpty()) return;
        $7DTD._Log.debug("收到来自 " + message.user_id + " 的消息");
        String command = message.message.get(0).data().get("text");
        if (command != null && !command.isEmpty()) {
            if (command.contains(" ")) command = command.split(" ")[0];
            AtomicReference<Boolean> qa = new AtomicReference<>(true);
            @Nullable ServerCore serverCore;
            if (message.message_type.equals("private")) {
                UserUaualConfig userUaualConfig = new UserUaualConfig(message.user_id);
                serverCore = ServerList.getServer(userUaualConfig.privateServer());
            } else serverCore = ServerList.getGroupServer(message.group_id);
            if (CommandRegistryNew.getCommands().containsKey(command)) {
                AtomicReference<Boolean> run = new AtomicReference<>(true);
                CommandRegistryNew.getCommands().get(command).forEach(commandInfo -> {
                    //$7DTD._Log.debug("权限 " + Permission.getPermission(message.user_id, serverCore).name());
                    if (run.get() && Permission.getPermission(message.user_id, serverCore).getPermission() <= commandInfo.permission().getPermission()) {
                        try {
                            Class<?> commandClass = commandInfo.commandClass();
                            //$7DTD._Log.debug("执行 " + Permission.getPermission(message.user_id, serverCore).name());

                            QQCommand qqCommand;
                            try {
                                Constructor<?> constructor = commandClass.getDeclaredConstructor(QQData.Message.class, ServerCore.class);
                                qqCommand = (QQCommand) constructor.newInstance(message, serverCore);
                            } catch (NoSuchMethodException e) {
                                qqCommand = (QQCommand) commandClass.getDeclaredConstructor().newInstance();
                            }
                            if (qqCommand.runCommand()) {
                                response.set("true"); // 执行命令
                                qa.set(false);
                                run.set(false);
                            }

                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            $7DTD._Log.error(e.getMessage());
                        }


                    }
                });


            }
            if (qa.get() && serverCore != null){
                if (QAMsg(message, serverCore)) response.set("true");
                else {
                    if (serverCore.js.containsKey(command)){
                        String file = DataTable.JS+"/"+serverCore.js.get(command);
                        $7DTD._Log.debug("运行JS脚本："+file);
                        _EasyJS easyJS = _EasyJS.NotSafe();
                        easyJS.addParameter("msg",message)
                                .addParameter("permission",Permission.getPermission(message.user_id, serverCore).name())
                                .addParameter("serverCore",serverCore);
                        easyJS.runFile(file);
                    }
                }
            }
        }


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
