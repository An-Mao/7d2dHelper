package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.data.ACData;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.data.Web;

public class ACNet {
    private final ServerData serverData;

    private final String acRootUrl ;
    private final String acLoginUrl ;
    private final String acAddWhiteUrl;
    private final String acCheckTokenUrl;
    private String token = "";
    public ACNet(ServerData serverData) {
        this.serverData = serverData;
        this.acRootUrl = "http://"+ serverData.acHost() +"/";
        this.acLoginUrl = acRootUrl+"api/login?username="+serverData.acUsername()+"&password="+serverData.acPassword();
        this.acCheckTokenUrl = acRootUrl +"api/checksession?key=";
        this.acAddWhiteUrl = acRootUrl +"api/action_antiobjects_skiplist_add?key=";
        login();
    }
    private boolean login() {
        String response = Net.sendGetData(acLoginUrl);
        Gson gson = new Gson();
        ACData.Login res = gson.fromJson(response, ACData.Login.class);
        if (res != null && res.result() == 1) {
            $7DTD._Log.info("AC登录成功");
            token = res.session_key();
            return true;
        }else {
            $7DTD._Log.error("AC登录失败");
            return false;
        }
    }



    public boolean addWhite(String userId, String item) {
        String response = Net.sendGetData(this.acAddWhiteUrl+getToken()+"&userid="+userId+"&items="+item);
        Gson gson = new Gson();
        Web.Result res = gson.fromJson(response, Web.Result.class);
        return res != null && res.result == 1;
    }


















    public boolean isLogin() {
        return token != null && !token.isEmpty();
    }
    public boolean checkLogin() {
        String response = Net.sendGetData(this.acCheckTokenUrl + token);
        Gson gson = new Gson();
        Web.Result res = gson.fromJson(response, Web.Result.class);
        return res != null && res.result == 1;
    }
    public String getToken() {
        if (!isLogin() || !checkLogin()) {
            $7DTD._Log.warn("session_key失效，尝试重新登录");
            if (!login()) return "";
        }
        return token;
    }
}
