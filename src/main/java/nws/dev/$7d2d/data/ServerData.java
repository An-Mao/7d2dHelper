package nws.dev.$7d2d.data;

import java.util.List;

public record ServerData(boolean logColor, String serverName, boolean isDebug, String adminToken, String kitHost, String kitUsername, String kitPassword, String botHost, String botUsername, String botPassword, String clearSetTime, String actualRestartTime, int waitTime, int listenPort, int heartInterval, int msgCount, int voteTime, float voteScale, int voteCooldown, String qqHost, List<String> adminQQ, int singInMaxPoint, int restartMode, int restartTime, String kitExePath, List<String> qqGroup, String acHost, String acUsername, String acPassword, int unBanNum, boolean bindNeedGameMsg, int qqMsgType, boolean enableKitHeartbeat, String mapName, int recordItemDefault, int recordItemLimit, int recordItemPoint, String GameDir, boolean imageRecipes, boolean imageItem){
}
