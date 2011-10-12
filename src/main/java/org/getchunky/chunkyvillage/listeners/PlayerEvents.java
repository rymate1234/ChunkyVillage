package org.getchunky.chunkyvillage.listeners;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class PlayerEvents extends PlayerListener{
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);
        if(chunkyTown==null) return;
        event.setRespawnLocation(chunkyTown.getHome().getCoord().toLocation());
    }
}
