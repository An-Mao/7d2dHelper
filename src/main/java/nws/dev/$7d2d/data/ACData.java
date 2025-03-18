package nws.dev.$7d2d.data;

public class ACData {
    public record Login(int result,String session_key) {
        public Login(int result) {
            this(result,"");
        }
    }
}
