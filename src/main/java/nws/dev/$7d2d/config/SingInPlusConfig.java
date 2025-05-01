package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.json._JsonConfig;

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

    public static SingInPlusConfig get(String qq) {
        return new SingInPlusConfig(DataTable.SingInDir + "/" + qq + ".plus.json");
    }
    public static SingInPlusConfig geto(String qq) {
        return new SingInPlusConfig(DataTable.SingInDir + "/" + qq + ".o.plus.json");
    }
    public record SingInPlusData(String steamid, String time){}
}
