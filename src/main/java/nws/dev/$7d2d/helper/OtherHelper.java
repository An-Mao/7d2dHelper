package nws.dev.$7d2d.helper;

public class OtherHelper {
    public static String removeColorCodes(String text) {
        return text.replaceAll("\\[[0-9a-fA-F]{6}\\]", "");
    }
}
