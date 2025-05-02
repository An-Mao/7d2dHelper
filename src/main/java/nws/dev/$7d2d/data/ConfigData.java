package nws.dev.$7d2d.data;

import java.util.List;

public record ConfigData(boolean outputDefaultConfig,boolean logColor, boolean isDebug, int listenPort, String qqHost, List<String> admin) {
}
