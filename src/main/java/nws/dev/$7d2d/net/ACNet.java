package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.data.ACData;
import nws.dev.$7d2d.data.Web;
import nws.dev.$7d2d.system._Log;

public class ACNet {
    public static final ACNet I = new ACNet();

    private String token = "";
    public ACNet() {
        login();
    }
    private boolean login() {
        String response = Net.sendGetData(Urls.acLoginUrl);
        Gson gson = new Gson();
        ACData.Login res = gson.fromJson(response, ACData.Login.class);
        if (res != null && res.result() == 1) {
            _Log.info("AC登录成功");
            token = res.session_key();
            return true;
        }else {
            _Log.error("AC登录失败");
            return false;
        }
    }



    public boolean addWhite(String userId, String item) {
        String response = Net.sendGetData(Urls.acAddWhiteUrl+getToken()+"&userid="+userId+"&items="+item);
        Gson gson = new Gson();
        Web.Result res = gson.fromJson(response, Web.Result.class);
        return res != null && res.result == 1;
    }


















    public boolean isLogin() {
        return token != null && !token.isEmpty();
    }
    public boolean checkLogin() {
        String response = Net.sendGetData(Urls.acCheckTokenUrl + token);
        Gson gson = new Gson();
        Web.Result res = gson.fromJson(response, Web.Result.class);
        return res != null && res.result == 1;
    }
    public String getToken() {
        if (!isLogin() || !checkLogin()) {
            _Log.warn("session_key失效，尝试重新登录");
            if (!login()) return "";
        }
        return token;
    }
}
