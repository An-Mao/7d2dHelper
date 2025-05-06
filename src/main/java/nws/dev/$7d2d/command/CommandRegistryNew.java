package nws.dev.$7d2d.command;

import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.system._Log;
import nws.dev.$7d2d.system._PriorityMap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandRegistryNew {
    private static final _PriorityMap<String, CommandInfo> Commands = new _PriorityMap<>(Comparator.comparingInt(CommandInfo::priority));

    private static final Map<String, CommandInfo> commandMap = new HashMap<>();

    public static _PriorityMap<String, CommandInfo> getCommands() {
        return Commands;
    }

    public static Map<String, CommandInfo> getCommandMap() {
        return commandMap;
    }

    public static void registerCommands(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("jar")) {
                    // 从 JAR 文件加载类 (保持不变)
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    JarFile jarFile = new JarFile(new File(jarPath));
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class") && entry.getName().startsWith(path)) {
                            String className = entry.getName().replace("/", ".").replace(".class", "");
                            reg(className);
                            /*
                            try {
                                Class<?> clazz = Class.forName(className);
                                if (clazz.isAnnotationPresent(Command.class) && !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                                    Command commandAnnotation = clazz.getAnnotation(Command.class);
                                    String commandName = commandAnnotation.name();
                                    if (commandName == null || commandName.isEmpty()) {
                                        commandName = clazz.getSimpleName().toLowerCase().replace("command", ""); // 默认命令名为类名小写
                                    }
                                    Permission permission = commandAnnotation.permission(); // 获取 permission 属性
                                    CommandType type = commandAnnotation.type();
                                    int priority = commandAnnotation.priority();

                                    // 创建 CommandInfo 对象，存储命令信息
                                    CommandInfo commandInfo = new CommandInfo(clazz, permission, type,priority);
                                    commandMap.put(commandName, commandInfo);
                                    _Log.debug("Registered command: " + commandName + " -> " + clazz.getName());
                                }
                            } catch (ClassNotFoundException e) {
                                _Log.debug("Class not found: " + className);
                            }

                             */
                        }
                    }
                    jarFile.close();

                } else {
                    // 从文件系统加载类
                    File dir = new File(resource.getFile());
                    if (dir.exists()) {
                        processDirectory(dir, packageName); // 使用递归方法处理目录及其子目录
                    }
                }
            }
        } catch (IOException e) {
            _Log.error(e.getMessage());
        }
    }
    private static void processDirectory(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归处理子目录
                    String subPackageName = packageName + "." + file.getName();
                    processDirectory(file, subPackageName);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    reg(className);
                    /*
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(Command.class) && !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                            Command commandAnnotation = clazz.getAnnotation(Command.class);
                            String commandName = commandAnnotation.name();
                            if (commandName == null || commandName.isEmpty()) {
                                commandName = clazz.getSimpleName().toLowerCase().replace("command", ""); // 默认命令名为类名小写
                            }
                            Permission permission = commandAnnotation.permission(); // 获取 permission 属性
                            CommandType type = commandAnnotation.type();

                            // 创建 CommandInfo 对象，存储命令信息
                            CommandInfo commandInfo = new CommandInfo(clazz, permission, type);
                            commandMap.put(commandName, commandInfo);
                            _Log.debug("Registered command: " + commandName + " -> " + clazz.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        _Log.debug("Class not found: " + className);
                    }

                     */
                }
            }
        }
    }

    private static void reg(String className){
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Command.class) && !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                Command commandAnnotation = clazz.getAnnotation(Command.class);
                if (commandAnnotation != null) {
                    String commandName = commandAnnotation.name();
                    // 默认命令名为类名小写
                    if (commandName == null || commandName.isEmpty())
                        commandName = clazz.getSimpleName().toLowerCase().replace("command", "");
                    // 获取 permission 属性
                    Permission permission = commandAnnotation.permission();
                    CommandType type = commandAnnotation.type();
                    int priority = commandAnnotation.priority();
                    // 创建 CommandInfo 对象，存储命令信息
                    CommandInfo commandInfo = new CommandInfo(clazz, commandAnnotation.permission(), commandAnnotation.type(), commandAnnotation.priority());
                    Commands.put(commandName, commandInfo);
                    _Log.debug("Registered command: " + commandName + " -> " + clazz.getName());
                }
            }
        } catch (ClassNotFoundException e) {
            _Log.debug("Class not found: " + className);
        }
    }
}
