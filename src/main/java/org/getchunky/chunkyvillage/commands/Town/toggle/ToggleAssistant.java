package org.getchunky.chunkyvillage.commands.Town.toggle;

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

public class ToggleAssistant implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
         if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 1) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"Please specify player.");
            return;
        }

        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You are not part of a town.");
            return;
        }

        if(!chunkyResident.isMayor()) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You do not have the authority to do this.");
            return;
        }

        ChunkyResident assistant = new ChunkyResident(strings[0]);

        if(!chunkyTown.isResident(assistant)) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"This player does not belong to your town.");
            return;
        }

        if(assistant.isAssistant()) {
            chunkyTown.removeAssistant(assistant);
            Language.sendMessage(assistant.getChunkyPlayer(), ChatColor.RED + "You have been demoted from assistant.");
            Language.sendMessage(chunkyResident.getChunkyPlayer(), ChatColor.RED + assistant.getName() + " has been demoted from assistant.");
        }
        else {
            chunkyTown.addAssistant(assistant);
            Language.sendGood(assistant.getChunkyPlayer(),"You have been promoted to assistant.");
            Language.sendGood(chunkyResident.getChunkyPlayer(),assistant.getName() + " has been promoted to assistant.");}


    }
}
