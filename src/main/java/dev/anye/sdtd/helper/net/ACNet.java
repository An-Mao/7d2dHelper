package dev.anye.sdtd.helper.net;

import com.google.gson.Gson;
import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.data.ACData;
import dev.anye.sdtd.helper.data.GetData;
import dev.anye.sdtd.helper.data.ServerData;
import dev.anye.sdtd.helper.data.Web;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class ACNet {
    private final Gson gson = new Gson();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ServerData serverData;

    private final String acRootUrl ;
    private final String acLoginUrl ;
    private final String acAddWhiteUrl;
    private final String acCheckTokenUrl;
    private final String urlLog;
    private String token = "";
    public ACNet(ServerData serverData) {
        this.serverData = serverData;
        this.acRootUrl = "http://"+ serverData.acHost() +"/";
        this.acLoginUrl = acRootUrl+"api/login?username="+serverData.acUsername()+"&password="+serverData.acPassword();
        this.acCheckTokenUrl = acRootUrl +"api/checksession?key=";
        this.acAddWhiteUrl = acRootUrl +"api/action_antiobjects_skiplist_add?key=";
        this.urlLog = acRootUrl +"api/logs?";

        login();
    }
    private boolean login() {
        String response = Net.sendGetData(acLoginUrl);
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
        Web.Result res = gson.fromJson(response, Web.Result.class);
        return res != null && res.result == 1;
    }
    public String getLog(String id){
        GetData getData = new GetData(this.urlLog);
        getData.add("key",getToken());
        getData.add("page","1");
        getData.add("limit","50");
        getData.add("searchkey",id);
        String response = Net.sendGetData(getData.toString());
        StringBuilder stringBuilder = new StringBuilder();
        ACData.Logs logs = gson.fromJson(response,ACData.Logs.class);
        if (logs != null && !logs.data().isEmpty()){
            AtomicReference<Integer> run = new AtomicReference<>(0);
            LocalDateTime[] time = {null};

            logs.data().forEach(log -> {
                if (run.get() > 3) return;
                if (log.userid().equals(id)) {
                    if (time[0] == null) time[0] = LocalDateTime.parse(log.time(), formatter);
                    LocalDateTime time1 = LocalDateTime.parse(log.time(), formatter);
                    Duration duration1 = Duration.between(time[0], time1);
                    if (duration1.getSeconds() < 3) {
                        run.set(run.get() + 1);
                        stringBuilder.append("\\n----------\\n类型：").append(log.typedesc()).append("\\n附加数据：").append(log.param()).append("\\n描述：").append(log.desc());
                    }
                }
            });
            /*
            ACData.Log log = logs.data().get(0);
            stringBuilder.append("----------\\n类型：").append(log.typedesc()).append("\\n附加数据：").append(log.param()).append("描述：").append(log.desc());

             */
        }
        return stringBuilder.toString();
    }


















    public boolean isLogin() {
        return token != null && !token.isEmpty();
    }
    public boolean checkLogin() {
        String response = Net.sendGetData(this.acCheckTokenUrl + token);
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
