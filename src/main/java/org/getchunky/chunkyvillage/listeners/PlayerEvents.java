package org.getchunky.chunkyvillage.listeners;

import org.bukkit.event.player.*;
import org.getchunky.chunkyvillage.ChatManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

import static org.getchunky.chunkyvillage.util.Config.Options;

public class PlayerEvents extends PlayerListener{
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        //Try to re-spawn at town.
        ChunkyResident chunkyResident = new ChunkyResident(event.getPlayer());
        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown==null) return;
        chunkyResident.setPlayTime(chunkyResident.getPlayTime()- Options.DEATH_TOLL.getInt());
        event.setRespawnLocation(chunkyTown.getHome().getCoord().toLocation());}

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Log join time
        ChunkyResident chunkyResident = new ChunkyResident(event.getPlayer());
        chunkyResident.login();

        //Apply title
        chunkyResident.applyTitle();
    }


    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Save playtime
        new ChunkyResident(event.getPlayer()).logout();


    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if(ChatManager.handleMessage(event.getPlayer(), event.getMessage())) event.setCancelled(true);
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {

    }
}
