package dev.anye.sdtd.helper.data;

import java.util.List;

public record ConfigData(boolean outputDefaultConfig,boolean logColor, boolean isDebug, int listenPort, String qqHost, List<String> admin) {
}
