package nws.dev.$7d2d;

import nws.dev.$7d2d.config.Configs;
import nws.dev.$7d2d.system._File;
import nws.dev.$7d2d.system._Pack;

public class DataTable {
    public static final String Dir = _File.getFileFullPathWithRun("config");
    public static final String DataDir = _File.getFileFullPathWithRun("data");
    public static final String LanguageDir = _File.getFileFullPathWithRun("lang");
    public static final String CacheDir = _File.getFileFullPathWithRun("cache");


    public static final String ServerConfigDir = _File.getFileFullPathWithRun(Dir + "/server");
    public static final String ServerDataDir = _File.getFileFullPathWithRun(DataDir + "/server");

    public static final String LogDir = _File.getFileFullPathWithRun("config/log");
    public static final String UserDir = _File.getFileFullPathWithRun("config/user");
    public static final String UserItemDir = _File.getFileFullPathWithRun("config/userItem");
    public static final String SingInDir = _File.getFileFullPathWithRun("config/singIn");
    public static final String FontDir = _File.getFileFullPathWithRun("config/font");
    public static final String EventDir = _File.getFileFullPathWithRun("config/event");

    public static final String ImageCache = _File.getFileFullPathWithRun("cache/image");



    public static void init(){
        _File.checkAndCreateDir(Dir);
        _File.checkAndCreateDir(DataDir);
        _File.checkAndCreateDir(LanguageDir);
        _File.checkAndCreateDir(CacheDir);

        _File.checkAndCreateDir(LogDir);
        _File.checkAndCreateDir(UserDir);
        _File.checkAndCreateDir(UserItemDir);
        _File.checkAndCreateDir(SingInDir);
        _File.checkAndCreateDir(FontDir);
        _File.checkAndCreateDir(EventDir);

        _File.checkAndCreateDir(ImageCache);

        _File.checkAndCreateDir(ServerConfigDir);
        _File.checkAndCreateDir(ServerDataDir);

        _Pack.writeFiles("lang/",LanguageDir,".json","zh_cn");
        _Pack.writeFiles("config/server/", ServerConfigDir,".json","example");

        Configs.init();
    }

}
