package nws.dev.$7d2d.data;

import java.util.HashMap;

public class QQData {
    public static class Message {
        public String self_id;
        public String user_id = "";
        public String time;
        public String message_id;
        public String real_id;
        public String message_seq;
        public String message_type;
        public Sender sender;


        public String raw_message;
        public String font;
        public String sub_type;

        //public List<Msg> message;
        public String message_format;
        public String post_type;
        public String group_id = "";

        public record Sender(String user_id, String nickname, String card,String role,String title) { }
        public record Msg(String type, MsgData data) { }
        public record MsgData(String text) { }
    }
    public record Msg(MsgType type, HashMap<String,String> data){
        public static Msg create(MsgType type, String key, String value){
            HashMap<String,String> map = new HashMap<>();
            map.put(key,value);
            return new Msg(type,map);
        }
        @Override
        public String toString() {
            StringBuilder s = new StringBuilder("{");
            data.forEach((s1, s2) -> s.append("\"").append(s1).append("\":\"").append(s2).append("\""));
            s.append("}");
            return "{\"type\":\""+type.toString()+"\",\"data\":"+s+"}";
        }

    }
    public enum MsgType{
        Text("text"),
        Image("image");
        private final String s;
        MsgType(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}
