package nws.dev.$7d2d.system;

public class _Time {
    public static int getSysSec(){
        return  (int) (System.currentTimeMillis() / 1000);
    }
}
