package nws.dev.$7d2d.net;

import nws.dev.$7d2d.config.Config;

public class Urls {
    public static String chatUrl;
    public static String chatSyncUrl;
    public static String commandUrl;
    public static String onlinePlayerUrl;
    public static String gsListUrl;


    public static String qqSendGroupMsg;
    static {
        reload();
    }

    public static void reload() {
        chatUrl = "http://"+Config.I.getDatas().kitHost +"/api/chat?admintoken="+Config.I.getDatas().adminToken;
        chatSyncUrl = "http://"+Config.I.getDatas().kitHost +"/api/chat_sync?admintoken="+Config.I.getDatas().adminToken;
        commandUrl = "http://"+ Config.I.getDatas().kitHost +"/api/executeconsolecommand?admintoken="+Config.I.getDatas().adminToken;
        onlinePlayerUrl = "http://"+ Config.I.getDatas().kitHost +"/api/getplayersonline?admintoken="+Config.I.getDatas().adminToken;
        gsListUrl = "http://"+ Config.I.getDatas().kitHost +"/cgi/gs_list";

        qqSendGroupMsg = "http://"+ Config.I.getDatas().qqHost+"/send_group_msg";
    }
}
