package org.getchunky.chunkyvillage.tasks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class PlayerTask implements Runnable{

    protected Player player;

    public PlayerTask(Player player) {
        this.player = player;
    }
}
