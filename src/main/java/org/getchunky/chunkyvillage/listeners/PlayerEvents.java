package org.getchunky.chunkyvillage.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
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
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        chunkyPlayer.getData().put("village-lastJoin",System.currentTimeMillis());
        chunkyPlayer.save();
    }


    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Save playtime
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(event.getPlayer());
        chunkyPlayer.getData().put("village-playTime",ChunkyTownManager.getPlayTime(chunkyPlayer));
        chunkyPlayer.getData().remove("village-lastJoin");
        chunkyPlayer.save();

    }
}
