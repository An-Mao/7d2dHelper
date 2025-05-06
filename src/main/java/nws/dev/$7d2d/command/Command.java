package nws.dev.$7d2d.command;

import nws.dev.$7d2d.data.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可见
@Target(ElementType.TYPE) // 注解只能用于类
public  @interface Command {
    String name() default "";
    /**
     * Permission
     * 0 all command
     * 1 server command
     * 2 use command
     * @return 0 - 2;
     */
    Permission permission() default Permission.Admin;
    CommandType type() default CommandType.All;
    int priority() default 1000;
    String desc() default "";
}
