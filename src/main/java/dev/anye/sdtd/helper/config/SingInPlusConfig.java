package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.server.ServerCore;

public class SingInPlusConfig extends _JsonConfig<SingInPlusConfig.SingInPlusData> {


    public SingInPlusConfig(String filePath) {
        super(filePath, """
                {
                    "steamid": "-1",
                    "time": "-1"
                }
                """, new TypeToken<>() {});
    }

    @Override
    public SingInPlusData getDatas() {
        if (this.datas == null) this.datas = new SingInPlusData("-1","-1");
        return super.getDatas();
    }
    public void setData(String qq,String time){
        this.datas = new SingInPlusData(qq,time);
    }

    public static SingInPlusConfig get(ServerCore serverCore, String qq) {
        return new SingInPlusConfig(serverCore.severUserDataDir + "/" + qq + ".plus.json");
    }
    public static SingInPlusConfig geto(ServerCore serverCore, String qq) {
        return new SingInPlusConfig(serverCore.severUserDataDir + "/" + qq + ".o.plus.json");
    }
    public record SingInPlusData(String steamid, String time){}
}
