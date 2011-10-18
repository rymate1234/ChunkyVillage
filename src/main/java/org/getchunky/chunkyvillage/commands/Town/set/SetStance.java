package org.getchunky.chunkyvillage.commands.Town.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class SetStance implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyResident chunkyResident = new ChunkyResident(sender);
        if(strings.length < 2) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "You must specify other town and stance.");
            return;}

        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(!chunkyResident.isMayor()) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You do not have the authority to do this");
            return;}

        ChunkyTown otherTown = ChunkyTownManager.matchTown(strings[0]);
        ChunkyTown.Stance result = chunkyTown.setStance(otherTown, strings[1]);
        if(result == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "This is already the current stance.");
            return;
        }

        chunkyTown.goodMessageTown("The town has changed its stance to " + result.toString() + ChatColor.GREEN + " with " + otherTown.getName());
        otherTown.goodMessageTown(chunkyTown.getName() + " has changed its stance to " + result.toString() + ChatColor.GREEN + " with us");
    }
}
