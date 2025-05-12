package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.$7DTD;
import nws.dev.core.draw._Image;
import nws.dev.core.json._JsonConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrawConfig extends _JsonConfig<DrawConfig.DrawData> {
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("\\[([0-9A-Fa-f]{6})\\]");
    private static final List<String> colorText=List.of(
            "0","1","2","3","4","5","6","7","8","9",
            "a","b","c","d","e","f");

    //public static final DrawConfig I = new DrawConfig();
    private BufferedImage backgroundImage;
    private final BufferedImage bufferedImage;
    private final Graphics graphics;
    private final FontMetrics fontMetrics;
    private final Font font;
    public DrawConfig(String filePath, Font font) {
        super(filePath, """
                {
                    "width": 256,
                    "backgroundType": 1,
                    "background": "img.png",
                    "backgroundColor": "#505050",
                    "textColor": "#FFD700",
                    "left": 10,
                    "top": 10,
                    "scaleFactor":1
                }
                """, new TypeToken<>(){});
        try {
            File file = new File(filePath);
            backgroundImage = ImageIO.read(file);
        } catch (IOException e) {
            $7DTD._Log.error(e.getMessage());
        }
        bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
        graphics = bufferedImage.getGraphics();
        this.font = font;
        graphics.setFont(this.font);
        fontMetrics = graphics.getFontMetrics();
    }

    public void dispose() {
        if (graphics != null) {
            graphics.dispose();
        }
    }

    @Override
    public DrawData getDatas() {
        //if (super.getDatas() == null) return new DrawData(256,1, "img.png", "#505050", "#000000", 10, 10,1);
        return super.getDatas();
    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }
    public List<String> newlineText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String remainingText = text;
        String currentColorCode = ""; // 用于保存当前颜色代码

        while (!remainingText.isEmpty()) {
            int lineEndIndex = findLineEndIndex(remainingText, maxWidth);
            String line = remainingText.substring(0, lineEndIndex);
            Matcher matcher = COLOR_CODE_PATTERN.matcher(line);
            String lineEndingColorCode = "";
            while (matcher.find()) {
                lineEndingColorCode = matcher.group(0);
            }
            lines.add(currentColorCode + line);
            remainingText = remainingText.substring(lineEndIndex);
            // 如果当前行以颜色代码结尾，则更新 currentColorCode
            if (!lineEndingColorCode.isEmpty()) {
                currentColorCode = lineEndingColorCode;
            } else if (!remainingText.isEmpty()){
                //如果新行不是空字符串，并且没有颜色代码，则保留之前的颜色代码
                //currentColorCode = ""; // 重置颜色代码
            }
        }

        return lines;
    }
    public int findLineEndIndex(String text, int maxWidth) {
        int textLength = text.length();
        boolean iscolor = false;
        //StringBuilder tmp = new StringBuilder();
        StringBuilder tmpColor = new StringBuilder();
        StringBuilder tmpString = new StringBuilder();
        for (int i = 0; i < textLength; i++) {
            String currentChar = text.substring(i, i + 1);
            if (iscolor){
                if (tmpColor.length() < 7){
                    if (colorText.contains(currentChar.toLowerCase())) {
                        tmpColor.append(currentChar);
                        continue;
                    }
                    iscolor = false;
                }else if (tmpColor.length() == 7){
                    iscolor = false;
                    if (currentChar.equals("]") ) {
                        tmpColor.append(currentChar);
                        //tmp.append(tmpColor);
                        continue;
                    }
                }
            }else {
                if (currentChar.equals("[")) {
                    iscolor = true;
                    tmpColor = new StringBuilder("[");
                    continue;
                }
            }
            tmpString.append(currentChar);
            int charWidth = fontMetrics.stringWidth(tmpString.toString());
            if (charWidth > maxWidth) return i;
        }
        return textLength;
    }

    private int oldfindLineEndIndex(String text, int maxWidth) {
        int textLength = text.length();
        int lineEndIndex = textLength;
        int currentWidth = 0;

        for (int i = 0; i < textLength; i++) {
            String currentChar = text.substring(i, i + 1);
            Matcher matcher = COLOR_CODE_PATTERN.matcher(currentChar);

            if (matcher.matches()) {
                // 颜色代码不计入宽度
            } else {
                int charWidth = fontMetrics.stringWidth(currentChar);
                if (currentWidth + charWidth > maxWidth) {
                    // 检查是否需要回退以避免截断颜色代码
                    if (i > 0 && currentChar.equals("]")) {
                        int j = i - 1;
                        while (j >= 0 && text.charAt(j) != '[') {
                            j--;
                        }
                        if (j >= 0) {
                            lineEndIndex = j; // 回退到颜色代码的开头
                            break;
                        }
                    }
                    lineEndIndex = i; // 否则，在此处截断
                    break;
                }
                currentWidth += charWidth;
            }
        }

        return lineEndIndex;
    }
    public List<String> lineText(String text, int maxWidth){
        int width = fontMetrics.stringWidth(text);
        if (width > maxWidth) {
            List<String> lines = new ArrayList<>();
            String remainingText = text;

            // 如果宽度大于限制的宽度，每次截取小于或等于宽度的字符存入 lines，
            // 将截取后的剩余字符重新判断，循环至不再超过为止
            while (fontMetrics.stringWidth(remainingText) > maxWidth) {
                int i = 0;
                String truncatedText = "";
                // 找到小于或等于 maxWidth 的最长子字符串
                for (; i < remainingText.length(); i++) {
                    String temp = remainingText.substring(0, i + 1);
                    int tempWidth = fontMetrics.stringWidth(temp);
                    if (tempWidth > maxWidth) {
                        break; // 超过 maxWidth，停止截取
                    }
                    truncatedText = temp;
                }
                // 将截取的文本添加到结果列表
                lines.add(truncatedText);
                // 更新剩余文本，去掉已经截取的部分
                remainingText = remainingText.substring(i);
            }
            // 添加剩余的文本（如果长度不为 0）
            if (!remainingText.isEmpty()) {
                lines.add(remainingText);
            }
            return lines;
        }
        else return List.of(text);
    }
    public _Image createImage(FontConfig fontConfig,List<String> texts) {
        List<String> newTexts = new ArrayList<>();
        texts.forEach(s -> {
            if (!s.isEmpty()){
                newTexts.addAll(newlineText(s,getDatas().width() - getDatas().left() * 2));
            }else {
                newTexts.add(s);
            }
        });
        $7DTD._Log.debug(newTexts.toString());
        int height = newTexts.size() * (int)(fontConfig.getLineHeight()+ 2f) + getDatas().top *2;
        _Image image = new _Image(getDatas().width(), height, getDatas().scaleFactor(), this.font);
        if (getDatas().backgroundType() == 0) {
            image.drawImage(getBackgroundImage(), 0, 0);
        } else if (getDatas().backgroundType() == 1) {
            image.drawRect(0, 0, image.getWidth(), height,Color.decode(getDatas().backgroundColor()) );
        } else if (getDatas().backgroundType() == 2) {
            image.drawRect(0, 0, image.getWidth(), height, Color.decode(getDatas().backgroundColor()));
            image.drawImage(getBackgroundImage(), 0, 0);
        }
        int[] y = {getDatas().top + (int)fontConfig.getLineHeight()};
        Color color = Color.decode(getDatas().textColor());
        newTexts.forEach(text -> {
            image.drawText(text,getDatas().left(), y[0], color);
            y[0] += (int)fontConfig.getLineHeight() + 2;
        });
        return image;
    }

    /**
     * 图片基础数据
     * @param width 宽度
     * @param backgroundType 背景类型 0：图片，1：颜色,2：图片 + 背景
     * @param background 图片路径
     * @param backgroundColor 颜色
     * @param textColor 字体颜色
     * @param left 左间距
     * @param top 上间距
     */
    public record DrawData(int width,int backgroundType ,String background, String backgroundColor, String textColor, int left, int top,int scaleFactor){}
}
