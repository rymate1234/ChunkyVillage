package org.getchunky.chunkyvillage.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.object.player.ChunkyPlayerBuildEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkChangeEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkClaimEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerListener;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyCoordinates;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.ChunkyVillage;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.objects.TownChunk;
import org.getchunky.chunkyvillage.tasks.Explode;
import org.getchunky.chunkyvillage.tasks.RemoveBlock;
import org.getchunky.chunkyvillage.util.Config;

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

    @Override
    public void onPlayerUnownedBuild(ChunkyPlayerBuildEvent event) {
        Block block = event.getBlock();

        if(!Config.isWarTool(block.getTypeId())) return;

        TownChunk chunk = new TownChunk(event.getChunkyChunk());
        ChunkyTown defendingTown = chunk.getTown();
        if(defendingTown == null) return;
        ChunkyResident attacker = new ChunkyResident(event.getChunkyPlayer());
        ChunkyTown attackingTown = attacker.getTown();

        if(attackingTown == null) return;

        if(defendingTown.getEffectiveStance(attackingTown) != ChunkyTown.Stance.ENEMY) {
            Language.sendBad(attacker.getChunkyPlayer(), "This is not an enemy town.");
            return;
        }

        int cost = Config.getWarToolCost(block.getType().getId());

        if(attacker.getPlayTime() < cost) {
            Language.sendBad(attacker.getChunkyPlayer(), "You do not have enough Influence to do this.");
            return;
        }

        attacker.subtractPlayTime(cost);
        event.setCancelled(false);

        if(block.getType() == Material.TNT)
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ChunkyVillage.getInstance(), new Explode(block), 20L * 3);
        else Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ChunkyVillage.getInstance(), new RemoveBlock(block), 20L * 20);

        Language.sendGood(attacker.getChunkyPlayer(), "That cost " + cost + " Influence");


    }
}

