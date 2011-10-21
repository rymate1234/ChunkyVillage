package org.getchunky.chunkyvillage.commands.Town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunkyvillage.ChunkyVillage;
import org.getchunky.chunkyvillage.config.Config;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.permissions.Permissions;
import org.getchunky.chunkyvillage.tasks.TeleportPlayer;

public class Spawn implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        ChunkyResident chunkyResident = new ChunkyResident(sender);
        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You are not part of a town.");
            return;
        }

        ChunkyChunk currentChunk = chunkyResident.getChunkyPlayer().getCurrentChunk();
        if(!Permissions.TELEPORT.has(sender)) {
            if(currentChunk.isOwned() && !currentChunk.isOwnedBy(chunkyTown) && !chunkyResident.owns(currentChunk)) {
                Language.sendBad(chunkyResident.getChunkyPlayer(),"You cannot teleport from other town's land.");
                return;}}
        int time = Config.Options.TELEPORT_WARMUP.getInt();
        if(!Permissions.WARMUP.has(sender)) time = 0;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ChunkyVillage.getInstance(), new TeleportPlayer((Player)sender, chunkyTown.getHome().getCoord().toLocation()), 20L * time);
        if(time > 0) Language.sendGood(chunkyResident.getChunkyPlayer(), "You will be teleported in " + time + " seconds.");

    }
}
