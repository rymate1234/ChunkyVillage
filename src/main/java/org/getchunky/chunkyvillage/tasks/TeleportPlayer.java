package org.getchunky.chunkyvillage.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class TeleportPlayer extends PlayerTask{

    private Location location;

    public TeleportPlayer(Player player, Location location) {
        super(player);
        this.location = location;
    }

    public void run() {
        if(!player.isOnline()) return;
        player.teleport(location);
    }
}
