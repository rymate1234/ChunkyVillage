package org.getchunky.chunkyvillage.permissions;

import org.bukkit.command.CommandSender;

public enum Permissions {

    ADMIN("chunky.town.admin"),
    CREATE_TOWN("chunky.town.create"),
    TELEPORT("chunky.town.teleport"),
    STANCE("chunky.town.stance"),
    WARMUP("chunky.town.warmup");

    private String node;

    Permissions(String node) {
        this.node = node;
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(node);
    }

}
