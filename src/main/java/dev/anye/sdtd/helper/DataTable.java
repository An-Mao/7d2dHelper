package dev.anye.sdtd.helper;

import dev.anye.core.pack._Pack;
import dev.anye.core.system._File;
import dev.anye.sdtd.helper.config.Config;
import dev.anye.sdtd.helper.data.ConfigData;

public class DataTable {
    public static final String CONFIG = _File.getFileFullPathWithRun("config");
    public static final String JS = _File.getFileFullPathWithRun("js");
    public static final String DataDir = _File.getFileFullPathWithRun("data");
    //public static final String LanguageDir = _File.getFileFullPathWithRun("lang");
    public static final String CacheDir = _File.getFileFullPathWithRun("cache");


    public static final String UserDataDir = DataDir + "/user";
    //public static final String UserMsgBindDataDir = UserDataDir + "/MsgBind";
    public static final String ServerListDir = CONFIG + "/ServerList";
    public static final String ServerConfigDir = CONFIG + "/ServerConfig";
    public static final String ServerDataDir = DataDir + "/server";

    public static final String LogDir = DataDir + "/log";
    public static final String UserDir = CONFIG + "/user";
    //public static final String UserItemDir = Dir + "userItem";
    //public static final String SingInDir = Dir + "singIn";
    public static final String FontDir = CONFIG + "/font";
    //public static final String EventDir = Dir + "event";

    public static final String ImageCache = CacheDir + "/image";



    public static void init(){
        _File.checkAndCreateDir(CONFIG,JS,DataDir,
                CacheDir,UserDataDir,LogDir,UserDir,FontDir,ImageCache,
                ServerListDir,ServerConfigDir,ServerDataDir);

        if (Config.I.getDatas().outputDefaultConfig()) {
            ConfigData configData = Config.I.getDatas();
            Config.I.setData(new ConfigData(false,configData.logColor(), configData.isDebug(), configData.listenPort(), configData.qqHost(), configData.admin()));
            Config.I.save();
            //_Pack.writeFiles("assets/lang/", LanguageDir + "/", ".json", "zh_cn");
            _Pack.writeFiles("assets/config/server/", ServerListDir + "/", ".json", "example");
        }

    }

}
