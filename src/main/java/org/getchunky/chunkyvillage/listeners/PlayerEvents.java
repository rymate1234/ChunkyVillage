package org.getchunky.chunkyvillage.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class PlayerEvents extends PlayerListener{
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        //Try to re-spawn at town.
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);
        if(chunkyTown==null) return;
        event.setRespawnLocation(chunkyTown.getHome().getCoord().toLocation());}

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Log join time
        new ChunkyResident(event.getPlayer()).login();
    }


    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Save playtime
        new ChunkyResident(event.getPlayer()).logout();


    }
}
