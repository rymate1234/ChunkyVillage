package org.getchunky.chunkyvillage.permissions;

import org.bukkit.command.CommandSender;

public enum Permissions {

    ADMIN("chunky.town.admin");

    private String node;

    Permissions(String node) {
        this.node = node;
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(node);
    }

}
