package nws.dev.$7d2d;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.data.ConfigData;
import nws.dev.$7d2d.system._File;
import nws.dev.$7d2d.system._Pack;

public class DataTable {
    public static final String Dir = _File.getFileFullPathWithRun("config");
    public static final String DataDir = _File.getFileFullPathWithRun("data");
    public static final String LanguageDir = _File.getFileFullPathWithRun("lang");
    public static final String CacheDir = _File.getFileFullPathWithRun("cache");


    public static final String UserDataDir = DataDir + "/user";
    //public static final String UserMsgBindDataDir = UserDataDir + "/MsgBind";
    public static final String ServerListDir = Dir + "/ServerList";
    public static final String ServerConfigDir = Dir + "/ServerConfig";
    public static final String ServerDataDir = DataDir + "/server";

    public static final String LogDir = Dir + "/log";
    public static final String UserDir = Dir + "/user";
    //public static final String UserItemDir = Dir + "userItem";
    //public static final String SingInDir = Dir + "singIn";
    public static final String FontDir = Dir + "/font";
    //public static final String EventDir = Dir + "event";

    public static final String ImageCache = CacheDir + "/image";



    public static void init(){
        _File.checkAndCreateDir(Dir);
        _File.checkAndCreateDir(DataDir);
        _File.checkAndCreateDir(LanguageDir);
        _File.checkAndCreateDir(CacheDir);

        _File.checkAndCreateDir(UserDataDir);
        //_File.checkAndCreateDir(UserMsgBindDataDir);

        _File.checkAndCreateDir(LogDir);
        _File.checkAndCreateDir(UserDir);
        //_File.checkAndCreateDir(UserItemDir);
        //_File.checkAndCreateDir(SingInDir);
        _File.checkAndCreateDir(FontDir);
        //_File.checkAndCreateDir(EventDir);

        _File.checkAndCreateDir(ImageCache);

        _File.checkAndCreateDir(ServerListDir);
        _File.checkAndCreateDir(ServerConfigDir);
        _File.checkAndCreateDir(ServerDataDir);
        if (Config.I.getDatas().outputDefaultConfig()) {
            ConfigData configData = Config.I.getDatas();
            Config.I.setData(new ConfigData(false,configData.logColor(), configData.isDebug(), configData.listenPort(), configData.qqHost(), configData.admin()));
            Config.I.save();
            _Pack.writeFiles("assets/lang/", LanguageDir + "/", ".json", "zh_cn");
            _Pack.writeFiles("assets/config/server/", ServerListDir + "/", ".json", "example");
        }

    }

}
