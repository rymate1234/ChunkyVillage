package org.getchunky.chunkyvillage.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkChangeEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkClaimEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerListener;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyCoordinates;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.objects.TownChunk;

public class ChunkyEvents extends ChunkyPlayerListener {
    @Override
    public void onPlayerChunkClaim(ChunkyPlayerChunkClaimEvent event) {
        ChunkyResident chunkyResident = new ChunkyResident(event.getChunkyPlayer());
        ChunkyTown chunkyTown = chunkyResident.getTown();
        TownChunk townChunk = new TownChunk(event.getChunkyChunk());
        if(chunkyTown == null) return;
        event.setCancelled(true);
        if(townChunk.isForSale()) {
            if(event.getChunkyChunk().isDirectlyOwnedBy(event.getChunkyPlayer())) {
                Language.sendBad(event.getChunkyPlayer(),"You cannot buy your own chunk.");
                return;
            }
            townChunk.buyChunk(chunkyResident);
            return;}

        if(chunkyResident.isAssistantOrMayor()) {
            if(chunkyTown.claimedChunkCount() >= chunkyTown.maxChunks()) {
                Language.sendBad(event.getChunkyPlayer(),"The town needs more influence to expand.");
                return;
            }
            if(!event.getChunkyChunk().isOwned()) {
                if(!isAdjacent(event.getChunkyChunk().getCoord(),chunkyTown)){
                    Language.sendBad(event.getChunkyPlayer(), "You may only expand next to owned chunks.");
                    return;
                }
                event.getChunkyChunk().setOwner(chunkyTown,true,true);
                Language.sendGood(event.getChunkyPlayer(),"You expanded " + chunkyTown.getName());
                event.getChunkyChunk().save();
                return;
            }
            else
                Language.CHUNK_OWNED.bad(event.getChunkyPlayer(),event.getChunkyChunk().getOwner().getName());
        }
    }


    private boolean isAdjacent(ChunkyCoordinates coordinates, ChunkyTown chunkyTown) {
        int X = coordinates.getX();
        int Z = coordinates.getZ();

        String world = coordinates.getWorld();
        for(int x=-1;x<2;x++) {
            for(int z=-1;z<2;z++) {
                if(x==0 && z==0) continue;
                ChunkyCoordinates coord = new ChunkyCoordinates(world,X+x,Z+z);
                ChunkyChunk chunk = ChunkyManager.getChunkyChunk(coord);
                if(chunk.isOwned() && chunk.isOwnedBy(chunkyTown)) return true;}}
        return false;
    }

    @Override
    public void onPlayerChunkChange(ChunkyPlayerChunkChangeEvent event) {
        ChunkyTown myTown = ChunkyTownManager.getTown(event.getChunkyPlayer());
        TownChunk toChunk = new TownChunk(event.getToChunk());
        ChunkyTown chunkTown = toChunk.getTown();
        if(myTown == null || chunkTown == null) return;

        if(toChunk.isForSale())
            event.setMessage(event.getToChunk().getOwner().getName() + " - on sale for: " + ChatColor.YELLOW  + Chunky.getMethod().format(toChunk.getCost()));
    }
}

