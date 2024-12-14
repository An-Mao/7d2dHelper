package nws.dev.$7d2d.system;

public class DataTable {
    public static final String Dir = _File.getFileFullPathWithRun("config");
    public static final String UserDir = _File.getFileFullPathWithRun("config/user");
    public static final String SingInDir = _File.getFileFullPathWithRun("config/singIn");
    static {
        _File.checkAndCreateDir(Dir);
        _File.checkAndCreateDir(UserDir);
        _File.checkAndCreateDir(SingInDir);
    }
}
