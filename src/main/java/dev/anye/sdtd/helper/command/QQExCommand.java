package dev.anye.sdtd.helper.command;

import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

public abstract class QQExCommand extends QQCommand {
    protected final String[] args;
    protected final String rawArg;

    public QQExCommand(String c, QQData.Message message, ServerCore serverCore) {
        super(c,message,serverCore);
        if (msg.contains(" ")) {
            this.args = msg.split(" ");
            StringBuilder s = new StringBuilder();
            for (int i = 1; i < this.args.length; i++) {
                if (!s.isEmpty()) s.append(" ");
                s.append(this.args[i]);
            }
            this.rawArg = s.toString();
        } else {
            this.args = new String[0];
            this.rawArg = "";
        }
    }

    public static String convertPath(String inputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            return inputPath;
        }

        // 1. 确保使用 "\" 作为分隔符 (先替换双反斜杠为单反斜杠)
        String replacedSlashes = inputPath.replace("/", "\\").replace("\\\\", "\\");

        // 2. 处理盘符后的反斜杠
        StringBuilder sb = new StringBuilder(replacedSlashes);
        if (sb.length() >= 2 && sb.charAt(1) == ':') {
            // 确保盘符后有两个反斜杠
            if (sb.length() >= 3) {
                if (sb.charAt(2) != '\\') {
                    sb.insert(2, '\\');
                }
            } else {
                sb.append("\\");
            }

            if (sb.length() >= 4) {
                if (sb.charAt(3) != '\\') {
                    sb.insert(3, '\\');
                }
            } else {
                sb.append("\\");
            }
        }

        // 3. 构建 file:/// URI
        String fileUri = "file:///" + sb.toString();

        return fileUri;
    }
    public static String convertPathToUri(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return filePath; // 或者抛出异常
        }

        // 1. 替换反斜杠为正斜杠
        String unixPath = filePath.replace("\\", "/");

        // 2. 处理盘符 (如果存在)
        if (unixPath.length() >= 2 && unixPath.charAt(1) == ':') {
            unixPath = unixPath.substring(0, 2) + "//" + unixPath.substring(2);
        }

        // 3. 添加 file:/// 前缀
        return "file:///" + unixPath;
    }







    public boolean argCheck(int length) {
        return this.args.length != length;
    }
}
