package nws.dev.$7d2d.command;

import nws.dev.$7d2d.system._Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandRegistry {

    private static final Map<String, Class<?>> commandMap = new HashMap<>();

    public static Map<String, Class<?>> getCommandMap() {
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
                    // 从 JAR 文件加载类
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    JarFile jarFile = new JarFile(new File(jarPath));
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class") && entry.getName().startsWith(path)) {
                            String className = entry.getName().replace("/", ".").replace(".class", "");
                            try {
                                Class<?> clazz = Class.forName(className);
                                if (clazz.isAnnotationPresent(Command.class) && !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                                    Command commandAnnotation = clazz.getAnnotation(Command.class);
                                    String commandName = commandAnnotation.name();
                                    if (commandName == null || commandName.isEmpty()) {
                                        commandName = clazz.getSimpleName().toLowerCase().replace("command", ""); // 默认命令名为类名小写
                                    }
                                    commandMap.put(commandName, clazz);
                                }
                            } catch (ClassNotFoundException e) {
                                System.err.println("Class not found: " + className);
                            }
                        }
                    }
                    jarFile.close();

                } else {
                    // 从文件系统加载类
                    File dir = new File(resource.getFile());

                    if (dir.exists()) {
                        String[] classFiles = dir.list((d, name) -> name.endsWith(".class"));

                        if (classFiles != null) {
                            for (String classFile : classFiles) {
                                String className = packageName + "." + classFile.substring(0, classFile.length() - 6);
                                try {
                                    Class<?> clazz = Class.forName(className);
                                    if (clazz.isAnnotationPresent(Command.class) && !Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                                        Command commandAnnotation = clazz.getAnnotation(Command.class);
                                        String commandName = commandAnnotation.name();
                                        if (commandName == null || commandName.isEmpty()) {
                                            commandName = clazz.getSimpleName().toLowerCase().replace("command", ""); // 默认命令名为类名小写
                                        }
                                        commandMap.put(commandName, clazz);
                                    }
                                } catch (ClassNotFoundException e) {
                                    System.err.println("Class not found: " + className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            _Log.error(e.getMessage());
        }
    }
}
