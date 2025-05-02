package nws.dev.$7d2d.command;

import nws.dev.$7d2d.data.Permission;

public record CommandInfo(Class<?> commandClass, Permission permission, CommandType type) {
}
