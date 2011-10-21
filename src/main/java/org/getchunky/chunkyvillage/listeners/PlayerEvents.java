package org.getchunky.chunkyvillage.listeners;

import org.bukkit.event.player.*;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunkyvillage.ChatManager;
import org.getchunky.chunkyvillage.config.Config;
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.objects.TownChunk;
import org.getchunky.chunkyvillage.permissions.Permissions;

public class PlayerEvents extends PlayerListener{
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        //Try to re-spawn at town.
        ChunkyResident chunkyResident = new ChunkyResident(event.getPlayer());
        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown==null) return;
        chunkyResident.subtractPlayTime(Config.Options.DEATH_TOLL.getInt());
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
        if(Permissions.TELEPORT.has(event.getPlayer())) return;
        if(event.getFrom().getWorld().equals(event.getTo().getWorld()) && event.getFrom().distance(event.getTo()) < 5) return;

        ChunkyResident chunkyResident = new ChunkyResident(event.getPlayer());
        ChunkyTown myTown = chunkyResident.getTown();

        if(myTown == null) return;

        TownChunk fromChunk = new TownChunk(ChunkyManager.getChunkyChunk(event.getFrom()));
        TownChunk toChunk = new TownChunk(ChunkyManager.getChunkyChunk(event.getTo()));

        ChunkyTown fromTown = fromChunk.getTown();

        if(fromTown != null && fromTown.getEffectiveStance(myTown) != ChunkyTown.Stance.ALLY) {
            Strings.NO_TELEPORT.bad(chunkyResident);
            event.setCancelled(true);
            return;
        }

        ChunkyTown toTown = toChunk.getTown();

        if(toTown != null && toTown.getEffectiveStance(myTown) != ChunkyTown.Stance.ALLY) {
            Strings.NO_TELEPORT.bad(chunkyResident);
            event.setCancelled(true);
            return;
        }

    }
}
