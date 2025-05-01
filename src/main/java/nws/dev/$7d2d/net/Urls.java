package nws.dev.$7d2d.net;

import nws.dev.$7d2d.config.Config;

public class Urls {
    public static String chatUrl;
    public static String chatSyncUrl;
    public static String commandUrl;
    public static String onlinePlayerUrl;
    public static String gsListUrl;
    public static String botChatUrl;
    public static String botChatHistoryUrl;
    public static String botChatSendUrl;

    public static String acRootUrl;
    public static String acLoginUrl;
    public static String acCheckTokenUrl;
    public static String acAddWhiteUrl;


    public static String qqSendGroupMsg;
    public static String qqSendPrivateMsg;
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
        qqSendPrivateMsg = "http://"+ Config.I.getDatas().qqHost+"/send_private_msg";

        botChatUrl = "http://"+ Config.I.getDatas().botHost +"/api/livechat_pull?key=";//+id
        botChatHistoryUrl = "http://"+ Config.I.getDatas().botHost +"/api/livechat_history?target=group_0&key=";
        //api/livechat_sendmessage?key=P8t0efXcPhUX&to=group_0&msg=tesr
        botChatSendUrl = "http://"+ Config.I.getDatas().botHost +"/api/livechat_sendmessage?key=";


        acRootUrl = "http://"+ Config.I.getDatas().acHost +"/";
        acLoginUrl = acRootUrl+"api/login?username="+Config.I.getDatas().acUsername+"&password="+Config.I.getDatas().acPassword;
        acCheckTokenUrl = acRootUrl +"api/checksession?key=";
        acAddWhiteUrl = acRootUrl +"api/action_antiobjects_skiplist_add?key=";
    }
}
