package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.data.ACItemsData;
import nws.dev.$7d2d.server.ServerConfig;
import nws.dev.core.json._JsonConfig;

public class AutoWhiteListConfig extends _JsonConfig<AutoWhiteListConfig.AutoWhiteListCD> {
    public AutoWhiteListConfig(String filePath) {
        super(filePath, """
                {
                    "enable":true,
                    "whiteItem":"白名单",
                    "allNeed":true,
                    "itemLevel":10,
                    "itemPoint":0,
                    "itemNumLevel":0,
                    "itemNumPoint":0,
                    "pointItem":"欢乐豆",
                    "pointItemMultiple":2
                }
                """, new TypeToken<>(){},false);
    }

    @Override
    public AutoWhiteListCD getDatas() {
        if (this.datas == null) this.datas = new AutoWhiteListCD(false,"",false,0,0,0,0,"",0);
        return super.getDatas();
    }

    public boolean isEnable() {
        return getDatas().enable() && !getDatas().whiteItem().isEmpty();
    }
    public String getWhiteItem() {
        return getDatas().whiteItem();
    }

    public void checkRecipe(ServerConfig serverCore, GameInfo.Recipe recipe){
        if (recipe.items().containsKey(getWhiteItem())){
            if (serverCore.acItem.includes(recipe.name())) return;
            int[] pl = {recipe.items().size() * getDatas().itemPoint(),
                    recipe.items().size() * getDatas().itemLevel()
            };

            recipe.items().forEach((s, integer) -> {
                pl[0] += integer * getDatas().itemNumPoint();
                pl[1] += integer * getDatas().itemNumLevel();
                if (s.equalsIgnoreCase(getDatas().pointItem())) pl[0] += integer * getDatas().pointItemMultiple();
            });
            ACItemsData data = new ACItemsData(new String[]{recipe.name()},pl[0],pl[1] ,getDatas().allNeed());
            serverCore.autoWhiteList.push(recipe.name(),data);
        }
    }







    public record AutoWhiteListCD(boolean enable, String whiteItem, boolean allNeed, int itemLevel, int itemPoint, int itemNumLevel, int itemNumPoint, String pointItem, int pointItemMultiple) {}
}
