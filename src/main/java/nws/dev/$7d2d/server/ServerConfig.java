package nws.dev.$7d2d.server;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.command.CommandRegistryNew;
import nws.dev.$7d2d.config.*;
import nws.dev.$7d2d.data.PlayerInfoData;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.core.system._File;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ServerConfig {
    private static final Font EmptyFont = new Font("Arial", Font.PLAIN, 12);

    public final ServerData serverData;
    public final String severConfigDir ;
    public final String severDataDir ;
    public final String severUserDataDir ;
    public final HashMap<String,Boolean> command;
    public final HashMap<String,String> qa;
    public final StringBuilder question = new StringBuilder();

    public final ACItemsConfig acItem;
    public final EventListConfig eventList;
    public final EventsConfig events;
    public final AutoWhiteList autoWhiteList;
    public final FontConfig fontConfig;
    public final DrawConfig drawConfig;
    public final GameInfo gameInfo;
    public final DailyRewardsConfig dailyRewardsConfig;
    public final AutoWhiteListConfig autoWhiteListConfig;
    public final ItemAttributeConfig itemAttributeConfig;
    public final RewardConfig rewardConfig;
    public final RaffleConfig raffleConfig;
    public final LanguageConfig languageConfig;


    public long wt = 0;
    public final HashMap<String, Long> saveItem = new HashMap<>();
    public final HashMap<String, List<PlayerInfoData.ItemData>> playerSaveItem = new HashMap<>();
    public final HashMap<String, Integer> playerSaveItemIndex = new HashMap<>();
    public final HashMap<String, String> bindUser = new HashMap<>();
    public final HashMap<String, List<String>> banUser = new HashMap<>();
    public final String imageCache;

    public boolean isVote = false;
    public long voteStartTime = 0;
    public final List<String> voteList = new ArrayList<>();

    public boolean isCooldown = false;

    public Font font;
    public final List<String> no = new ArrayList<>();
    protected final String onlinePlayerUrl;
    public final String userItemDir;
    protected final String commandUrl;


    public ServerConfig(ServerData serverData){
        $7DTD._Log.info("==========开始加载服务器配置==========","服务器配置名称："+serverData.serverName());
        this.onlinePlayerUrl = "http://"+ serverData.kitHost() +"/api/getplayersonline?admintoken="+serverData.adminToken();
        this.commandUrl = "http://"+ serverData.kitHost() +"/api/executeconsolecommand?admintoken="+serverData.adminToken();

        this.serverData = serverData;
        severConfigDir = DataTable.ServerConfigDir +"/"+serverData.serverName();
        _File.checkAndCreateDir(severConfigDir);

        severDataDir = DataTable.ServerDataDir+"/"+serverData.serverName();
        _File.checkAndCreateDir(severDataDir);
        severUserDataDir = severDataDir+"/user";
        _File.checkAndCreateDir(severUserDataDir);
        userItemDir = severUserDataDir +"/item";
        _File.checkAndCreateDir(userItemDir);
        imageCache = severDataDir+"/image";
        _File.checkAndCreateDir(imageCache);



        $7DTD._Log.info("开始加载命令配置");
        if (!new File(getConfigPre()+"command.json").exists()){
            $7DTD._Log.info("开始输出默认命令配置");
            HashMap<String, Boolean> map = new HashMap<>();
            CommandRegistryNew.getCommands().forEach((s, priorityQueue) -> map.put(s, true));
            command = map;
            CommandConfig commandConfig = new CommandConfig(getConfigPre()+"command.json");
            commandConfig.getDatas().clear();
            commandConfig.getDatas().putAll(command);
            commandConfig.save();

        }else command = new CommandConfig(getConfigPre()+"command.json").getDatas();

        $7DTD._Log.info("开始加载QA文件");
        qa = new QA(getConfigPre()+"QA.json").getDatas();
        qa.keySet().forEach(s -> question.append("\\n").append(s));

        $7DTD._Log.info("开始加载礼包");
        rewardConfig = new RewardConfig(getConfigPre() + "reward.json");

        $7DTD._Log.info("开始加载事件文件");
        events = new EventsConfig(getConfigPre() + "events.json");
        $7DTD._Log.info("开始加载事件信息");
        eventList = new EventListConfig(getConfigPre()+"EventList.json");

        $7DTD._Log.info("开始加载手动白名单列表");
        acItem = new ACItemsConfig(getConfigPre()+"ACItems.json");
        $7DTD._Log.info("开始加载自动白名单");
        autoWhiteListConfig = new AutoWhiteListConfig(getConfigPre()+"AutoWhite.json");
        autoWhiteList = new AutoWhiteList(this.severDataDir +"/AutoWhiteList.json");

        $7DTD._Log.info("开始加载字体");
        fontConfig = new FontConfig(getConfigPre()+"font.json");
        loadFont();
        $7DTD._Log.info("开始加载画板");
        drawConfig = new DrawConfig(getConfigPre()+"draw.json",font);

        $7DTD._Log.info("开始加载物品属性文件");
        itemAttributeConfig = new ItemAttributeConfig(getConfigPre() + "ItemAttributes.json");

        $7DTD._Log.info("开始加载每日奖励");
        dailyRewardsConfig = new DailyRewardsConfig(getConfigPre()+"DailyReward.json");

        $7DTD._Log.info("开始加载游戏文件");
        gameInfo = new GameInfo(this);

        $7DTD._Log.info("开始加载抽奖文件");
        raffleConfig = new RaffleConfig(getConfigPre() + "raffle.json");
        //raffleConfig.getDatas().items.forEach((rewardData) -> $7DTD._Log.info("加载抽奖物品[" + rewardData .reward().name+"]权重：" + rewardData.weight()));

        $7DTD._Log.info("开始加载语言文件");
        languageConfig = new LanguageConfig(getConfigPre() + "lang.json");

    }


    public String getConfigPre(){
        return this.severConfigDir +"/";
    }


    public void loadFont(){

        if (fontConfig.isCustom()) {
            $7DTD._Log.debug("开始加载自定义字体");
            String fontPath = DataTable.FontDir+"/"+fontConfig.getFontName();
            try {
                File fontFile = new File(fontPath);
                if (!fontFile.exists()) {
                    $7DTD._Log.error("字体文件未找到: " + fontPath);
                    font = EmptyFont;
                    return;
                }
                try (InputStream fontStream = new FileInputStream(fontFile)) {
                    font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontConfig.getLineHeight());
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(font);
                }

            } catch (IOException | FontFormatException e) {
                $7DTD._Log.error("加载字体文件失败: " + e.getMessage());
                font = EmptyFont;
            }
        }else {
            $7DTD._Log.debug("开始加载默认字体");
            try {
                InputStream fontStream = DataTable.class.getResourceAsStream("/assets/font/NotoSansSC-Regular.otf");
                if (fontStream == null) {
                    $7DTD._Log.error("未找到字体文件");
                    return;
                }
                font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontConfig.getLineHeight());
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
            } catch (IOException | FontFormatException e) {
                $7DTD._Log.error(e.getMessage());
            }
        }
    }


    public UserConfig getUserData(String qq){
        return new UserConfig(severUserDataDir+"/"+qq+".json");
    }
    public SingInConfig getSignData(String qq){
        return new SingInConfig(severUserDataDir + "/"+qq+".sign.json");
    }



    public void reloadConfig(){
        $7DTD._Log.info("即将重新加载配置");
        Config.I.init();
        $7DTD._Log.info("重新加载配置完成");
    }

    public String getTranslate(String key){
        return languageConfig.getLang(key);
    }
}
