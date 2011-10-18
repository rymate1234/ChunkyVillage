package org.getchunky.chunkyvillage.commands.Resident;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class Resident implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {

        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);
        if(strings.length > 0) chunkyResident = new ChunkyResident(strings[0]);

        if(chunkyResident.getChunkyPlayer() == null) {
            Language.NO_SUCH_PLAYER.bad(sender, strings[0]);
            return;
        }

        //Print info
        sender.sendMessage(ChatColor.GRAY + "|-------------------" + ChatColor.GREEN + "["+ChatColor.GOLD + chunkyResident.getName()+ChatColor.GREEN + "]" + ChatColor.GRAY + "-------------------|");

        sender.sendMessage(
                ChatColor.GRAY + "| " + ChatColor.GREEN + "Bank: " + ChatColor.YELLOW + chunkyResident.getAccount().balance() +
                ChatColor.GRAY + " | "+ ChatColor.GREEN + "Influence: " + ChatColor.YELLOW + chunkyResident.getPlayTime());
        ChunkyTown chunkyTown = chunkyResident.getTown();

        if(chunkyTown == null) return;

        sender.sendMessage(
                ChatColor.GRAY + "| "+ ChatColor.GREEN + "Town: " + ChatColor.YELLOW + chunkyTown.getName());



    }

}
