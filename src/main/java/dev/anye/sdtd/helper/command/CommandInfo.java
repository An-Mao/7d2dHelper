package dev.anye.sdtd.helper.command;

import dev.anye.sdtd.helper.data.Permission;

public record CommandInfo(Class<?> commandClass, Permission permission, CommandType type,int priority,String desc) {
}
