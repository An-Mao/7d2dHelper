package nws.dev.$7d2d.data;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.server.ServerCore;

public enum Permission {
    Admin(0),
    ServerAdmin(1),
    User(2);
    private final int permission;
    Permission(int permission){
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }

    public static Permission getPermission(String qq, ServerData serverData){
        if (Config.I.isAdmin(qq)) return Admin;
        if (serverData.adminQQ().contains(qq)) return ServerAdmin;
        return User;
    }
    public static Permission getPermission(String qq, ServerCore serverCore){
        if (serverCore == null)return Permission.User;
        return getPermission(qq, serverCore.serverData);
    }
}
