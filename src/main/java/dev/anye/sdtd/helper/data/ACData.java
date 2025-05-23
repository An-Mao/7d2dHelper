package dev.anye.sdtd.helper.data;

import java.util.List;

public class ACData {
    public record Login(int result,String session_key) {
        public Login(int result) {
            this(result,"");
        }
    }
    public record Logs(int code, int count, List<Log> data,String msg){}

    /**
     * 日志基础数据
     * @param addid 关联id
     * @param desc 描述
     * @param param 附加数据
     * @param pn 玩家名
     * @param pos 坐标
     * @param time 日期
     * @param typedesc 作弊类型
     * @param userid
     */
    public record Log(String addid,String desc,String param,String pn,String pos,String time,String typedesc,String userid){

    }
}
