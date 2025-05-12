package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.core.json._JsonConfig;

public class FontConfig extends _JsonConfig<FontConfig.FontData> {
    public FontConfig(String file) {
        super(file, """
                {
                    "custom": false,
                    "name": "custom.ttf",
                    "lineHeight": 15
                }
                """, new TypeToken<>() {});
    }

    @Override
    public FontData getDatas() {
        if (this.datas == null) this.datas = new FontData(false,"",15);
        return super.getDatas();
    }
    public boolean isCustom() {return getDatas().custom;}
    public String getFontName() {return getDatas().name;}
    public float getLineHeight() {return getDatas().lineHeight;}

    public record FontData(boolean custom,String name, float lineHeight) {}
}
