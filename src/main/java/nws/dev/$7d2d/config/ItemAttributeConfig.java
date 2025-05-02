package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.json._JsonConfig;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class ItemAttributeConfig extends _JsonConfig<HashMap<String,String>> {
    public ItemAttributeConfig(String filePath) {
        super(filePath, """
                {
                    "AttacksPerMinute":"攻速",
                    "StaminaLoss":"耐力消耗",
                    "DegradationMax":"耐久",
                    "BlockRange":"物块触及距离",
                    "MaxRange":"最大范围",
                    "EntityDamage":"实体伤害",
                    "BlockDamage":"物块伤害"
                }
                """, new TypeToken<>(){},false);

    }
    public String attributeToString(Element element){
        StringBuilder stringBuilder = new StringBuilder();
        NodeList passiveEffects = element.getElementsByTagName("passive_effect");
        for (int i = 0; i < passiveEffects.getLength(); i++){
            Element p = (Element) passiveEffects.item(i);
            String n = getDesc(p.getAttribute("name"));
            if (!n.isEmpty()){
                if(!stringBuilder.isEmpty()) stringBuilder.append("\\n");
                if (p.getAttribute("tags").isEmpty()) stringBuilder.append("[FFD700]");
                else stringBuilder.append("[FF0000]");
                stringBuilder.append(n).append(" ").append(Operation.valueOf(p.getAttribute("operation")).desc).append(p.getAttribute("value"));
            }
        }
        return stringBuilder.toString();
    }
    public String getDesc(String name){
        return getDatas().getOrDefault(name,"");
    }
    public enum Operation{
        perc_add("perc_add","(倍乘) + "),
        perc_set("perc_set","(倍乘) = "),
        perc_subtract("perc_subtract","(倍乘) - "),
        base_add("base_add","(基础) + "),
        base_set("base_set","(基础) = "),
        base_subtract("base_subtract","(基础) - ");


        private final String name;
        private final String desc;
        Operation(String name,String desc){
            this.name = name;
            this.desc = desc;
        }

    }
}
