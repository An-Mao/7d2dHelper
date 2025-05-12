package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.core.json._JsonConfig;

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
