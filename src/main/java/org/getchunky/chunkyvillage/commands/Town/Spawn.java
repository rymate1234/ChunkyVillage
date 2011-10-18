package org.getchunky.chunkyvillage.commands.Town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

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
        if(!sender.hasPermission("chunky.admin.teleport")) {
            if(currentChunk.isOwned() && !currentChunk.isOwnedBy(chunkyTown) && !chunkyResident.owns(currentChunk)) {
                Language.sendBad(chunkyResident.getChunkyPlayer(),"You cannot teleport from other town's land.");
                return;}}

        ((Player)sender).teleport(chunkyTown.getHome().getCoord().toLocation());

    }
}
