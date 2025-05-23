package dev.anye.sdtd.helper.config;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.server.ServerConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameInfo {
    //public static final GameInfo I = new GameInfo(Config.I.getDatas().GameDir);
    private final ServerConfig serverCore;
    private final String gameDir;
    private final ArrayList<String> ModFiles = new ArrayList<>();
    private final HashMap<String,Item> items = new HashMap<>();
    private final HashMap<String,Recipe> recipes = new HashMap<>();
    private final HashMap<String,String> language = new HashMap<>();
    public GameInfo(ServerConfig serverCore) {
        this.serverCore = serverCore;
        this.gameDir = serverCore.serverData.GameDir();
        this.ModFiles.add(this.gameDir + "/Data/");
        getSubdirectories().forEach(file -> this.ModFiles.add(file.getPath()));
        init();
    }

    public void init() {
        loadItems();
        loadRecipes();
        loadLanguage();
    }

    public String getItemInfo(String name) {
        if(items.containsKey(name)) {
            StringBuilder sb = new StringBuilder();
            Item item = items.get(name);
            $7DTD._Log.debug(item.toString());
            sb.append("名称：").append(getLocalization(item.name())).append("\\n-----描述-----\\n").append(getLocalization(item.description())).append("\\n-----属性-----\\n").append(item.attribute());
            return sb.toString();
        }
        return "无此物品信息";
    }
    public String getRecipeInfo(String name) {
        if(recipes.containsKey(name)) {
            StringBuilder sb = new StringBuilder();
            Recipe recipe = recipes.get(name);
            sb.append("名称：").append(getLocalization(recipe.name));
            sb.append("\\n合成位置：").append(getLocalization(recipe.craft_area));
            sb.append("\\n合成数量：").append(recipe.count);
            sb.append("\\n合成材料：");
            recipe.items.forEach((k,v)->sb.append("\\n").append(v).append("x ").append(getLocalization(k)));
            return sb.toString();
        }
        return "无此配方信息";
    }

    public String getLocalization(String key) {
        return language.getOrDefault(key,key);
    }

    public String findItem(String itemName){
        StringBuilder s = new StringBuilder("-----查找结果-----");
        int[] c = {0};

        getItems().forEach((k, v) -> {
            String name = getLocalization(v.name());
            if (k.equalsIgnoreCase(itemName) || name.equalsIgnoreCase(itemName) || v.name.contains(itemName)){
                c[0]++;
                s.append("\\n").append(k).append("（").append(name).append("）");
            }else {
                if (name.length() < 20) {
                    if (name.contains(itemName)){
                        c[0]++;
                        s.append("\\n").append(k).append("（").append(name).append("）");
                    }
                }
            }
        });

        if (c[0] > 15){
            return ("查找结果过多，请详细描述要查找的物品");
        }else if (c[0] == 0) {
            return ("未找到相关物品");
        }
            return (s.toString());
    }


    public HashMap<String, Item> getItems() {
        return items;
    }

    public HashMap<String, Recipe> getRecipes() {
        return recipes;
    }

    public HashMap<String, String> getLanguage() {
        return language;
    }

    private void loadLanguage() {
        language.clear();
        ModFiles.forEach(file -> {
            String filePath = file + "/config/localization.txt";
            File l = new File(filePath);
            if (!l.exists()) return;
            try (BufferedReader br = new BufferedReader(new FileReader(l))) {
                String line;
                StringBuilder currentLine = new StringBuilder();
                boolean inQuotes = false;
                int chineseIndex = -1;
                int englishIndex = -1;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    currentLine.append(line).append("\n"); // 添加换行符，因为 readLine() 会移除换行符
                    // 检查当前行是否包含未闭合的引号
                    int quoteCount = 0;
                    for (int i = 0; i < currentLine.length(); i++) {
                        if (currentLine.charAt(i) == '"') {
                            quoteCount++;
                        }
                    }
                    inQuotes = (quoteCount % 2 != 0);

                    // 如果引号已闭合，则解析该行
                    if (!inQuotes) {
                        List<String> parts = parseCsvLine(currentLine.toString().trim()); // 使用之前的 parseCsvLine 方法
                        currentLine.setLength(0);
                        if (chineseIndex == -1) {
                            for (int i = 0; i < parts.size(); i++) {
                                if (parts.get(i).equalsIgnoreCase("schinese")) chineseIndex = i;
                                if (parts.get(i).equalsIgnoreCase("english")) englishIndex = i;
                            }
                            if (chineseIndex == -1 && englishIndex != -1) chineseIndex = englishIndex;
                            if (chineseIndex != -1 && englishIndex == -1) englishIndex = chineseIndex;
                        } else {
                            if (language.containsKey(parts.get(0))) {
                                $7DTD._Log.debug("语言文件解析异常，已存在相同的Key。key = " + parts.get(0)+", value = "+language.get(parts.get(0)),filePath);
                                continue;
                            }
                            if (parts.size() <= chineseIndex) {
                                if (chineseIndex != englishIndex && parts.size() > englishIndex) {
                                    language.put(parts.get(0), parts.get(englishIndex));
                                }else $7DTD._Log.debug("语言文件格式错误", parts.get(0), String.valueOf(parts.size() ));
                                continue;
                            }else language.put(parts.get(0), parts.get(chineseIndex));
                        }
                    }
                }

                // 处理文件末尾可能存在的未处理行
                if (!currentLine.isEmpty()) {
                    List<String> parts = parseCsvLine(currentLine.toString().trim());
                    if (chineseIndex != -1){
                        if (language.containsKey(parts.get(0))) {
                            $7DTD._Log.debug("语言文件解析异常，已存在相同的Key。key = " + parts.get(0)+", value = "+language.get(parts.get(0)),filePath);
                            return;
                        }
                        if (parts.size() <= chineseIndex) {
                            if (chineseIndex != englishIndex && parts.size() > englishIndex) {
                                language.put(parts.get(0), parts.get(englishIndex));
                            }else $7DTD._Log.debug("语言文件格式错误", parts.get(0), String.valueOf(parts.size() ));
                        }else language.put(parts.get(0), parts.get(chineseIndex));
                    }
                }

            } catch (IOException e) {
                $7DTD._Log.error("读取语言文件出错: " + e.getMessage());
            }
            /*
            try (BufferedReader br = new BufferedReader(new FileReader(file + "/config/localization.txt"))) {
                int chineseIndex = -1;
                int englishIndex = -1;
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    List<String> parts = parseCsvLine(line);
                    if (chineseIndex == -1) {
                        for (int i = 0; i < parts.size(); i++) {
                            if (parts.get(i).equalsIgnoreCase("schinese")) chineseIndex = i;
                            if (parts.get(i).equalsIgnoreCase("english")) englishIndex = i;
                        }
                    } else {
                        if (parts.size() <= chineseIndex) {
                            $7DTD._Log.error("语言文件格式错误", parts.get(0), String.valueOf(parts.size() ));
                            continue;
                        }else language.put(parts.get(0), parts.get(chineseIndex));
                    }
                }
            } catch (IOException e) {
                $7DTD._Log.error("读取语言文件出错: " + e.getMessage());
            }

             */
        });
    }
    public List<String> oldparseCsvLine(String csvLine) {
        List<String> parts = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentPart = new StringBuilder();

        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // 切换引号状态
            } else if (c == ',' && !inQuotes) {
                // 遇到逗号且不在引号内，则完成一个字段的解析
                parts.add(currentPart.toString());
                currentPart.setLength(0); // 清空 StringBuilder
            } else {
                currentPart.append(c); // 将字符添加到当前字段
            }
        }
        // 添加最后一个字段
        parts.add(currentPart.toString());
        return parts;
    }
    public List<String> parseCsvLine(String csvLine) {
        List<String> parts = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentPart = new StringBuilder();

        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // 切换引号状态
            } else if (c == ',' && !inQuotes) {
                // 遇到逗号且不在引号内，则完成一个字段的解析
                parts.add(currentPart.toString());
                currentPart.setLength(0); // 清空 StringBuilder
            } else {
                currentPart.append(c); // 将字符添加到当前字段
            }
        }

        // 添加最后一个字段
        parts.add(currentPart.toString());

        return parts;
    }
    private void loadItems() {
        items.clear();
        ModFiles.forEach(file -> {
            List<Element> items1 = getElementsFromXMLFile(file + "/config/items.xml", "/items/item");
            loadItem(items1);
            List<Element> items2 = getElementsFromXMLFile(file + "/config/items.xml", "/configs/append/item");
            loadItem(items2);
            List<Element> items3 = getElementsFromXMLFile(file + "/config/item_modifiers.xml", "/item_modifiers/item_modifier");
            loadItem(items3);
            List<Element> items4 = getElementsFromXMLFile(file + "/config/item_modifiers.xml", "/configs/append/item_modifier");
            loadItem(items4);

        });
    }
    private void loadItem(List<Element> elements) {
        elements.forEach(item->{
            String name = item.getAttribute("name");
            if(name.isEmpty() || items.containsKey(name)) return;
            String description = name+"Desc";
            NodeList propertyNodes = item.getElementsByTagName("property");
            for (int i = 0; i < propertyNodes.getLength(); i++) {
                Element property = (Element) propertyNodes.item(i);
                if (property.getAttribute("name").equalsIgnoreCase("DescriptionKey")) description = property.getAttribute("value");
            }
            NodeList effectGroupNodes = item.getElementsByTagName("effect_group");
            StringBuilder att = new StringBuilder();
            for (int i = 0; i < effectGroupNodes.getLength(); i++){
                Element element = (Element) effectGroupNodes.item(i);
                String a = serverCore.itemAttributeConfig.attributeToString(element);
                if (a.isEmpty())continue;
                String en = element.getAttribute("name");
                if (!att.isEmpty()) att.append("\\n");
                if (en.isEmpty()) att.append("【未命名效果组】");
                else att.append("【").append(en).append("】");
                att.append("\\n").append(a);
            }
            items.put(item.getAttribute("name"),new Item(name,name,description,att.toString()));
        });
    }
    private void loadRecipes() {
        recipes.clear();
        ModFiles.forEach(file -> {
            String filePath = file + "/config/recipes.xml";
            $7DTD._Log.debug("加载配方",filePath);
            List<Element> items1 = getElementsFromXMLFile(filePath, "/recipes/recipe");
            loadRecipe(items1);
            List<Element> items2 = getElementsFromXMLFile(filePath, "/configs/append/recipe");
            loadRecipe(items2);
        });
    }
    private boolean isClear = false;
    private void loadRecipe(List<Element> elements) {
        boolean auto = this.serverCore.autoWhiteListConfig.isEnable();
        if (auto && !isClear) {
            isClear = true;
            this.serverCore.autoWhiteList.clear();
        }
        elements.forEach(item->{
            String name = item.getAttribute("name");
            if(name.isEmpty() || recipes.containsKey(name)) return;
            int count = Integer.parseInt(item.getAttribute("count"));
            String craft_area = item.getAttribute("craft_area");
            NodeList propertyNodes = item.getElementsByTagName("ingredient");
            HashMap<String,Integer> items = new HashMap<>();
            for (int i = 0; i < propertyNodes.getLength(); i++) {
                Element property = (Element) propertyNodes.item(i);
                items.put(property.getAttribute("name"),Integer.parseInt(property.getAttribute("count")));
            }
            Recipe recipe = new Recipe(name,name,count,craft_area,items);
            if (auto) serverCore.autoWhiteListConfig.checkRecipe(serverCore,recipe);

            //recipes.put(item.getAttribute("name"),recipe);
            recipes.put(name,recipe);
        });
        if (auto) serverCore.autoWhiteList.saveAll();
    }
    public List<Element> getElementsFromXMLFile(String xmlFilePath, String xpathExpression) {
        List<Element> items = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            if (!xmlFile.exists()) return items;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 读取文件时指定字符编码，避免中文乱码问题
            FileInputStream fis = new FileInputStream(xmlFile);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8); // 使用 UTF-8 编码
            InputSource inputSource = new InputSource(isr);

            Document doc = builder.parse(inputSource);  // 使用 InputSource 对象

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile(xpathExpression);

            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    items.add((Element) node);
                }
            }
            isr.close(); // 关闭 InputStreamReader
            fis.close(); // 关闭 FileInputStream

        } catch (Exception e) {
            $7DTD._Log.error(e.getMessage());
            //e.printStackTrace();
        }
        return items;
    }
    public List<File> getSubdirectories() {
        List<File> subdirectories = new ArrayList<>();
        File directory = new File(this.gameDir+"/Mods");
        if (!directory.exists() || !directory.isDirectory()) {
            return subdirectories;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    subdirectories.add(file);
                }
            }
        }
        return subdirectories;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "gameDir='" + gameDir + '\'' +
                ", ModFiles=" + ModFiles +
                ", items=" + items +
                '}';
    }

    public record Item(String id,String name,String description,String attribute) {
        @Override
        public String toString() {
            return "Item{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", attribute='" + attribute + '\'' +
                    '}';
        }
    }
    public record Recipe(String id,String name,int count,String craft_area,HashMap<String,Integer> items) {}















}
