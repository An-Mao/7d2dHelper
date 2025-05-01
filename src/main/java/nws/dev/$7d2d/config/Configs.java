package nws.dev.$7d2d.config;

import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.system._File;
import nws.dev.$7d2d.system._Log;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Configs {
    private static final Font EmptyFont = new Font("Arial", Font.PLAIN, 12);
    public static Font font;
    private static final HashMap<String, ServerData> servers = new HashMap<>();


    public static void init(){
        LoadServerData();
        LoadFont();
    }

    public static void LoadFont(){

        if (FontConfig.I.isCustom()) {
            _Log.debug("开始加载自定义字体");
            String fontPath = DataTable.FontDir+"/"+FontConfig.I.getFontName();
            try {
                File fontFile = new File(fontPath);
                if (!fontFile.exists()) {
                    _Log.error("字体文件未找到: " + fontPath);
                    font = EmptyFont;
                    return;
                }
                try (InputStream fontStream = new FileInputStream(fontFile)) {
                    font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(FontConfig.I.getLineHeight());
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(font);
                }

            } catch (IOException | FontFormatException e) {
                _Log.error("加载字体文件失败: " + e.getMessage());
                font = EmptyFont;
            }
        }else {
            _Log.debug("开始加载默认字体");
            try {
                InputStream fontStream = DataTable.class.getResourceAsStream("/font/NotoSansSC-Regular.otf");
                if (fontStream == null) {
                    _Log.error("未找到字体文件");
                    return;
                }
                font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(FontConfig.I.getLineHeight());
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
            } catch (IOException | FontFormatException e) {
                _Log.error(e.getMessage());
            }
        }
    }
    public static void LoadServerData() {
        servers.clear();
        _File.getFiles(DataTable.ServerConfigDir,".json").forEach(path -> {
            ServerData serverDataIO = new ServerDataIO(path.getFileName().toString()).getDatas();
            if (serverDataIO != null) servers.put(getFileNameWithoutExtension(path.getFileName().toString()),serverDataIO);
        });
    }

    public static ServerData getServerData(String key){
        return servers.getOrDefault(key,null);
    }

    public static String getFileNameWithoutExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(0, dotIndex);
        } else {
            return filename;
        }
    }
}
