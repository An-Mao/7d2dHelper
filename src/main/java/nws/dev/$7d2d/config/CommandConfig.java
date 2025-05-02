package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.json._JsonConfig;

import java.util.HashMap;

public class CommandConfig extends _JsonConfig<HashMap<String, Boolean>> {
    public CommandConfig(String filePath) {
        super(filePath, """
                {
                    "help": true,
                    "adminHelp":true,
                    "signHelp":true,
                    "kickSelf":true,
                    "whiteList":false,
                    "eventList":true,
                    "bind":true,
                    "serverInfo":true,
                    "getNewPlayerGift":true,
                    "sign":true,
                    "killSelf":true,
                    "lookSelf":true,
                    "requestUnban":true,
                    "requestSaveItem":true,
                    "sendSaveItem":true,
                    "receiveGift":true,
                    "requestInfo":true,
                    "applyWhite":true,
                    "requestWhite":true,
                    "agreeUnban":true,
                    "buySaveItemNum":true,
                    "findWhite":true,
                    "getItemInfo":true,
                    "findItem":true,
                    "requestRecipe":true
                }
                """, new TypeToken<>(){});
    }

    @Override
    public HashMap<String, Boolean> getDatas() {
        if (this.datas == null) this.datas = new HashMap<>();
        return super.getDatas();
    }

    public boolean isEnable(String command){
        return !this.getDatas().getOrDefault(command,false);
    }
}
