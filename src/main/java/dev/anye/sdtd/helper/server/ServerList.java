package dev.anye.sdtd.helper.server;

import dev.anye.sdtd.helper.config.Configs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ServerList {
    public static final HashMap<String, ServerCore> LIST = new HashMap<>();

    public static @Nullable ServerCore getGroupServer(String group){
        return LIST.getOrDefault(Configs.groupServer.get(group),null);
    }
    public static @Nullable ServerCore getServer(String key){
        return LIST.getOrDefault(key,null);
    }
}
